#!/usr/bin/env python3
"""In-place bytecode patcher cho Quan-va-Ti-Quay.

Vá THẲNG byte trong .class, GIỮ NGUYÊN độ dài lệnh -> không đụng bảng
StackMap/offset nhánh -> không cần preverify lại (class đã preverified sẵn).

Mỗi site vá được định vị bằng 1 "anchor" (chuỗi byte bytecode duy nhất) + offset
tương đối, rồi assert giá trị cũ trước khi ghi. Anchor không duy nhất hoặc byte cũ
lệch -> dừng ngay (fail loud), không vá mù.

Dùng:  patch-class.py <src.class> <dst.class> <mode> [arg]
  mode=splash Y   : dời Y ảnh splash (man n==0) -> proof tinh vi
  mode=menu       : nen menu (n==4) -> DO + full-width -> proof khong the nham
"""
import sys, struct


def _apply(data: bytearray, anchor: bytes, rel: int, expect: bytes, new: bytes, label: str):
    n = data.count(anchor)
    if n != 1:
        sys.exit(f"[FAIL] {label}: anchor xuat hien {n} lan (can dung 1).")
    off = data.find(anchor) + rel
    cur = bytes(data[off:off + len(expect)])
    if cur != expect:
        sys.exit(f"[FAIL] {label}: byte cu @0x{off:x} = {cur.hex(' ')} != {expect.hex(' ')}.")
    if len(new) != len(expect):
        sys.exit(f"[FAIL] {label}: doi do dai (phai giu nguyen).")
    data[off:off + len(new)] = new
    print(f"[OK] {label}: @0x{off:x} {expect.hex(' ')} -> {new.hex(' ')}")


# --- Spike splash: sipush 160 -> sipush Y (giu opcode sipush) trong f.g(int) n==0 ---
_SPLASH_ANCHOR = bytes.fromhex("1A9A002B07B800C34CB2005512 0A".replace(" ", ""))

def spike_splash(data: bytearray, new_y: int):
    _apply(data, _SPLASH_ANCHOR, 37,
           bytes([0x11, 0x00, 0xA0]),
           bytes([0x11]) + struct.pack(">h", new_y),
           f"splash-Y 160->{new_y}")


# --- Spike menu: (1) mau nen pool int 6801708 -> 0xFF0000 (do)
#                 (2) fillRect width 240 -> 320 (full man ngang) tai n==4 ---
_MENU_COLOR = b"\x03" + struct.pack(">i", 6801708)              # 03 00 67 C9 2C, duy nhat trong pool
_MENU_FILL_ANCHOR = bytes([0xA0, 0x00, 0x3E, 0xB2, 0x00, 0x55,  # if_icmpne; getstatic
                           0x12, 0x09, 0xB6, 0x01, 0x1E,        # ldc#9; invokevirtual setColor
                           0xB2, 0x00, 0x55, 0x03, 0x03])       # getstatic; iconst_0; iconst_0

def spike_menu(data: bytearray):
    _apply(data, _MENU_COLOR, 1,
           struct.pack(">i", 6801708), struct.pack(">i", 0xFF0000),
           "menu-color -> DO(0xFF0000)")
    _apply(data, _MENU_FILL_ANCHOR, 16,
           bytes([0x11, 0x00, 0xF0]), bytes([0x11, 0x01, 0x40]),
           "menu-fill width 240->320")


if __name__ == "__main__":
    src, dst, mode = sys.argv[1], sys.argv[2], sys.argv[3]
    d = bytearray(open(src, "rb").read())
    if mode == "splash":
        spike_splash(d, int(sys.argv[4]))
    elif mode == "menu":
        spike_menu(d)
    else:
        sys.exit("mode: splash <Y> | menu")
    open(dst, "wb").write(d)
    print(f"[WROTE] {dst}")
