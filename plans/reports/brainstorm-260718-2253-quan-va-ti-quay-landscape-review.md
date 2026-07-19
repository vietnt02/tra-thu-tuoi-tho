# Brainstorm Review — Quan và Tí Quậy → landscape 320×240 (E72)

Ngày: 2026-07-18. Nguồn: consult kongming (đọc code decompiled + parse resource thật). Trạng thái: **review xong, CHƯA lên plan**.

## Bối cảnh
- Máy đích: Nokia E72 (RM-530), 320×240 QVGA ngang cứng, S60 3rd FP2.
- Game gốc khoá cứng portrait 240×320. Scope: tái tạo native (decisions.md D1–D5).
- Toolchain đã cài: OpenJDK 26 (`/opt/homebrew/opt/openjdk/bin/java`) + CFR (`tools/cfr.jar`). Đã decompile: `products/quan-va-ti-quay/work/decompiled/`.

## Kết luận chính
Hướng **vá bytecode + remap hằng số theo ý nghĩa = đúng**. Recompile bất khả (CFR sinh 9 chỗ `GOTO` không hợp lệ Java). Effort dịch từ "giải mã art + rìa map" (đã đóng) sang "reflow UI qua script-hook + swap ring buffer + input QWERTY".

## Rủi ro ĐÃ ĐÓNG (verified)
- **Art màn tĩnh KHÔNG cần encoder riêng**: container = `[u32 bỏ][u8 count][u32 end-offset×count][blobs]`, parse khớp 100% byte cả 12 pack. Màn tĩnh là **PNG chuẩn nằm trong pack** (file 3/blob0 = title 240×320 id768; file 4/blob0 = menu bg 240×320 id1024; …). → vẽ PNG 320×240 mới, thay blob, patch toạ độ vẽ. Repack lossless ~15 dòng Python.
- **Rìa bản đồ KHÔNG lộ trống**: clamp camera có sẵn (`g.java:216-225` → `f.java:1550,1566` → `b.java:214-225,269-284`). 5 map, nhỏ nhất 480×336px → viewport 320×240 không bao giờ chạm mép.

## Bản đồ remap (~8 cụm, KHÔNG rải 56 chỗ)
1. Camera nửa-màn (120,160)→(160,120): `f.java:722-723`, `1677-1678`, `b.java:212-213`.
2. Clamp max-scroll: `g.java:217-218` (đổi 240↔320).
3. Viewport clip tile: `g.java:191-198`.
4. **⚠ Ring buffer map (khó nhất)**: `createImage(264,360)` `f.java:868` + `%264/%360`, tile-count 11×15 → 15×11 (360×264): `g.java:112-113,119-149,163-165,260,268`. Sai → map vẽ rác.
5. Màn tĩnh: `f.g()` `f.java:2039-2095` + `f.i` 2121 + `f.d()` 2147-2148.
6. Wrap text 240: `d.java:390,495`, `b.java:471`.
7. Neo softkey: `f.java:996,999` → (0,240)/(320,240).
8. Default script opcode40: `k.java:125,128`.

**Bẫy chồng nghĩa (KHÔNG đụng)**: `f.java:675` (120=ngưỡng khoảng cách), `b.java:713,756` (%360=độ góc sin), `f.java:931,939` (480,640=điểm đậu entity).

## RỦI RO MỚI (chưa từng liệt kê)
1. **Layout UI nằm trong SCRIPT data ở resource, KHÔNG trong hằng Java** — toạ độ UI truyền qua opcode script (`f.java:1479-1537…`), engine chỉ nhận `b.b(x,y)`. Remap hằng KHÔNG relayout được menu/dialog/HUD. → giải pháp: **hook điểm set-vị-trí bằng class helper + bảng remap theo (màn, entity)**, không đụng resource. Đây là phần thủ công nặng nhất (khớp D3 "60-70% tay").
2. **E72 = QWERTY, không có phím số** — `f.a(int)` `f.java:186-244` map arrow + softkey + phím số `0-9 * #`. Di chuyển (d-pad/arrow) OK; nhưng hành động gán vào phím số/`*`/`#` sẽ khó bấm. → cần **đo keycode thật trên E72** + chốt scheme phím với client.

## Toolchain gaps
- Chưa có preverify → dùng **ProGuard `-microedition`** (chạy trên JDK mới, thay WTK).
- ASM drop `StackMap` attribute CLDC khi rewrite → **re-preverify sau mỗi patch** bắt buộc.
- Nếu thêm class helper Java: javac 26 không target classfile cổ → downgrade qua ProGuard `-target` hoặc Krakatau.
- FreeJ2ME: user nói có, **chưa verify trong repo**.

## Thứ tự phase đề xuất (chốt ở bước plan)
- **Spike (1-2 ngày, làm ngay)**: unjar → patch 1 hằng vô hại → ProGuard preverify → repack jar/jad → FreeJ2ME xanh → **chạy trên E72 thật**. +30': đo keycode QWERTY bằng chính build spike.
- **P1 World landscape**: cụm 1-4 + k defaults. Nghiệm thu: đi lại đủ 5 map full 320×240, fps OK trên E72.
- **P2 Màn tĩnh**: PNG 320×240 mới + repack + patch `f.g/f.i/f.d`. Song song P1.
- **P3 HUD/dialog/menu reflow**: hook vị trí entity UI + wrap text + softkey + anim thả item. Nặng nhất.
- **P4 Input QWERTY + regression**: map phím + giữ audio/save/vibrate + chơi trọn game FreeJ2ME + E72.

## Câu hỏi mở cần chốt trước/khi plan
1. **[client] Scheme điều khiển trên E72** — chấp nhận remap hành động sang phím chữ/d-pad? (chốt chính xác sau khi đo keycode ở spike).
2. **[client] Art màn tĩnh** — ai vẽ 2 PNG full-screen 320×240 (title + menu bg) + duyệt mockup trước khi patch?
3. **[kỹ thuật] Xác nhận FreeJ2ME có sẵn** hay cần tải.
4. **[client] Thoại wrap 320** → ít dòng hơn/phân trang đổi — coi là đúng chuẩn native, chỉ ghi nhận.
