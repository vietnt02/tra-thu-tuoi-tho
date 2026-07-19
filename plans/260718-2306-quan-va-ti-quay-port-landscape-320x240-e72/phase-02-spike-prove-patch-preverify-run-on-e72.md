---
phase: 2
title: "Spike: patch→preverify→run trên E72 + đo phím"
status: in-progress  # 2026-07-19: spike.jar/.jad build xong (splash Y 160->60, đổi đúng 1 byte). ĐANG CHỜ client cài lên E72 xác nhận go/no-go. Đo phím = build kế tiếp.
priority: P1
effort: "1-2d"
dependencies: [1]
---

# Phase 2: Spike — chứng minh vá chạy được trên E72 thật + đo phím

## Overview
Chứng minh rủi ro #1 (D4-1): vá 1 hằng số vô hại → preverify → repack → **chạy trên E72 thật**, không chỉ emulator. Nhét thêm đo keycode QWERTY. Đây là cổng go/no-go trước khi đổ công vào các phase nặng.

## Requirements
- Functional: build đã vá cài & chạy trên E72; 1 thay đổi hình học nhỏ hiện đúng; in được keycode phím bấm.
- Non-functional: chuỗi lặp lại được (chạy lại repack.sh ra cùng kết quả).

## Architecture
- **Patch thử nghiệm an toàn**: đổi 1 site hằng số dễ thấy nhưng vô hại (vd vị trí vẽ màn tĩnh). Mục tiêu: *thấy khác biệt* → patch có hiệu lực.
- **Patch path rủi ro thật (FM7)**: spike KHÔNG chỉ vá 1 hằng vô hại — phải thêm **1 patch multi-constant trong method OBFUSCATE** (vd camera nửa-màn `f.java:722-723`), preverify, xác nhận **StackMap tái tạo sạch** sau ASM edit và E72 verify được. Đây mới là thứ hay vỡ, phải chứng minh ở gate, không để lộ ở P3.
- **Đo phím**: patch tạm để in keycode `keyPressed(int)` lên màn (overlay góc). Chạy trên E72, bấm d-pad / phím số (mode số) / softkey → ghi lại keycode thật. Dùng để đóng open-question input (D7).
- Cài lên E72: qua App Manager (MIDlet unsigned — chấp nhận prompt), Bluetooth/USB.

## Related Code Files
- Modify (tạm, bản copy): `products/quan-va-ti-quay/work/extracted/*.class` (1 hằng số + hook in keycode).
- Create: `products/quan-va-ti-quay/work/build/spike.jar` + `.jad`.
- Reference: input map tại `f.java:186-244`; site hằng an toàn ví dụ `f.java:2121` (drawImage title).

## Implementation Steps
1. Patch 1 hằng vô hại (vd dịch 1 phần tử màn tĩnh vài px) qua ASM → preverify → repack.
2. Chạy trong FreeJ2ME: xác nhận khác biệt hiện đúng, game vẫn chạy.
3. Cài build lên **E72 thật**, chạy: xác nhận cài được, chạy được, khác biệt đúng.
4. Thêm patch in keycode → chạy E72 → bấm hết d-pad / số (mode số) / softkey / `*` `#` → ghi bảng keycode.
5. Xác nhận d-pad phát mã âm khi đang ở mode số (đóng open-question D7). **D7 là TẠM cho tới bước này (AD6)**: nếu E72 phát mã KHÁC `-1..-4` hoặc d-pad không ăn ở mode số → **cho phép remap/thêm shim dịch phím** (gỡ ràng buộc "không remap"). Ghi kết quả vào `docs/products-quan-va-ti-quay.md`.
6. Kết luận go/no-go: nếu preverify/cài/chạy đều xanh → tiếp P3. Nếu vỡ → **tự thử đường vá khó hơn** (Krakatau assemble / vá hex thủ công) trước; chỉ escalate cho client khi đã cạn các cách vá. Không dừng dự án vì 1 trắc trở (validate session 1).

## Success Criteria
- [ ] Build đã vá cài & chạy được trên E72 thật, không lỗi verify/crash.
- [ ] Thay đổi hình học nhỏ hiện đúng trên màn.
- [ ] Bảng keycode E72 (d-pad, số, softkey, `*`, `#`) đo xong, ghi vào docs.
- [ ] Xác nhận d-pad ăn ở mode số; ghi nhận có/không màn gõ chữ.
- [ ] Quyết định go/no-go ghi lại.

## Risk Assessment
- **Cổng go/no-go**: nếu recompile/preverify path vỡ ở đây → đổi chiến lược (Krakatau assemble, hoặc vá hex thủ công). Đây đúng là chỗ phải lộ sớm — không đi tiếp khi chưa xanh.
- E72 từ chối unsigned MIDlet → đổi setting App Manager / self-sign. Ghi lại cách cài.
- Không có cáp/BT sẵn → chuẩn bị trước khi vào phase.
