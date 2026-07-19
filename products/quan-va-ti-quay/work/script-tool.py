#!/usr/bin/env python3
"""Disassemble + vá script VM của game (toạ độ widget nằm trong script data).

Format đã giải mã từ k.java / f.java:
  - Resource id n: JAR entry str(n>>8), blob n&0xFF (pack format như patch-pack.py).
  - Bảng opcode: pack0/blob0 = [u16 opcodeCount][u16 varCount] rồi mỗi opcode:
    [u8 số operand][size từng operand (byte)...]
  - Script blob: bỏ 2 byte đầu, sau đó chuỗi lệnh:
    [u16 opcode LE][u16 flags LE][u8 present][operand LE, size theo bảng, present cái đầu]
    operand vắng mặt -> default k.a(op,idx). flag bit i -> operand i là BIẾN (không vá).
  - Opcode đặt widget (object[] = operand):
    40: alpha-rect  pos=(op7,op8)  wh=(op9,op10)
    41: g-attach    pos=(op4,op5)
    42: LIST (menu/thoại chọn) pos=(op2,op3)
    43: fillrect    pos=(op1,op2) wh=(op3,op4)
    44: sprite/anim pos=(op1,op2)
    45: TEXT        pos=(op1,op2)
    46: camera-follow clamp=(op1..op4)

Dùng:
  script-tool.py dis <jar> <resId>            # disassemble 1 script
  script-tool.py scan <jar>                   # thử parse mọi blob, liệt kê blob là script
  script-tool.py patch <jar> <patches.tsv>    # vá: mỗi dòng "resId insnOffset opIdx newValue"
"""
import sys, struct, zipfile

WIDGET_POS = {40: (7, 8), 41: (4, 5), 42: (2, 3), 43: (1, 2), 44: (1, 2), 45: (1, 2)}

def read_le(b, off, size):
    v = 0
    for i in range(size):
        v |= (b[off + i] & 0xFF) << (i * 8)
    return v

def write_le(b, off, size, v):
    for i in range(size):
        b[off + i] = (v >> (i * 8)) & 0xFF

def parse_pack(data):
    count = data[4]
    ends = [struct.unpack_from("<I", data, 5 + 4 * i)[0] for i in range(count)]
    base = 5 + 4 * count
    starts = [0] + ends[:-1]
    return [data[base + starts[i]:base + ends[i]] for i in range(count)]

class Jar:
    def __init__(self, path):
        self.z = zipfile.ZipFile(path)
        self.packs = {}
    def blob(self, res_id):
        name = str(res_id >> 8)
        if name not in self.packs:
            self.packs[name] = parse_pack(self.z.read(name))
        return self.packs[name][res_id & 0xFF]
    def entries(self):
        return [n for n in self.z.namelist() if n.isdigit()]

def load_optable(jar):
    d = jar.blob(0)  # pack0/blob0
    n_ops = struct.unpack_from("<H", d, 0)[0]
    pos = 4
    ops = []
    for _ in range(n_ops):
        cnt = d[pos]; pos += 1
        sizes = [d[pos + i] for i in range(cnt)]; pos += cnt
        ops.append(sizes)
    return ops

def walk(script, ops):
    """Yield (offset, opcode, flags, [(opIdx, valOffset, size, value|None)...]).
    value None = operand vắng (dùng default)."""
    b = script[2:]
    pos = 0
    while pos + 5 <= len(b):
        op = read_le(b, pos, 2)
        flags = read_le(b, pos + 2, 2)
        present = b[pos + 4]
        if op >= len(ops) or present > len(ops[op]):
            raise ValueError(f"bad insn @{pos}: op={op} present={present}")
        sizes = ops[op]
        o = pos + 5
        opers = []
        for i in range(len(sizes)):
            if i < present:
                opers.append((i, o + 2, sizes[i], read_le(b, o, sizes[i])))
                o += sizes[i]
            else:
                opers.append((i, None, sizes[i], None))
        yield pos + 2, op, flags, opers  # offset tính trên blob gốc (cả 2 byte đầu)
        pos = o
    if pos != len(b):
        raise ValueError(f"trailing {len(b)-pos} bytes")

def dis(jar, res_id, ops):
    s = jar.blob(res_id)
    print(f"== script res {res_id} (entry {res_id>>8} blob {res_id&0xFF}, {len(s)} bytes)")
    for off, op, flags, opers in walk(s, ops):
        vals = []
        for i, voff, size, v in opers:
            tag = "VAR" if flags >> i & 1 else ""
            vals.append(f"{i}={'∅' if v is None else v}{tag}")
        mark = " <== WIDGET" if op in WIDGET_POS or op == 46 else ""
        print(f"  @{off:5d} op{op:<3d} {' '.join(vals)}{mark}")

def scan(jar, ops):
    for name in sorted(jar.entries(), key=int):
        blobs = parse_pack(jar.z.read(name))
        for i in range(len(blobs)):
            rid = (int(name) << 8) | i
            if rid == 0:
                print(f"res {rid} = bảng opcode"); continue
            try:
                n = sum(1 for _ in walk(jar.blob(rid), ops))
                print(f"res {rid} (entry {name} blob {i}): SCRIPT, {n} lệnh, {len(jar.blob(rid))}B")
            except Exception:
                pass

