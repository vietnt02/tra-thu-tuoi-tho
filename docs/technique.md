# Kỹ thuật mod (tái dùng chung mọi game)

Kiến thức chung về cách mod J2ME sang màn hình đích. Bồi thêm sau mỗi product.

## Nền tảng J2ME cần nhớ

- Game = `.jar` (bytecode) + `.jad` (manifest). MIDlet chính khai báo ở `MIDlet-1` trong `META-INF/MANIFEST.MF`.
- Màn hình = subclass của `javax.microedition.lcdui.Canvas` (hoặc `GameCanvas`). Vẽ trong `paint(Graphics g)`.
- `setFullScreenMode(true)` → xin toàn màn hình; kích thước thật lấy qua `getWidth()/getHeight()`.
- Game **thích nghi màn hình** khi: đọc `getWidth/getHeight` động + override `sizeChanged(w,h)`. Game **khoá cứng** khi: nhét hằng số (vd 240/320) khắp nơi, không đọc size động → đây là ca cần mod.

## Cách phát hiện game khoá cứng size (không cần chạy)

Trên các file `.class` đã giải nén:
- `grep -rlaE 'getWidth|getHeight' *.class` → nếu **rỗng** ⇒ không đọc size động ⇒ hardcode.
- `grep -rla 'sizeChanged' *.class` → nếu **rỗng** ⇒ không thích nghi khi đổi size.
- `grep -rla 'setFullScreenMode' *.class` → tìm class Canvas chính.
- `grep -rlaE 'drawRGB|getRGB' *.class` → game blit pixel thủ công (ảnh hưởng cách xử lý xoay/scale).
- `strings -n 4 X.class | grep -iE 'Canvas|Graphics|drawImage|clip'` → đọc constant pool để biết class dùng API gì.

## Ba kiểu adapt (xem `decisions.md` để biết đã chọn kiểu nào)

### Kiểu A — Rotate-blit (generic, KHÔNG dùng cho project này)
Vẽ vào offscreen `Image` đúng size gốc → blit lên màn thật bằng `Graphics.drawRegion(img, 0,0,w,h, Sprite.TRANS_ROT90, dx,dy, anchor)`. MIDP 2.0 có sẵn blit xoay. Cả cảnh xoay đồng bộ, không lệch UI, nhưng phải xem ở tư thế xoay.

### Kiểu C — Scale + letterbox (generic, chất lượng kém)
MIDP 2.0 **không có scale sẵn**. Phải tự resample: `getRGB` buffer gốc → tính pixel đích → `drawRGB`. Tốn CPU mỗi frame; co non-integer làm rỗ pixel-art.

### Kiểu B — Reflow đứng thẳng (THỦ CÔNG, project này chọn)
Không có mẹo tổng quát. Phải:
1. **Remap toạ độ**: đổi hệ 240x320 → 320x240; dò & sửa mọi hằng số vị trí hardcode.
2. **Relayout UI/HUD**: dời title, khung, icon về góc màn ngang.
3. **Vẽ lại art**: asset vẽ cho khung dọc phải làm lại cho ngang.
4. **Xử lý mất chiều cao** (320→240): co vùng chơi / đổi camera / cân bằng gameplay.

## Rebuild pipeline (chỗ dễ vỡ nhất)

Hai hướng, thử hướng 1 trước:

1. **Decompile → sửa source → recompile**: dùng CFR ra `.java`, sửa, `javac` (target CLDC), **preverify**, đóng gói lại. Rủi ro: code obfuscate có quan hệ private/synthetic → recompile cả cụm dễ lỗi.
2. **Vá bytecode trực tiếp** (fallback chắc hơn): chỉ sửa đúng vài class cần đổi, giữ nguyên phần còn lại. Dùng thư viện thao tác bytecode (ASM) hoặc chèn class wrapper mới rồi đổi entry. Tránh phải recompile toàn bộ.

**Bắt buộc**: sau mọi thay đổi phải **preverify** lại (CLDC yêu cầu class đã preverify) rồi mới đóng gói `.jar`, cập nhật `.jad` (size, MIDlet).

## Xử lý obfuscation

- Tên class/biến 1 ký tự (`a`, `b`, `f`...) — không đổi tên; dựa vào chữ ký method + luồng gọi để định danh (class nào extends Canvas, method nào là `paint`).
- Lập "bản đồ" tên obfuscate → vai trò thật, lưu vào file product tương ứng.

## Verify

- Vòng nhanh: FreeJ2ME trên PC (đổi resolution, xem layout).
- Vòng thật: **Nokia 320x240 thật** — perf (`drawRGB` chậm), crash bộ nhớ, phím thật chỉ lộ ở đây.

## Checklist reflow Kiểu B — đúc kết từ Quan và Tí Quậy (2026-07-19)

Các NHÓM hằng số phải mổ khi đổi 240x320 → 320x240 (game engine tự chế, obfuscate). Dò thiếu 1 nhóm là ra đúng triệu chứng ghi kèm:

| Nhóm | Ví dụ trong game | Triệu chứng nếu bỏ sót |
|------|------------------|------------------------|
| Viewport clamp + vòng vẽ tile | `l-240-1`, vòng `a(Graphics,int)` | thế giới chỉ vẽ dải 240 |
| Ring buffer offscreen: **cả 2 nửa** — kích thước `createImage(264,360)` VÀ toán wrap (`%264/%360`, lưới tile `11x15`, offset blit `+264/+360`, ngưỡng reset `>=360/>=264`) | `g.class` | tile cắt dở, cột rác ở mép khi cuộn — buffer xoay mà wrap chưa xoay |
| Tâm camera — thường ≥3 site: snap khi vào map, tween cutscene, **follow mượt mỗi frame trong widget** | `-120/-160` ở f + b | nhân vật đứng lệch tâm (120,160) thay vì (160,120) |
| Painter màn tĩnh (fillRect nền + drawImage center + text center) | `f.g(int)` | màn dồn trái, dải thừa bên phải |
| Softkey label neo đáy | `(0,320)/(240,320)` | nhãn phím rơi ngoài màn |
| Fade/dim toàn màn | `a(g,0,0,240,320,...)` | fade chỉ phủ 240 |
| Wrap text (default + hard-code, **per-caller**) | `d.class` 2 site | text bẻ dòng sớm; chú ý 240 còn là mask màu 0xF0 — không swap mù |
| Hằng SCREEN_W/H của script VM (operand default) | `k.a(40,9)=240`, `(40,10)=320` | mọi layout script tính từ tâm màn bị lệch (menu, text vòng...) |
| Toạ độ tường minh trong script data (blob) | hộp thoại cuộn tre | chỉ sửa được bằng vá blob — phần "mổ từng cụm" |

Quy trình vá đã chạy thông: **byte-swap cùng độ dài trước** (an toàn StackMap) → **ASM đổi độ dài lệnh** (anchor chuỗi lệnh + assert số lượng khớp, scope theo method để né hằng trùng giá trị) → **ProGuard `-microedition`** sinh lại StackMap CLDC (class giữ v45) → repack + JAD. Art khổ ngang: scale x4/3 rồi ghép dải trên/dưới + blend trời (không méo hình); tranh nhỏ hơn màn chỉ cần recenter, không cần vẽ lại.
