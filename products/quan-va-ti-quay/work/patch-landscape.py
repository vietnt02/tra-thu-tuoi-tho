#!/usr/bin/env python3
"""Landscape viewport reflow — same-length byte swaps (StackMap-safe, no preverify).

Đổi khung nhìn thế giới từ dọc 240x320 sang ngang 320x240:
  g.class: mọi `sipush 240` <-> `sipush 320` (viewport width/height trong clamp a()
           và vòng vẽ tile a(Graphics,int)). Đã xác nhận toàn class chỉ có 3+3 site,
           tất cả đều là kích thước khung nhìn -> swap toàn bộ an toàn.
  f.class: ring buffer `createImage(264,360)` -> `createImage(360,264)` (đủ rộng >=320,
           đủ cao >=240 cho khung ngang). sipush 264 <-> sipush 360.

CHƯA xử lý (cần đường ASM+preverify vì đổi độ dài lệnh bipush->sipush):
  tâm camera `-120/-160` -> `-160/-120` (f.java 722-723, 1677-1678). Thiếu bước này
  khung nhìn vẫn rộng 320 nhưng tâm lệch ~40px — chấp nhận cho bản đầu, tinh chỉnh sau.

Dùng:  patch-landscape.py <stage_dir>
"""
import sys, os

SI = 0x11  # sipush opcode

def _find_all(data: bytearray, pat: bytes):
    out, i = [], data.find(pat)
    while i != -1:
        out.append(i)
        i = data.find(pat, i + 1)
    return out

def swap_sipush(path: str, a: int, b: int, expect_a: int, expect_b: int, label: str):
    """Swap every `sipush a` with `sipush b` (same length). Assert exact counts."""
    d = bytearray(open(path, "rb").read())
    pa = bytes([SI, (a >> 8) & 0xFF, a & 0xFF])
    pb = bytes([SI, (b >> 8) & 0xFF, b & 0xFF])
    oa, ob = _find_all(d, pa), _find_all(d, pb)
    if len(oa) != expect_a or len(ob) != expect_b:
        sys.exit(f"[FAIL] {label}: count {a}={len(oa)} (want {expect_a}) {b}={len(ob)} (want {expect_b})")
    for off in oa:
        d[off:off + 3] = pb
    for off in ob:
        d[off:off + 3] = pa
    open(path, "wb").write(d)
    print(f"[OK] {label}: swapped {len(oa)}x sipush {a} <-> {len(ob)}x sipush {b} in {os.path.basename(path)}")

def swap_drawpos(path, anchor: bytes, rel: int, old: bytes, new: bytes, label: str):
    """Đảo vị trí drawImage (giữ nguyên tổng độ dài -> StackMap an toàn).
    vd (120,160)->(160,120): `bipush120;sipush160` <-> `sipush160;bipush120`."""
    d = bytearray(open(path, "rb").read())
    if d.count(anchor) != 1:
        sys.exit(f"[FAIL] {label}: anchor count {d.count(anchor)} != 1")
    off = d.find(anchor) + rel
    if bytes(d[off:off+len(old)]) != old:
        sys.exit(f"[FAIL] {label}: bytes @0x{off:x}={d[off:off+len(old)].hex(' ')} != {old.hex(' ')}")
    d[off:off+len(new)] = new
    open(path, "wb").write(d)
    print(f"[OK] {label}: reposition @0x{off:x}")

# Nền menu (f.a(768), g() n==2) vẽ tại (120,160) center -> (160,120) để ảnh 320x240 phủ kín
_MENU_DRAW_ANCHOR = bytes([0x11,0x03,0x00,0xB8,0x00,0xC3,0x4C,0xB2,0x00,0x55,0x2B])

if __name__ == "__main__":
    stage = sys.argv[1]
    swap_sipush(os.path.join(stage, "g.class"), 240, 320, 3, 3, "viewport g.class 240<->320")
    swap_sipush(os.path.join(stage, "f.class"), 264, 360, 1, 1, "ringbuffer f.class 264<->360")
    # NOTE: f.a(768)/g() n==2 KHÔNG phải nền menu (menu = banner res2 + nút + nhân vật,
    # ghép nhiều mảnh). Draw-swap menu tạm gỡ tới khi định đúng đường vẽ nền menu.
