# j2me-display-porting

Mod game J2ME (JAR/MIDP) để chạy đúng trên màn hình đích khác kích thước/hướng với bản gốc.

Sản phẩm đầu tiên: đưa game portrait **240x320** chạy **native landscape 320x240 (đứng thẳng)** trên Nokia màn ngang.

## Tài liệu

| File | Nội dung |
|------|----------|
| [CLAUDE.md](CLAUDE.md) | Context bàn giao cho agent — đọc đầu tiên |
| [docs/project-overview.md](docs/project-overview.md) | Tầm nhìn, phạm vi, roadmap |
| [docs/decisions.md](docs/decisions.md) | Các quyết định + lý do |
| [docs/technique.md](docs/technique.md) | Kỹ thuật mod (offscreen, remap toạ độ, rebuild, obfuscation) |
| [docs/toolchain-setup.md](docs/toolchain-setup.md) | Cài JDK + decompiler + đóng gói |
| [docs/products-quan-va-ti-quay.md](docs/products-quan-va-ti-quay.md) | Game đầu tiên: findings + tasklist |

## Trạng thái

Khảo sát + kế hoạch xong. Bước kế: **spike** (cài toolchain → decompile → chứng minh build lại được) trước khi làm phần nặng.

## Nguyên tắc

- Từng game làm thẳng, chưa dựng framework (để dành làm sau dựa trên docs này).
- `products/<game>/original/` bất biến.
- Verify cuối cùng trên **Nokia thật**, không chỉ emulator.
