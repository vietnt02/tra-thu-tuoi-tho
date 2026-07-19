# Toolchain Setup

Máy dev: macOS (darwin, ARM). **ĐÃ chạy được** (cập nhật 2026-07-19, Phase 1 xong).

## Trạng thái thực tế (đã dựng)

| Công cụ | Đường dẫn / nguồn | Vai trò |
|---------|-------------------|---------|
| JDK 26 | `/opt/homebrew/opt/openjdk@26/bin` (cài qua brew, **chưa link** vào PATH → gọi bằng đường dẫn tuyệt đối) | jar/javap/javac |
| CFR | `tools/cfr.jar` | decompile |
| ASM 9.7.1 | `tools/asm.jar`, `tools/asm-tree.jar` | dự phòng (hiện KHÔNG dùng — xem dưới) |
| MicroEmulator 2.0.4 | `tools/microemulator.jar` (Main-Class `org.microemu.app.Main`) | giả lập PC xem nhanh (double-click, load JAR). **Lưu ý**: lỏng hơn phone thật — test cuối vẫn ở E72 |

> **FreeJ2ME KHÔNG có sẵn** như plan tưởng; bản nightly chỉ nằm ở GitHub Actions artifact (cần token). Tạm dùng MicroEmulator; nghiệm thu thật = E72.

## Cách vá đã chốt: **sửa THẲNG byte tại chỗ** (KHÔNG preverify)

Các class trong JAR gốc **đã preverified sẵn** (có attribute `StackMap`, class version 45.3). Chốt:

- Vá bằng cách đổi **toán hạng hằng số, GIỮ NGUYÊN độ dài lệnh** (vd `sipush 160` = `11 00 A0` → `sipush 60` = `11 00 3C`). Vì không đổi độ dài → mọi offset nhánh + bảng StackMap còn nguyên hiệu lực → **không cần preverify lại, không cần thư viện CLDC/MIDP stub, không cần ProGuard**.
- Định vị site vá bằng 1 **anchor** (chuỗi byte bytecode duy nhất) + offset tương đối; assert byte cũ trước khi ghi (fail-loud, không vá mù). Xem `products/quan-va-ti-quay/work/patch-class.py`.
- ASM để **dự phòng** ca khó (thêm/bớt lệnh). Cảnh báo: ASM đọc `StackMapTable` chứ không hiểu `StackMap` CLDC → ghi lại sẽ **rớt StackMap** → khi đó buộc preverify. Nên ưu tiên byte-patch.

Cũ (giữ tham chiếu): Hiện **chưa có Java runtime** (`java`/`javap` chỉ là stub macOS). Cần cài trước khi decompile/build.

## Cần cài

| Công cụ | Vai trò | Ghi chú |
|---------|---------|---------|
| **JDK** (Temurin/OpenJDK 8 hoặc 11+) | chạy decompiler, `javac`, đóng gói | `brew install openjdk` |
| **CFR** | decompiler .class → .java | 1 jar đơn, chạy bằng java: `java -jar cfr.jar <input> --outputdir <dir>` |
| **preverify** (WTK / phoneME / bộ CLDC) | preverify class cho CLDC-1.0 | BẮT BUỘC nếu recompile; class không preverify sẽ bị từ chối |
| **ProGuard** (tuỳ chọn) | preverify/repackage tiện lợi | có bước preverify tích hợp |
| **ASM** (nếu đi hướng vá bytecode) | sửa bytecode không cần recompile | thư viện Java |
| **FreeJ2ME** | emulator test nhanh trên PC | đã có/đang dùng (ảnh chụp trước đó) |

## Gợi ý cài (khi user đồng ý)

```bash
brew install openjdk        # JDK
# CFR: tải cfr-<ver>.jar từ trang chính chủ, để vào project (vd tools/cfr.jar)
```

> User đã chọn "từ từ, đang trao đổi" cho việc cài — **chờ user đồng ý mới cài**.

## Đóng gói lại JAR/JAD — đã tự động hoá

Script `products/quan-va-ti-quay/work/make.sh` (Phase 1):

```bash
cd products/quan-va-ti-quay/work
./make.sh identity     # tháo f.class rồi nhét lại KHÔNG đổi -> chứng minh pipeline sạch
./make.sh spike 60     # vá Y splash 160->60 -> out/spike.jar + out/spike.jad (cài E72)
```

Cách làm bên trong: copy JAR gốc → update DUY NHẤT entry đã vá (`zip -X`) → các entry + manifest khác **byte-identical** bản gốc → sinh `.jad` từ manifest + size mới. Đã kiểm: identity round-trip cho f.class **byte y hệt**; spike chỉ **đổi đúng 1 byte** (đã `cmp -l` xác nhận).

Test: MicroEmulator (`java -jar tools/microemulator.jar`, load JAR) để xem nhanh → **rồi Nokia E72 thật** (nghiệm thu).

## Định dạng game hiện tại (tham chiếu)

`quan-va-ti-quay.jar`: MIDP-2.0, CLDC-1.0, MIDlet chính `CGTQ`. Chi tiết ở `products-quan-va-ti-quay.md`.
