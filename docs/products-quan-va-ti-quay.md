# Product #1 — Quan và Tí Quậy

JAR gốc: `products/quan-va-ti-quay/original/quan-va-ti-quay.jar` (333 KB).

**Thể loại**: RPG / nhập vai giải đố (KHÔNG phải game cờ). => vùng chơi nhiều khả năng là **map + camera cuộn theo nhân vật** — trường hợp thuận cho reflow (đổi khung nhìn, không phải vẽ lại bàn tĩnh). Cần verify ở `f.paint()`.

## Manifest

- `MIDlet-Name`: Quan và Tí Quậy · `MIDlet-Vendor`: gamevn24h.net
- `MIDlet-1`: `Quan va Ti Quay, /icon.png, CGTQ` → **MIDlet chính = `CGTQ`**
- `MicroEdition-Configuration`: CLDC-1.0 · `MicroEdition-Profile`: MIDP-2.0

## Nội dung JAR

- Class (obfuscate 1 ký tự): `a b c d e f g h i j k l m` + `CGTQ`.
- Resource: `0`–`11` = **archive nhị phân tự đóng gói** (không phải PNG; header `0` = bảng offset kiểu `f502 0100 093b...`). Chứa sprite/map, có thể kèm kích thước — **cần giải mã định dạng riêng để sửa/vẽ lại art**.
- `icon.png` (409 B).

## Findings bytecode (đã VERIFY, chưa decompile)

Phân tích qua constant pool (`strings`) + grep, KHÔNG cần chạy Java:

| Phát hiện | Bằng chứng | Ý nghĩa |
|-----------|-----------|---------|
| **Không đọc size động** | `grep getWidth\|getHeight *.class` → **rỗng** | 240/320 hardcode khắp nơi |
| **Không thích nghi resize** | `grep sizeChanged` → **rỗng** | đổi resolution vô ích (khớp ảnh FreeJ2ME: game vẽ cột 240 trái, chừa trắng phải) |
| **Canvas chính = `f.class`** | chứa `setFullScreenMode`, `getClipWidth/Height`, `setClip`, `drawImage`, extends `javax/microedition/lcdui/Canvas` | class to nhất (27 KB) — engine render |
| **Blit pixel thủ công** | `drawRGB`/`getRGB` ở `d.class`, `f.class` | xử lý scale/xoay tốn CPU; art nằm trong mảng RGB |
| **Entry** | `CGTQ.class` dùng `Display.getDisplay`, `setCurrent` | MIDlet khởi tạo, set Canvas |

Kết luận: game **khoá cứng portrait 240x320**, ứng viên điển hình cần Kiểu B. Điểm can thiệp chính: **`f.class`** (`paint` + `setFullScreenMode`).

## Tasklist ước tính (Kiểu B)

Con số thô, chốt lại sau khi decompile `paint()`.

| # | Việc | Giờ | Rủi ro |
|---|------|-----|--------|
| 0 | Cài toolchain (JDK, CFR, preverify, packaging) | 1–2 | Thấp |
| 1 | Decompile + đọc hiểu `CGTQ`, `f.class`, `b/d`; map tên obfuscate→vai trò | 4–8 | Trung |
| 2 | **Dựng pipeline rebuild**: sửa 1 dòng vô hại → build lại → chạy OK trong FreeJ2ME | 3–8 | **CAO** |
| 3 | Remap toạ độ 240x320 → 320x240 | 8–20 | **CAO** |
| 4 | Relayout UI/HUD (title, khung tre, thanh icon) về góc ngang | 6–12 | Trung |
| 5 | Giải mã resource `0`–`11` + vẽ lại art cho khung ngang | 6–15 | Trung |
| 6 | Xử lý mất 80px chiều cao (co vùng chơi / camera / cân bằng) | 4–12 | **CAO** |
| 7 | Map lại input menu (điều khiển chơi giữ nguyên) | 1–3 | Thấp |
| 8 | Test FreeJ2ME + **Nokia thật**, sửa lỗi | 6–15 | Trung |
| 9 | Đóng gói `.jar` + `.jad` | 1–2 | Thấp |

