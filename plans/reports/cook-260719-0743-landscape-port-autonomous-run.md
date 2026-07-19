# Cook autonomous run — landscape port Quan và Tí Quậy (E72)

Ngày: 2026-07-19. Chế độ: /goal tự chạy (client AFK). Video client: `~/Desktop/8057624911037.mp4`.

## TL;DR

- ✅ **Pipeline vá + đóng gói** (`work/make.sh`) chạy, identity round-trip byte-identical.
- ✅ **Giả lập render 320×240 offscreen** (`tools/emu-harness/`) — tái tạo ĐÚNG màn E72, **không cần điện thoại**. Đây là công cụ then chốt để làm tiếp port có vòng phản hồi.
- ✅ **Vá chạy được trên E72 THẬT** — video: màn CROCO đổi xanh→đỏ (spike-menu). Go/no-go = GO.
- ✅ **Reflow khung nhìn thế giới** 240×320→320×240 (`out/landscape.jar`, viewport g.class + ring buffer f.class) — build **ổn định**, boot tới gameplay không crash, chỉ đổi 14 byte trong 2 class.
- ⛔ **Chặn thật sự**: hoàn thiện VISUAL của port **phụ thuộc art mới** (mọi màn = artwork 240-rộng cố định: khung tre, logo, nền trời). Theo **quyết định D7**, art do Codex gen + **client duyệt**. Không thể tự quyết thay client.
- ⏸ **Hoãn kỹ thuật**: tâm camera `-120/-160`→`-160/-120` là sửa **đổi độ dài lệnh** (bipush→sipush) → cần đường **ASM + preverify lại** (chưa dựng). Thiếu bước này viewport vẫn 320 nhưng tâm lệch ~40px.

## Đã làm (verified)

1. **Toolchain**: JDK26 (`/opt/homebrew/opt/openjdk@26`), CFR, ASM, MicroEmulator 2.0.4. FreeJ2ME KHÔNG có sẵn → dùng MicroEmulator (đủ để render; E72 vẫn là nghiệm thu cuối).
2. **Cách vá chốt**: sửa THẲNG byte, giữ nguyên độ dài lệnh → StackMap còn nguyên → không cần preverify/CLDC stub. Định vị bằng anchor bytecode duy nhất + assert byte cũ (`patch-class.py`, `patch-landscape.py`).
3. **Render harness** (`tools/emu-harness/`): MicroEmulator headless + `J2SEDisplayGraphics` đã vá **clip drawRGB** (KVM thật clip; MicroEmulator gốc ném AIOOBE ở tile mép). Device 400×400, crop 320×240 = đúng view E72. Drive phím qua `DisplayAccess.keyPressed`. Chụp framebuffer thiết bị (game double-buffer, cờ dirty `f` chặn paint chủ động).
4. **Chẩn đoán lỗi trong video** (bằng ảnh render + video): E72 cấp canvas 320×240 ngang; game vẽ nội dung **rộng 240 ghim mép TRÁI** → **sọc phải ~80px bỏ phí** + phần cao 320 **cắt cụt đáy** dưới 240. Đúng bài "Kiểu B" reflow.
5. **Bản đồ input** (`f.a(int)`): UP=-1 DOWN=-2 LEFT=-3 RIGHT=-4 FIRE=-5 softkey -6/-7; số '0'..'9'=48..57; '*'=42 '#'=35. → đóng open-question D7 phần đo phím (d-pad âm hoạt động).
6. **Viewport reflow** (`landscape.jar`): g.class swap mọi `sipush 240↔320` (3+3 site = clamp `a()` + vòng vẽ tile `a(Graphics,int)`); f.class ring buffer `createImage(264,360)→(360,264)`. Đổi 14 byte, class vẫn v45.3, 26 entry khác byte-identical. Boot→menu→story→"Vòng 01"→gameplay: **không crash**.

## Vì sao chưa "xong hình" — bằng chứng

Màn gameplay "vườn nhà quan" render qua harness: khung **tre 240-rộng cố định** bao quanh 1 dải thế giới; sọc phải vẫn phí. Widen viewport (code) KHÔNG hiện ra vì **khung art che**. Muốn màn này (và menu/dialog/splash — đều art) full 320 → **phải vẽ lại art rộng ra**. Art = D7 = client duyệt. Đây là ranh giới thật, không phải thiếu sót kỹ thuật.

Ảnh baseline 320×240: `products/quan-va-ti-quay/work/captures/` + shots trong quá trình.

## Sản phẩm để client check

- `products/quan-va-ti-quay/work/out/landscape.jar` (+ `.jad`) — viewport reflow, ổn định. Cài E72 để xác nhận không regression (menu/chơi vẫn chạy). *Chưa* đổi hình rõ rệt (art chưa làm).
- `tools/emu-harness/render.sh` — xem bất kỳ jar nào ở 320×240 trên PC, không cần E72.

