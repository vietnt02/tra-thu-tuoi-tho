# Project Overview

## Tầm nhìn

Adapter/porting cho game J2ME cũ: đưa game thiết kế cho **một kích thước/hướng màn hình** chạy đúng trên **màn hình đích khác**. Bắt đầu hẹp (1 hướng, 1 vài game), mở rộng dần.

Tên project đặt trung tính (`j2me-display-porting`) vì:
- Không chỉ 1 game — mỗi game là 1 "product" trong `products/`.
- Không chỉ 240x320→320x240 — về sau có thể thêm cặp độ phân giải/hướng khác.
- Về sau có thể tiến hoá từ "mod tay từng game" → "framework/tool bán tự động".

## Phạm vi hiện tại (thu hẹp có chủ đích)

- **Vào**: JAR game J2ME portrait 240x320 (MIDP-2.0/CLDC-1.0).
- **Ra**: JAR chạy native landscape 320x240, **UI đứng thẳng** (không xoay), điều khiển bình thường.
- **Thiết bị đích**: Nokia màn ngang cứng 320x240.
- **Cách làm**: thủ công từng game (chưa framework).

## Ngoài phạm vi (lúc này)

- Framework/tool tổng quát tự động (để dành — xem `decisions.md`).
- Giải pháp xoay 90° (rotate) — đã loại vì máy đích ngang cứng.
- Nâng độ phân giải/HD remaster.

## Roadmap

1. **[Hiện tại] Product #1 — Quan và Tí Quậy**: làm tay trọn vẹn, rút ra pattern tái dùng. Ghi lại mọi bài học vào `technique.md`.
2. **Product #2, #3...**: lặp lại quy trình, mỗi lần bồi thêm `technique.md`. Nhận diện phần lặp đi lặp lại.
3. **[Tương lai] Framework**: khi đã mod đủ vài game và thấy rõ phần chung, dựng scaffold bán tự động (xem `decisions.md` mục "Ranh giới tool hoá").

## Cấu trúc thư mục

```
docs/                 — kiến thức tái dùng (chung mọi game)
products/<game>/
  original/           — JAR gốc, bất biến
  (work/ decompiled/ build/ ... tạo khi bắt tay từng game)
```