**Tổng ~40–95 giờ** (~1.5–3 tuần nghiêm túc). Nếu bước 2 sập phải vá bytecode toàn bộ → đội thêm.

## Việc kế tiếp (NEXT)

Chạy **spike = task 0→1→2** (~8–18 giờ) như bước thăm dò:
- Cần user đồng ý **cài JDK** trước.
- Nếu **build lại được** + `paint()` gọn ⇒ khả thi, ước tính lại chính xác phần còn lại.
- Nếu **build lại sập** / code rối ⇒ dừng sớm, chưa tốn công art/relayout.

## Bản đồ tên obfuscate (VERIFY qua CFR decompile — `work/decompiled/`)

| Obfuscate | Vai trò | Ghi chú |
|-----------|---------|---------|
| `CGTQ` | MIDlet entry | chỉ set Canvas |
| `f` | god-class: Canvas + game loop + `paint` + input + audio + save + camera | 63KB; toạ độ nằm đây |
| `g` | tầng vẽ map/tile + clamp camera + viewport clip | `g.java:191-225` |
| `b` | blit sprite mức thấp (byte[]+palette→pixel) + camera bám mục tiêu | |
| `d` | cache Image + giải mã pack art (palette RGB555/4444/8888 + RLE + alpha) | `d.java:51-215` |
| `k` | script interpreter (opcode điều khiển entity/UI) | opcode40 default 240×320 |

## Findings VERIFY sau decompile (2026-07-18)

- **Camera có thật, world cuộn**: offset = tâm entity − nửa-màn (120,160); vẽ world qua viewport 240×320 + scroll. → reflow = swap sang (320,240)/(160,120). Xem `docs/technique.md`.
- **Recompile ĐỨT**: CFR sinh 9 chỗ `GOTO` không hợp lệ Java → **buộc vá bytecode** (ASM/Krakatau), không decompile→recompile.
- **Hằng số hình học tập trung ~8 cụm** (không rải 56 chỗ), có chỗ chồng nghĩa — remap theo ý nghĩa. Danh sách site: report `plans/reports/brainstorm-260718-2253-*`.
- **Resource `0–11` = pack lồng** `[u32 bỏ][u8 count][u32 end-offset×count][blobs]`; parse khớp 100% byte. **Màn tĩnh = PNG chuẩn trong pack** (title/menu-bg 240×320) → thay blob = xong, KHÔNG cần encoder riêng.
- **5 map**, nhỏ nhất 480×336px → viewport 320×240 không lộ mép (clamp có sẵn).
- **UI layout nằm trong SCRIPT data**, không trong hằng Java → relayout menu/dialog/HUD phải **hook điểm set-vị-trí**, không remap hằng.
- **Ring buffer map** (`createImage(264,360)`, tile 11×15→15×11) = cụm khó nhất của phần chơi.
- Game có audio (`media.Player`) + save (`RecordStore`) + vibrate → giữ nguyên khi port.

## Rủi ro mới (từ review)

- **E72 QWERTY không có phím số** — engine map hành động vào phím số/`*`/`#`. Di chuyển d-pad OK, hành động cần remap. Cần đo keycode thật + chốt scheme với client.
- **Toolchain**: chưa có preverify → ProGuard `-microedition`; ASM drop StackMap → re-preverify sau mỗi patch.

## Phase 1-2 findings (2026-07-19)