## Việc còn lại (cần client)

1. **Duyệt hướng art (D7)** cho các màn: menu, splash publisher, dialog, khung gameplay, HUD. Đây là chặn chính. Sau khi có art 320×240, mỗi màn: thay blob PNG trong pack + chỉnh anchor (đa số là byte-swap cùng độ dài) → verify từng màn qua harness.
2. **Camera precision**: dựng đường ASM+preverify để đổi `-120/-160→-160/-120` (2 site f.java 722-723, 1677-1678) cho tâm khung nhìn đúng.
3. Nghiệm thu tuần tự trên E72 thật (perf/crash ẩn).

## Art pipeline — THÔNG (2026-07-19, sau khi client "làm nốt đi")

- **Giải mã resource pack** (`f.a(n)`): JAR entry `"n>>8"`, format `[u32 bỏ][u8 count][u32 LE end×count][blobs]`, trả blob `n&0xFF`. Map: `f.a(768)`=pack'3'/blob0=menu scene; `f.a(1024)`=pack'4'/blob0=khung gameplay; pack'4'/blob1-4=nút menu 204×74.
- **Tool thay tranh** `patch-pack.py`: thay 1 blob PNG, tính lại ends, ghi lại JAR entry (các entry khác giữ nguyên).
- **PROVEN end-to-end** (harness): thay pack'4'/blob0 = ảnh 320×240 → khung gameplay **phủ kín màn ngang**, nội dung chơi vẽ đè lên trên. Khung gameplay drawImage tại `(0,0)` anchor 0 → **không cần vá draw**. (Màn vẽ centered `(120,160)` thì swap `(120,160)→(160,120)` = đảo 2 lệnh push, cùng độ dài — StackMap an toàn.)
- **Tranh khổ ngang** `recompose-art.py`: GIỮ art gốc, nới ngang 240→320 bằng nhân bản cột mép (nền/rào tre kéo dài tự nhiên, không đụng logo/nhân vật), fit cao 320→240 co từng vùng. Khung gameplay đã dựng: `art-landscape/pack4-blob0-gameplay-320x240.png`.
- **Final build** `out/landscape.jar` = viewport reflow + khung gameplay khổ ngang, render OK trên harness, boot→gameplay không crash. `make.sh landscape` tái lập được.

### Còn lại (cùng công thức, cần client duyệt hướng art D7)
- menu scene (pack'3'/blob0), splash publisher, hộp thoại, thanh HUD → mỗi màn: `recompose-art.py` dựng 320×240 + `patch-pack.py` ghép + (nếu vẽ centered) swap draw + verify harness.
- Căn nội dung chơi động cho khít khung mới (world render vẫn ~240 rộng ở màn này) + tâm camera (ASM).

## Deep-patch toolchain — THÔNG (2026-07-19)

Blocker lớn nhất để "làm cho ra" (căn giữa/nới content = đổi toạ độ 120->160 = **đổi độ dài lệnh**) là preverify lại CLDC. Đã giải:
- **`tools/preverify.sh`** (+ `proguard.jar`): ASM sửa (chèn/đổi lệnh) -> ProGuard `-microedition` sinh lại StackMap CLDC, class **giữ v45**, E72 KVM chấp nhận.
- Thư viện CLDC/MIDP = `microemulator.jar` (API javax.microedition) + **java.base của openjdk@21** (ProGuard 7.6.1 đọc class <= Java23; JDK26 quá mới). Đã cài openjdk@21.
- Kiểm: preverify JAR gốc -> chạy được trong harness (StackMap 46->18 frame, hợp lệ). **=> Giờ đổi được BẤT KỲ toạ độ nào mà không hỏng E72.**

### Vẫn còn (dùng toolchain trên, là phần multi-session):
- **Play content pinned-left**: dải world + nhân vật + HUD do engine vẽ bám trái (thiết kế 240). Căn giữa/nới cho khít 320 phải dịch nhiều cụm toạ độ (một phần trong script data) — nay LÀM ĐƯỢC nhờ preverify, nhưng là việc từng cụm, nhiều buổi.
- Menu/splash/dialog: mỗi màn ghép nhiều mảnh khác nhau, mổ riêng.
- Ước lượng plan gốc: **2-4 tuần**. Đúng với thực tế gặp phải.

## Unresolved

- Chưa reach được màn cuộn thế giới "thuần" (không khung) qua input script để đo trực tiếp mức lợi của viewport widen — có thể tồn tại ở đoạn đi lại; cần verify khi làm art.
- Ring buffer `360×264`: chọn theo swap an toàn (≥320×≥240); nếu sau này thấy dư/thiếu margin khi cuộn nhanh, tinh chỉnh.
