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

## Trang tải game (site)

- Hiện tại: 1 trang duy nhất, `make-site.sh` sinh từ `products/*/release/` (jar + icon từ manifest, `shots/`, `description.txt`, `vendor.txt`), deploy GitHub Pages qua Actions — https://vietnt02.github.io/tra-thu-tuoi-tho/. CSS 2 tầng: nền CSS 2.1 cho Opera Mini/Symbian (người dùng thật mở bằng E72i), tầng hiện đại trong `@supports (display:flex) and (color:var(--gate))`.
- **[Tương lai] Khi nhiều game**: tách overview → detail (trang chủ chỉ liệt kê icon + tên, bấm vào mới ra ảnh/mô tả/nút tải) + ô search theo tên game. Để detail hết trên trang chủ như hiện nay thì nhiều game sẽ phải lướt rất lâu. Lưu ý search phải chạy được trên trình duyệt cổ (form GET thuần hoặc lọc phía server-build, không dựa JS hiện đại).

## Cấu trúc thư mục

```
docs/                 — kiến thức tái dùng (chung mọi game)
products/<game>/
  original/           — JAR gốc, bất biến
  (work/ decompiled/ build/ ... tạo khi bắt tay từng game)
```