def patch(jar_path, tsv, ops):
    jar = Jar(jar_path)
    edits = {}  # res_id -> [(insnOff, opIdx, newVal)]
    for line in open(tsv):
        line = line.split("#")[0].strip()
        if not line:
            continue
        rid, ioff, oidx, val = [int(x) for x in line.split()]
        edits.setdefault(rid, []).append((ioff, oidx, val))
    z = zipfile.ZipFile(jar_path)
    names = z.namelist()
    data = {n: bytearray(z.read(n)) for n in names}
    infos = {n: z.getinfo(n) for n in names}
    z.close()
    for rid, lst in edits.items():
        entry = str(rid >> 8)
        blobs = [bytearray(x) for x in parse_pack(data[entry])]
        blob = blobs[rid & 0xFF]
        insns = {off: (op, flags, opers) for off, op, flags, opers in walk(bytes(blob), ops)}
        for ioff, oidx, val in lst:
            if ioff not in insns:
                sys.exit(f"[FAIL] res {rid}: không có lệnh @ {ioff}")
            op, flags, opers = insns[ioff]
            i, voff, size, old = opers[oidx]
            if voff is None:
                sys.exit(f"[FAIL] res {rid}@{ioff}: operand {oidx} vắng mặt (default)")
            if flags >> oidx & 1:
                sys.exit(f"[FAIL] res {rid}@{ioff}: operand {oidx} là BIẾN")
            if val >= 1 << (8 * size):
                sys.exit(f"[FAIL] res {rid}@{ioff}: {val} không vừa {size} byte")
            write_le(blob, voff, size, val)
            print(f"[OK] res {rid} @{ioff} op{op} operand{oidx}: {old} -> {val}")
        # ghi lại pack: giữ 4 byte đầu, dựng lại ends
        out = bytearray(data[entry][:4]); out.append(len(blobs))
        acc = 0; ends = []
        for b in blobs:
            acc += len(b); ends.append(acc)
        for e in ends:
            out += struct.pack("<I", e)
        for b in blobs:
            out += b
        data[entry] = out
    import shutil
    tmp = jar_path + ".tmp"
    with zipfile.ZipFile(tmp, "w", zipfile.ZIP_DEFLATED) as outz:
        for n in names:
            zi = zipfile.ZipInfo(n, date_time=infos[n].date_time)
            zi.compress_type = infos[n].compress_type
            zi.external_attr = infos[n].external_attr
            outz.writestr(zi, bytes(data[n]))
    shutil.move(tmp, jar_path)
    print(f"[WROTE] {jar_path}")

def shiftx(jar_path, res_id, dx):
    """Cộng dx vào các phần tử X (chỉ số chẵn) của blob dạng [u16 count][count× i16].
    Dùng cho res1030 = 4 nút (x,y) mà cả rương lẫn con trỏ màn chọn màn cùng đọc."""
    import shutil
    z = zipfile.ZipFile(jar_path); names = z.namelist()
    data = {n: bytearray(z.read(n)) for n in names}
    infos = {n: z.getinfo(n) for n in names}; z.close()
    entry = str(res_id >> 8)
    blobs = [bytearray(x) for x in parse_pack(data[entry])]
    blob = blobs[res_id & 0xFF]
    count = blob[0] | blob[1] << 8
    for i in range(0, count, 2):  # chỉ X (cặp x,y)
        off = 2 + 2 * i
        x = struct.unpack_from("<h", blob, off)[0]
        struct.pack_into("<h", blob, off, x + dx)
    print(f"[OK] res{res_id}: +{dx} vào {count//2} toạ độ X")
    # dựng lại pack (giữ 4 byte đầu)
    out = bytearray(data[entry][:4]); out.append(len(blobs))
    acc = 0; ends = []
    for b in blobs:
        acc += len(b); ends.append(acc)
    for e in ends:
        out += struct.pack("<I", e)
    for b in blobs:
        out += b
    data[entry] = out
    tmp = jar_path + ".tmp"
    with zipfile.ZipFile(tmp, "w", zipfile.ZIP_DEFLATED) as outz:
        for n in names:
            zi = zipfile.ZipInfo(n, date_time=infos[n].date_time)
            zi.compress_type = infos[n].compress_type; zi.external_attr = infos[n].external_attr
            outz.writestr(zi, bytes(data[n]))
    shutil.move(tmp, jar_path)
    print(f"[WROTE] {jar_path} (entry '{entry}')")

if __name__ == "__main__":
    cmd = sys.argv[1]
    if cmd == "shiftx":
        shiftx(sys.argv[2], int(sys.argv[3]), int(sys.argv[4]))
    elif cmd == "patch":
        jar = Jar(sys.argv[2])
        patch(sys.argv[2], sys.argv[3], load_optable(jar))
    else:
        jar = Jar(sys.argv[2])
        ops = load_optable(jar)
        if cmd == "dis":
            dis(jar, int(sys.argv[3]), ops)
        elif cmd == "scan":
            scan(jar, ops)
