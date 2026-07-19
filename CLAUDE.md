# CLAUDE.md — j2me-display-porting

Context bàn giao cho agent làm việc trong cwd này. Đọc file này + `docs/` trước khi bắt tay.

## Project là gì (1 dòng)

Mod các game J2ME (JAR/MIDP) để chạy đúng trên màn hình đích khác kích thước/hướng với bản gốc — bắt đầu bằng **portrait 240x320 → native landscape 320x240 (đứng thẳng)** cho điện thoại Nokia màn ngang.

## Trạng thái hiện tại

- Giai đoạn: **khảo sát + lập kế hoạch xong**, CHƯA viết code, CHƯA cài toolchain.
- Sản phẩm đầu tiên: `products/quan-va-ti-quay/` (game "Quan và Tí Quậy", obfuscated MIDP-2.0).
- Quyết định đã chốt: xem `docs/decisions.md`.
- Việc kế tiếp: chạy **spike (task 0–2)** trong `docs/products-quan-va-ti-quay.md` — cài toolchain, decompile, chứng minh build lại được. Chưa cam kết phần nặng (relayout + vẽ art) cho tới khi spike xanh.

## Ranh giới quan trọng (đọc kỹ, tránh đi sai hướng)

- **Máy đích là Nokia màn NGANG cứng 320x240.** => KHÔNG dùng giải pháp "xoay 90°" (rotate-blit); người dùng đã loại vì phải quay ngang điện thoại, điều khiển bất tiện. Bắt buộc **reflow UI đứng thẳng** (gọi là "Kiểu B" trong docs).
- **Lần này làm THẲNG cho từng game, KHÔNG dựng framework.** Mục tiêu lấy kinh nghiệm. Framework tổng quát để dành làm sau, dựa trên docs của project này.
- Game này chỉ là **1 trong nhiều sản phẩm** của project. Đặt mọi thứ tái dùng được vào `docs/`; thứ riêng game để trong `products/<game>/`.

## Cấu trúc

```
docs/
  project-overview.md   — tầm nhìn, phạm vi, roadmap
  decisions.md          — các quyết định + lý do (vì sao Kiểu B, vì sao chưa framework)
  technique.md          — kỹ thuật mod: offscreen, remap toạ độ, rebuild pipeline, xử lý obfuscation
  toolchain-setup.md    — cài JDK + CFR + preverify + đóng gói jar/jad
  products-quan-va-ti-quay.md — findings bytecode + tasklist ước tính + trạng thái game đầu tiên
products/
  quan-va-ti-quay/
    original/quan-va-ti-quay.jar   — bản gốc (nguồn tham chiếu, KHÔNG sửa)
```

## Nguyên tắc làm việc

- Luôn giữ `original/` bất biến; làm việc trên bản copy khác.
- Verify từng bước trên FreeJ2ME, và cuối cùng trên **Nokia thật** (perf/crash ẩn chỉ lộ ở máy thật).
- Ghi phát hiện mới tái dùng được vào `docs/technique.md`; findings riêng game vào file product.
