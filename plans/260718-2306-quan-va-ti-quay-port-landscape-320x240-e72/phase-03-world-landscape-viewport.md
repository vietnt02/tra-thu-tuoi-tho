---
phase: 3
title: "World landscape viewport"
status: pending
priority: P1
effort: "3-6d"
dependencies: [2]
---

# Phase 3: World landscape viewport

## Overview
Reflow phần đang chơi: đổi khung nhìn 240×320 → 320×240 để engine camera sẵn có cho thấy thế giới rộng hơn (ví dụ client "3 cây + đống rơm"). Remap các cụm hằng số hình học **theo ý nghĩa**, gồm cụm khó nhất: ring buffer map.

## Requirements
- Functional: đi lại đủ 5 map, world phủ kín 320×240, camera bám nhân vật, clamp mép đúng, tile vẽ không rác.
- Non-functional: không tụt fps rõ trên E72. (Lưu ý Codex: viewport 320×240 = 76.800 px, bằng đúng 240×320 cũ; buffer cũng không đổi. Rộng +33% nhưng cao −25% → **tải/frame gần như KHÔNG tăng**, lo fps nhẹ hơn tưởng.)

## Architecture
Remap theo bản đồ site đã verify (report brainstorm-260718-2253). CHỈ sửa các site đúng nghĩa hình học; KHÔNG đụng site chồng nghĩa.

- **Nửa-màn camera** (120,160)→(160,120): `f.java:722-723`, `f.java:1677-1678`, `b.java:212-213`.
- **Clamp max-scroll** (mapW−240 / mapH−320): `g.java:217-218` → đổi 240↔320.
- **Viewport clip tile**: `g.java:191-198`.
- **Ring buffer map (KHÓ NHẤT, ATOMIC — FM5)**: `createImage(264,360)` `f.java:868`; `%264/%360` `g.java:112-113`; blit wrap `+264/+360` `g.java:163-165`; guard `>=360/>=264` `g.java:260,268`; tile-count 11×15→15×11 (kể cả arg trộn trục `g.java:137,149`) trong `g.java:119-149`. **KHÔNG chia nhỏ được**: mọi trạng thái dở đều ra rác giống nhau → không bisect được. Làm **1 commit không chia**, kèm **bảng kiểm kê hằng** (mỗi literal → trục → giá trị mới), validate bằng test "đi nhanh 4 hướng qua seam map".
- **Default script opcode40** (w=240,h=320): `k.java:125,128` — **audit consumer trước khi flip (AD5)**. **+ Codex risk**: `k.b(int)` trả operand TƯỜNG MINH nếu script có, chỉ fallback default khi vắng (`k.java:73`) → **script nào mã hoá thẳng 240,320 sẽ vẫn portrait** dù đã sửa default. Phải kiểm kê operand tường minh trong script (cần soi resource script, không chỉ code).
- **Overlay fill 240×320**: `f.java:787,2079,2087` **+ các `fillRect(0,0,240,320)` bị sót: `f.java:2042,2048,2057,2092`** (FM3/AD4) → `320,240`. Không xoá dải phải 80px = ra rác.
- **Camera clamp có thể theo SCRIPT (AD1)**: mốc pan `a[1..4]` đọc từ operand script (`b.java:216-220`, `f.java:1680-1689`), calibrate cho 240-rộng. Cutscene/pan có thể lộ vùng ngoài khi viewport thành 320-rộng. → đưa mốc camera script vào phạm vi hook của P5; KHÔNG coi "rìa đã đóng" cho tới khi path pan script được phủ.
- **Vá THEO TỪNG LỆNH, không theo giá trị/theo class (FM2)**: mỗi patch = (method + chỉ số lệnh) cụ thể. **Lưu ý Codex**: mọi số `f.java:NNN` ở đây là **dòng file giải mã CFR, KHÔNG phải vị trí bytecode thật** — chỉ dùng tham chiếu; định vị thật (owner + tên hàm + JVM descriptor + fingerprint + instruction index) suy ra từ disassembly class lúc vá. Nhất là các hàm nạp chồng nặng (vd `f.a(...)`).
- **Merge với P4 (Codex ordering)**: P3 và P4 cùng đụng vùng `f.java:2039-2095` → dùng **1 bảng patch gộp / thứ tự merge rõ**, tránh 2 patch chồng lên nhau sau preverify.
- **TRÁNH (chồng nghĩa — mở rộng)**: `f.java:675` (120=ngưỡng khoảng cách world), `b.java:713,756` (%360=độ góc sin), `f.java:931,939` (480,640=điểm đậu entity), **`f.java:2164` (120='x' trong BẢNG KEYCODE — FM2, swap nhầm = vỡ input)**.

## Related Code Files
- Modify (bytecode, bản copy): `f.class`, `g.class`, `b.class`, `k.class` tại các site trên.
- Reference (đọc): `work/decompiled/{f,g,b,k}.java`.
- Không đụng: resource art, class audio/save.

## Implementation Steps
0. **Gate map-dim (AD2)**: dump kích thước cả 5 map từ resource (tile-count × 24), **assert mọi map ≥ 320×240 rộng/cao**. Nếu có map < 320 rộng → map đó cần xử lý riêng (extend/letterbox), KHÔNG dùng clamp remap chung. Chưa pass gate này thì chưa swap viewport.
1. Remap cụm nửa-màn camera + clip viewport trước (ít rủi ro) → preverify → FreeJ2ME: nhân vật giữa màn, world rộng ra.
2. Remap clamp max-scroll → kiểm 4 mép mỗi map không lộ trống.
3. **Ring buffer swap** (buffer 264×360→360×264, tile 11×15→15×11): sửa + test kỹ, đây là chỗ dễ ra rác nhất. Test đi lại nhanh mọi hướng.
4. Remap opcode40 default + overlay fill.
5. Audit site chồng nghĩa: xác nhận đã TRÁNH đúng; xử lý riêng 480/640 nếu entity đậu lỗi.
6. Chạy đủ 5 map trên FreeJ2ME rồi E72 thật: đo fps, kiểm rác/mép.

## Success Criteria
- [ ] 5 map đi lại full 320×240, không viền/trống mép, **camera bám nhân vật + clamp entity-follow đúng**. (Mốc camera theo SCRIPT/pan cutscene KHÔNG thuộc P3 — xử lý ở P5; nghiệm thu P3 loại trừ pan script, tránh mâu thuẫn Codex.)
- [ ] Tile không rác khi cuộn nhanh mọi hướng (ring buffer đúng).
- [ ] fps trên E72 chấp nhận được (không giật rõ).
- [ ] Site chồng nghĩa không bị đụng nhầm (ngưỡng khoảng cách, sin, điểm đậu vẫn đúng).

## Risk Assessment
- **Ring buffer** là rủi ro kỹ thuật cao nhất phase — sai toán %/tile-count = map rác. **ATOMIC (FM5)**: KHÔNG bisect được (trạng thái dở đều rác giống nhau). Mitigation: bảng kiểm kê hằng viết trước, áp cùng lúc 1 commit, validate bằng test đi nhanh qua seam 4 hướng; nếu rác thì soi toàn bộ ~20 site theo bảng, không dựa incremental.
- Chồng nghĩa 120/360 → remap nhầm gây bug ẩn (AI/animation sai). Mitigation: bám checklist site, review từng site.
- fps trên E72: tải/frame gần như không đổi (Codex: 320×240 = 240×320 về số px) → rủi ro fps THẤP; vẫn đo máy thật, chưa tối ưu sớm (YAGNI).
