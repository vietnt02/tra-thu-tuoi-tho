# Fix batch — trace nguyên nhân bản landscape "chưa ưng ý" + giải quyết

Ngày: 2026-07-19. Tiếp nối `cook-260719-0743-landscape-port-autonomous-run.md`.

## TL;DR

Bản trước chỉ swap được hằng CÙNG độ dài lệnh → mọi toạ độ 120↔160 (đổi độ dài bipush↔sipush) đều chưa vá. Phiên này dựng đường **ASM + ProGuard preverify** vào pipeline chính, vá **71 site** trong f/g/b/d/k, verify từng màn qua harness. Kết quả: splash/title/menu/story/màn Vòng/gameplay đều phủ kín 320×240, nhân vật giữa màn, hết seam tile. Còn lại: hộp thoại cuộn tre (toạ độ trong script data) + tinh chỉnh art menu.

## Nguyên nhân gốc (trace được, có bằng chứng)

1. **Camera lệch tâm**: 3 site `-120/-160`, KHÔNG phải 2 như báo cáo trước — site thứ 3 nằm ở `b.java:212-213` (widget camera-follow mượt mỗi frame, case 5) → đây là site quyết định vị trí nhân vật khi đi lại. Cả 3 đã đổi `-160/-120`.
2. **Painter màn tĩnh `f.g(int)`**: fillRect nền `240×320` + drawImage/text center `(120,160)` → màn dồn trái, dải thừa phải. Đã đổi cả 6 cặp rect + 5 cặp center + site lẻ (logo 120→160, text 210→170, dialog text x, plaque vòng `i(int)` 120→160).
3. **Ring buffer xoay nửa vời** (lỗi lộ khi test di chuyển): `createImage` đã 360×264 nhưng toán wrap trong `g.class` vẫn khổ dọc — `%264/%360`, lưới tile `11×15` (24px/tile), offset blit `+264/+360`, ngưỡng reset `>=360/>=264` → cây cắt dở + cột rác mép khi cuộn. Đã đảo đủ 4 nhóm (24 site), test 4 hướng sạch seam.
4. **Softkey neo `y=320`** → rơi ngoài màn 240. Đổi `(0,240)/(320,240)`.
5. **Fade toàn màn `(0,0,240,320)`** → chỉ phủ 240. Đã đảo.
6. **Hằng SCREEN_W/H của script VM** `k.a(40,9)=240 / (40,10)=320` → layout script (menu list, text màn Vòng) tính tâm sai. Flip 240↔320 → menu/nút/text tự căn giữa. (Đúng dự đoán red-team #12: chỉ flip sau khi audit consumer bằng harness.)
7. **Wrap text** `d.class` 2 site 240→320 (né site `sipush 240` = mask màu `0xF0` trong cùng class — bằng chứng vì sao không swap mù).
8. **Anchor hộp thoại `f.d(int)`** `(120,160)→(160,120)` — đã vá nhưng hộp thoại âm thanh KHÔNG đi qua đường này → toạ độ nó nằm tường minh trong script data (xem "Còn lại").

## Pipeline mới (tái lập: `./make.sh landscape`)

byte-swap cùng độ dài (`patch-landscape.py`) → **`LandscapePatch.java`** (ASM, anchor chuỗi lệnh + assert count, scope theo method) → repack → **`tools/preverify.sh`** (ProGuard `-microedition`, StackMap CLDC mới, class giữ v45.3) → ghép art (`patch-pack.py`) → JAD. Log build in đủ 71 site `[OK]`.

## Art

- `pack3-blob0-menu-320x240.png` (mới): scale nguyên bản ×4/3 → ghép dải banner trên + dải nhân vật dưới, blend trời 24px. Dùng cho CẢ title lẫn menu. v1 đạt; **đầu nhân vật bị cắt nhẹ ở seam** — chờ client duyệt hướng chỉnh (D7).
- Khung gameplay (pack4-blob0) giữ nguyên bản phiên trước.

## Verify (MicroEmulator harness, crop 320×240 đúng E72)

- Boot→splash CROCO (giữa màn, nền phủ kín) → sound dialog → story (đen phủ kín, wrap 320) → title → menu (nút giữa màn) → màn "Vòng 01" (text giữa) → khung gameplay → thế giới mở.
- Di chuyển 4 hướng nhiều bước: không seam, không cột rác, nhân vật ~(160,120±10) khi xa mép map, clamp mép hoạt động.

## Còn lại (theo thứ tự nên làm)

1. **Hộp thoại cuộn tre** (âm thanh đầu game + thoại trong game): mảnh giấy/thanh tre + text đều lấy toạ độ tường minh từ **script data blob** — phải giải mã format script rồi vá blob (per-dialog). Đây là phần "mổ từng cụm" đã dự báo trong plan.
2. Tinh chỉnh art menu theo ý client (D7).
3. Màn `g(4)` (text y=170) chưa thấy ở đường chơi thường — kiểm khi gặp.
4. Widget tween `b.b(48,370)` + `a(1,3,320,500,...)` (chuyển cảnh trượt dọc?) — audit bằng mắt khi chơi sâu.
5. **Nghiệm thu E72 thật**: bản này là bản ĐẦU TIÊN đi qua ProGuard toàn JAR (bắt buộc để đổi độ dài lệnh) — harness OK nhưng cần cài máy thật sớm để chốt KVM chấp nhận + perf.

## Unresolved

- Script format (k.class parse) chưa giải mã — cần cho mục 1.
- Vị trí dải chơi trong khung "vườn nhà quan" hơi lệch (x bắt đầu ~40) — nghi toạ độ script, gộp vào mục 1.

## Bổ sung cùng ngày — vá toạ độ trong SCRIPT DATA (từ screenshot client)

Đã giải mã script VM: bảng opcode ở `entry0/blob0`, script chính `res1` (`entry0/blob1`, 731 lệnh, nhãn màn = op0), lệnh = `[u16 op][u16 flags][u8 present][operand LE theo bảng size]`; opcode đặt widget: 40(rect) 41(map) 42(list) 43(fill) 44(sprite) 45(text) 46(cam-clamp). Tool mới: `work/script-tool.py` (dis/scan/patch) + bảng vá `work/script-patches.tsv` (~66 edit), nối vào cuối `make.sh landscape`.

Đã sửa & verify harness: hộp thoại cuộn tre căn giữa phủ ngang (thấy đủ 2 thanh tre), story text vào giữa, menu list hạ xuống 170 hết đè banner, popup truyện tranh dời +40/tâm 120 (chưa reach được để verify), thoại trong game (giấy 160,180 / chữ 55,180 — chưa reach), **HUD gameplay hiện lại** (2 widget neo y=320→240; bàn tay x155 giữa, avatar x320 mép phải).

Client tự test được trên Mac: `tools/emu-harness/play.sh` (MicroEmulator 320×240, RMS lưu file).

### Unresolved (chờ client chơi sâu để verify)
- Thoại NPC trong game + popup truyện tranh "bị phát hiện": đã vá theo suy luận (@976/@989... y=180; sec6 +40), chưa thấy trực tiếp.
- Text `@1499 (120,285)`, `@5282 (120,240)`, sprite `@2367 (120,210)` — chưa rõ màn, để nguyên.
- Level scripts entry 6/8 có thể còn widget UI riêng theo màn.

## Màn "Vòng/chọn rương" — kết luận sau khi đối chiếu bản gốc

Nhân vật đứng mép trái + hàng rương bên phải LÀ thiết kế gốc (render `original/` xác nhận: nhân vật là hình dẫn cạnh hàng rương, không phải con trỏ chạy trên rương). Bản landscape đã khớp bố cục gốc. Delta duy nhất: khung landscape rộng 320 nên cụm (nhân vật+rương+chấm) nằm ở 240 trái, thừa ~80px phải.

Đã thử căn giữa cụm (+40): thất bại vì **2 hệ toạ độ tách rời** — tranh rương do bộ vẽ bản đồ (camera g.h/i), nhân vật+con trỏ do hệ widget (b.b[] - b.c/d). Dịch lệ hai lượng → lệch nhau. Muốn căn giữa phải ép CẢ camera bản đồ LẪN scroll widget = -40 chỉ cho riêng màn này (cần hook per-screen — chưa làm). Đã revert các thử nghiệm, giữ layout khớp-gốc. CHỈ giữ: text "Vòng 01..." đưa lên vùng tranh vườn (gốc nằm dưới khung, landscape hết chỗ).

Text-moved sites: `1 2451 1 160` / `1 2451 2 155`.

## Sửa lại màn "Vòng/chọn màn" (chiều 16:xx) — con trỏ nhân vật

Client báo nhân vật = CON TRỎ chọn màn (nhảy giữa các chấm), không phải trang trí. Trước đó tôi dời container hàng chấm +40 (120->160) để "căn giữa" → nhưng đo lại: ở container=120, cụm rương/chấm span x44-275, **tâm 159 = đã giữa sẵn** (list tự căn theo SCREEN_W=320 đã flip). +40 mới là cái đẩy hàng chấm lệch phải, và con trỏ (hệ toạ độ riêng) không theo → nhảy trượt chấm.

FIX: revert container về 120 (`1 2367 1 120`). Con trỏ + hàng chấm lại chung gốc như bản gốc → con trỏ nhảy đúng chấm, cụm vẫn ở giữa. Giữ text "Vòng 01" dời lên (`1 2451 1 160 / 2 155`). Bài học: đừng "căn giữa" bằng cách dời literal khi list đã tự căn theo SCREEN_W — chỉ làm lệch.

## Ô item HUD (màn 2+) — case 72

Client báo màn 2 thiếu ô item ở đáy HUD. Nguồn: f.java case 72 (nhặt item) tạo widget 518 (khung ô) + 519 (icon) neo đáy màn DỌC — start y370, đích y320/y315 → rơi ngoài màn 240. Vá qua LandscapePatch (anchor sipush 370 duy nhất): đích 320→240, start 370→290, icon 315→235. Áp sạch (assert x1 mỗi rule). Chưa verify harness (cần trạng thái có item — nhờ client kiểm màn 2).

## Rà soát tổng (client yêu cầu — tránh phải phá đảo test từng màn)

Quét toàn bộ 2 nguồn toạ độ:
- **Code (f/b/d/g/k)**: mọi `sipush 320` còn lại sau vá đều là CHIỀU RỘNG (đúng, màn giờ 320 rộng); `b(480,640)` = vị trí "cất widget ra ngoài màn" (không phải hiển thị). Ô item (case 72) đã vá. => code sạch.
- **Script (mọi resource 1,1541-1544,2055-2058)**: quét op42/op45 (menu/text = luôn UI) + op40/43/44 (sprite/rect) trong section UI (loại section world/cutscene). Kết quả: chỉ còn 2 text neo đáy chưa vá — sec1 story text (@1499 y285) + sec37 màn ảnh+text (@5282 y240). Đã vá x120→160, kéo y lên. Level scripts (màn chơi) KHÔNG có widget UI text/menu riêng.

=> Đã xử lý chủ động tất cả UI neo-đáy/lệch-tâm mà không cần chơi hết game. Còn phần WORLD/cutscene (vật thể trong cảnh, camera) giữ nguyên là đúng.

## Màn chọn màn — CĂN GIỮA cả cụm (con trỏ + rương) — GIẢI QUYẾT TRIỆT ĐỂ

Client làm rõ: con trỏ khớp nút, nhưng CẢ CỤM (con trỏ + 4 rương + chấm) lệch trái, cần ra giữa. Trước đó chỉ dịch được chấm (container widget 0) → con trỏ+rương không theo.

Tìm ra nguồn chung: **res1030** = 4 nút `(62,210)(101,210)(141,210)(179,210)` (tâm x=120, thiết kế 240). f.java case 2 đặt CẢ rương (a[0][1..4]) LẪN con trỏ (b=a[0][6]) tại `a[n<<1]` = res1030. Dịch res1030 x +40 → rương + con trỏ cùng ra giữa (tâm 160). Cộng container widget 0 (chấm đỏ) → 160. => cả cụm căn giữa, con trỏ vẫn đúng nút.

Tool mới: `script-tool.py shiftx <jar> <res> <dx>` (cộng dx vào X của blob [u16 count][i16...]). make.sh: `shiftx out 1030 40`. Bài học: khi 2 phần tử "lệch nhau khi dịch", tìm MẢNG DỮ LIỆU CHUNG cả hai cùng đọc thay vì dịch từng widget.
