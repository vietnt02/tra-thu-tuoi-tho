#!/usr/bin/env python3
"""Đọc/ghi resource pack của game (định dạng đã giải: `[u32 bỏ][u8 count][u32 LE
end-offset × count][blobs]`; blob i = data[start_i:end_i], end tích luỹ).

f.a(n): mở JAR entry "n>>8", trả blob "n&0xFF". Dùng để THAY tranh 1 màn:
thay blob PNG bằng bản 320×240 rồi ghi lại pack (giữ nguyên header u32, tính lại ends).

Dùng:  patch-pack.py <jar> <packName> <blobIndex> <new.png>   (ghi đè entry trong jar)
"""
import sys, struct, zipfile, shutil, os

def parse(pack: bytes):
    skip = pack[:4]
    count = pack[4]
    ends = [struct.unpack_from("<I", pack, 5 + 4 * i)[0] for i in range(count)]
    base = 5 + 4 * count
    starts = [0] + ends[:-1]
    blobs = [pack[base + starts[i]:base + ends[i]] for i in range(count)]
    return skip, blobs

def build(skip: bytes, blobs) -> bytes:
    out = bytearray(skip)
    out.append(len(blobs))
    acc = 0
    ends = []
    for b in blobs:
        acc += len(b); ends.append(acc)
    for e in ends:
        out += struct.pack("<I", e)
    for b in blobs:
        out += b
    return bytes(out)

def replace_blob(pack: bytes, index: int, new_blob: bytes) -> bytes:
    skip, blobs = parse(pack)
    if index >= len(blobs):
        sys.exit(f"[FAIL] blob index {index} >= count {len(blobs)}")
    old = blobs[index]
    blobs[index] = new_blob
    print(f"[OK] pack blob {index}: {len(old)} -> {len(new_blob)} bytes")
    return build(skip, blobs)

def replace_in_jar(jar: str, pack_name: str, index: int, png_path: str):
    new_blob = open(png_path, "rb").read()
    z = zipfile.ZipFile(jar)
    names = z.namelist()
    data = {n: z.read(n) for n in names}
    infos = {n: z.getinfo(n) for n in names}
    z.close()
    data[pack_name] = replace_blob(data[pack_name], index, new_blob)
    tmp = jar + ".tmp"
    with zipfile.ZipFile(tmp, "w", zipfile.ZIP_DEFLATED) as out:
        for n in names:
            # giữ nguyên compress type gốc để các entry khác byte-ổn định
            zi = zipfile.ZipInfo(n, date_time=infos[n].date_time)
            zi.compress_type = infos[n].compress_type
            zi.external_attr = infos[n].external_attr
            out.writestr(zi, data[n])
    shutil.move(tmp, jar)
    print(f"[WROTE] {jar} (entry '{pack_name}')")

if __name__ == "__main__":
    replace_in_jar(sys.argv[1], sys.argv[2], int(sys.argv[3]), sys.argv[4])
