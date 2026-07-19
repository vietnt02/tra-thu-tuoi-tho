---
phase: 6
title: "Input verify + full regression"
status: pending
priority: P1
effort: "2-4d"
dependencies: [5]
---

# Phase 6: Input verify + full regression

## Overview
Đóng game: xác nhận điều khiển trên E72 (không remap — mode số), giữ nguyên audio/save/vibrate, chơi trọn game bắt lỗi. Đóng gói jar/jad cuối.

## Requirements
- Functional: chơi được từ đầu tới cuối trên E72; audio/save/vibrate hoạt động; điều khiển ổn ở mode số.
- Non-functional: không regression so với bản gốc (ngoài phần đã đổi chủ đích).

## Architecture
- **Input — TIÊU THỤ kết quả P2 (Codex)**: KHÔNG cứng "không remap". Nếu **P2 đo thấy mã E72 khác `-1..-4` / d-pad chết ở mode số → áp shim/remap theo quyết định P2**; nếu P2 xác nhận mã chuẩn → giữ mapping gốc. Xác nhận trên E72: d-pad di chuyển, phím số/`*`/`#` ở mode số cho hành động; nếu có màn gõ chữ (đặt tên save) → kiểm chuyển mode chữ↔số mượt.
- **Bảo toàn**: audio (`media.Player`), save (`RecordStore` — giữ tên store gốc, không đổi format), vibrate — không đụng khi patch hình học; verify còn nguyên.
- **Đóng gói**: repack.sh ra `quan-va-ti-quay-landscape.jar` + `.jad` (đồng bộ size, giữ manifest `MIDlet-1=…,CGTQ`).

## Related Code Files
- Modify: chỉ khi input cần chỉnh nhỏ (ưu tiên không đụng).
- Reference: input `f.java:186-244`; audio/save trong `f.class`.
- Output: `products/quan-va-ti-quay/work/build/quan-va-ti-quay-landscape.{jar,jad}`.

## Implementation Steps
1. Verify input trên E72: đi lại, mọi hành động, menu — ở mode số. Ghi bug nếu có.
2. Verify save: tạo save, thoát, vào lại — dữ liệu còn. Kiểm tương thích format gốc.
3. Verify audio + vibrate hoạt động các điểm quen thuộc.
4. Chơi xuyên suốt **tuần tự** (game gated — không nhảy màn, giữ save tích luỹ theo tiến trình; validate session 1) trên FreeJ2ME → E72: soi mọi màn (world/menu/dialog/HUD/title) đúng native, không viền/độn/rác. Test tăng dần theo tiến độ dev, tới đâu chơi được kiểm tới đó.
5. Sửa regression phát sinh (giữ diff cô lập).
6. Đóng gói jar/jad cuối; xác nhận cài & chạy sạch trên E72.

## Success Criteria
- [ ] Chơi trọn game trên E72, không crash, điều khiển ổn ở mode số.
- [ ] Save/load đúng, format tương thích; audio + vibrate hoạt động.
- [ ] Mọi loại màn đều native 320×240, không lỗi hiển thị.
- [ ] jar/jad cuối cài chạy sạch trên E72, preverify không lỗi.

## Risk Assessment
- Bug ẩn chỉ lộ ở E72 thật (perf, phím, crash) → playthrough đầy đủ trên máy thật là bắt buộc, không chỉ emulator.
- Mode số + màn gõ chữ (nếu có) gây vướng → xử lý ở đây; nếu nặng, cân nhắc remap tối thiểu 1-2 phím (đổi D7, hỏi client trước).
- Save thực ra RỦI RO THẤP (FM note): blob RecordStore chỉ ~2 byte (level index `k.a[1]` + 1 flag), không chứa toạ độ/màn (`f.java:2107-2109`, `f.java:362-388`). Hazard thật duy nhất = vá mù class `k` đụng mảng dữ liệu `k.a[]` thay vì method default `k.a(int,int)` → giữ patch `k` **theo lệnh** (chỉ `k.java:125,128`), không theo class.