- **BỎ preverify cho đường chính**: class đã preverified sẵn (`StackMap`, v45.3). Vá kiểu **đổi toán hạng hằng, giữ nguyên độ dài lệnh** → StackMap + offset còn nguyên → không cần ProGuard/CLDC stub. Giảm rủi ro toolchain #FM1 xuống ~0 cho các patch dạng này.
- **Pipeline** `work/make.sh` + `work/patch-class.py`; định vị site vá bằng anchor bytecode duy nhất + assert byte cũ. Kiểm: identity round-trip **byte-identical**; spike **đổi đúng 1 byte** (`cmp -l`).
- **Spike splash** (`work/out/spike.jar`): splash `n==0` — `drawImage(img,120,160,3)` → Y `sipush 160`→`60`. Anchor `1A 9A 00 2B 07 B8 00 C3 4C B2 00 55 12 0A`, rel=37. **Quá tinh vi** (màn chớp nhanh) → client không thấy khác.
- **BASELINE E72 (ảnh client, 2026-07-19)** — xác nhận cơ chế hiển thị: E72 cấp canvas **320×240 ngang**; game vẽ nội dung **rộng 240 ghim mép TRÁI** (hằng 240 cứng). Hệ quả: **sọc phải ~80px BỎ PHÍ** (thấy rõ sọc trắng bên phải ở màn hộp thoại + màn CROCO GAMEZ), phần cao 320 bị **cắt cụt đáy** dưới 240. KHÔNG scale, KHÔNG xoay → đúng bài reflow của plan (Kiểu B). Ngoại lệ: splash nhà mạng Viettel vẽ full-screen (ảnh generic, không qua engine hằng 240).
- **Spike menu** (`work/out/spike-menu.jar`): proof KHÔNG THỂ NHẦM. Nền menu `n==4`: (1) màu pool int `6801708→0xFF0000` (đỏ) — pool #9 dùng chung với `n==1` nên màn đó cũng đỏ; (2) `fillRect` width `sipush 240→320` (anchor `A0 00 3E B2 00 55 12 09 B6 01 1E B2 00 55 03 03`, rel=16) → nền đỏ **tràn hết bề ngang**, sọc trắng phải biến mất. Kiểm: đổi đúng 5 byte, `cmp -l` xác nhận.
- **E72 GO** (video 2026-07-19): cài spike-menu → màn CROCO đổi xanh→ĐỎ trên máy thật ⇒ vá bytecode chạy đúng trên E72. Chốt go/no-go.

## Autonomous run 2026-07-19 (Phase 3 + harness)

- **Render harness 320×240** (`tools/emu-harness/`, `render.sh`): MicroEmulator headless + `J2SEDisplayGraphics` vá clip drawRGB (KVM thật clip; MicroEmulator gốc ném AIOOBE ở tile mép). Device 400×400 → crop 320×240 = view E72. Drive phím qua `DisplayAccess.keyPressed`, chụp framebuffer thiết bị (game double-buffer, cờ dirty `f.f` chặn paint chủ động). **Xem bất kỳ jar ở 320×240 không cần điện thoại.**
- **Viewport reflow** (`work/out/landscape.jar`): `g.class` swap mọi `sipush 240↔320` (đúng 3+3 site: clamp `a()` off 4/17 + vòng vẽ tile `a(Graphics,int)` off 95/100/115/147, mọi 240=viewport-width→320, 320=height→240); `f.class` ring buffer `createImage(264,360)→(360,264)`. 14 byte, v45.3, boot→gameplay không crash. Xem `patch-landscape.py`.
- **RANH GIỚI ART (D7)**: mọi màn = artwork 240-rộng cố định (khung tre gameplay, logo menu, nền trời, hộp thoại). Viewport widen (code) KHÔNG hiện ra vì khung art che → hoàn thiện visual **cần art 320×240 mới, client duyệt**. Đây là chặn chính, không phải thiếu sót code.
- **HOÃN**: camera center `-120/-160→-160/-120` (f.java 722-723, 1677-1678) là **bipush→sipush = đổi độ dài lệnh** → cần ASM+preverify; chưa dựng. Thiếu → tâm khung lệch ~40px (không crash).
