# Quyết định & Lý do

Các quyết định đã chốt qua thảo luận. Không đảo ngược nếu không có bằng chứng/bối cảnh mới.

## D1 — Mục tiêu: native landscape đứng thẳng (Kiểu B), KHÔNG xoay

- **Thiết bị đích (chốt)**: **Nokia E72 (RM-530)** — S60 3rd Ed FP2, màn **320×240 QVGA ngang cứng**, MIDP-2.0 / CLDC-1.1. Full-screen Canvas = 320×240.
- **Nguyên tắc reflow**: phần tử ở góc logic nào của bản dọc thì về **đúng góc đó** ở bản ngang (vd icon góc phải-trên dọc → góc phải-trên ngang). KHÔNG scale, KHÔNG rotate.
- **Mức độ (chốt)**: làm **như một game 320×240 native thật sự** — sửa cả **vùng chơi**, KHÔNG crop/letterbox tạm. Bao gồm relayout mọi màn tĩnh (title, dialog, menu) + vẽ lại art cho khung ngang. Đây là mức nỗ lực CAO NHẤT của Kiểu B (làm trọn task 3–6), chấp nhận để đạt chất lượng "không lỗi".
- **Bối cảnh**: thiết bị đích là Nokia màn **ngang cứng 320x240**.
- **Loại "Kiểu A" (rotate-blit, xoay cả khung 90°)**: tuy rẻ và generic, nhưng để xem đúng phải quay dọc điện thoại → điều khiển bất tiện. Không hợp máy ngang cứng.
- **Loại "Kiểu C" (thu nhỏ + viền hai bên)**: MIDP 2.0 không có scale sẵn → phải resample pixel mỗi frame, trên Nokia thật dễ lag; pixel-art co 0.75x bị rỗ/mờ; game nhỏ + viền đen. Chỉ hợp "chạy tạm".
- **Chốt**: đi **Kiểu B** — game đứng thẳng, bố cục lại cho khung ngang. Đây là bản nặng nhất (remap toạ độ + vẽ lại art), nhưng là bản duy nhất cho kết quả tốt trên máy đích.

## D5 — Chuẩn "tái tạo native", KHÔNG "dồn vào chỗ trống" (áp cho MỌI màn)

Quyết định lõi về chất lượng, client chốt.

- **Bác cách "reflow bằng dồn/xê dịch"**: KHÔNG giữ nội dung khung dọc rồi nhét phần thừa/HUD vào dải trống hai bên → tạo vết ghép, nhìn ra ngay là chắp vá. Client từ chối cách này.
- **Chuẩn đúng = tái tạo**: mỗi màn phải được **dựng lại thành một khung 320×240 native thực thụ**, liền mạch, như game vốn làm cho màn ngang. Khi thiếu chất liệu bề ngang thì **tạo mới** (vd: ghép/mở rộng art gốc rồi cắt về 320×240), không để trống hay độn.
- **Phạm vi = CẢ HAI**: áp cho **cả phần đang chơi** lẫn **các màn vẽ sẵn**.
- **Làm rõ bằng ví dụ client (3 cây + đống rơm)**: thế giới game vốn lớn hơn màn hình và cuộn được. Màn dọc 240-rộng chỉ lọt 3 cây; đống rơm đã nằm sẵn bên phải bản đồ. Native ngang = **khung nhìn rộng hơn cho thấy luôn 3 cây + đống rơm có sẵn**, KHÔNG vẽ thêm, KHÔNG chắp vá hai bên. Rộng hơn = thấy xa hơn hai bên; thấp hơn = thấy gần hơn trên-dưới.
- **Chi phí thực (đã hiệu chỉnh)**:
  - **Phần đang chơi (đi lại trong thế giới)** = chủ yếu **mở rộng khung nhìn + camera giữ nhân vật**, KHÔNG cày art mới (nội dung đã có trong bản đồ). Ca NHẸ. Cần xử lý clamp ở rìa bản đồ để không lòi khoảng trống.
  - **Vẽ/dựng art tay** thu hẹp còn: (1) màn tĩnh không có "thế giới phía sau" (title, cắt cảnh); (2) HUD/thanh chức năng đè lên (icon, tim, mặt nhân vật) phải xếp lại cho thanh ngang.
- **Quy tắc HUD (neo theo góc, ví dụ client)**: bố cục dưới đáy = mũi tên xanh (góc trái) · 5 bàn tay (chính giữa) · avatar + số tim/mạng (góc phải). Sang màn ngang **giữ nguyên neo**: trái→trái, giữa→giữa, phải→phải; thanh rộng 240→320 nên **khoảng cách giữa các cụm giãn ra** — đây là kết quả đúng, không phải độn/chắp vá. Icon chỉ **dời chỗ** (không vẽ lại); chỉ **dải nền HUD** (nếu có, vẽ cho 240) mới cần nới/vẽ tiếp cho đủ 320.
- Bản đồ tình huống của từng màn (nhẹ/nặng) chốt sau khi xem game chạy.

## D2 — Làm tay từng game trước, CHƯA dựng framework

- **Lý do**: lấy kinh nghiệm thực chiến trước; hiểu rõ phần chung/riêng trước khi trừu tượng hoá (tránh YAGNI).
- Framework dựng **sau**, dựa trên docs tích luỹ từ các product.

## D3 — Ranh giới tool hoá (định hướng framework tương lai)

Insight cốt lõi, quyết định framework có thể làm được tới đâu:

- **Tool hoá được (generic mọi game)**: phép biến đổi ở **ranh giới framework MIDP** — chặn `Canvas.paint(Graphics)`, `setFullScreenMode`, `keyPressed`, render vào offscreen rồi **xoay/scale**. Đây là lý do FreeJ2ME/J2ME-Loader có sẵn nút Rotate/Scale. → Framework tự động phần này được (~30–40% khung xương).
- **KHÔNG tool hoá được (riêng từng game, thủ công)**: **reflow UI đứng thẳng** — dời icon về góc màn ngang, vẽ lại art khung dọc, cân bằng lại việc mất chiều cao. Đây là **quyết định thiết kế + sáng tạo art**, không phải biến đổi cơ học. → ~60–70% việc của Kiểu B mãi là tay.
- **Nghịch lý**: thứ tool hoá được (xoay/scale) lại đúng thứ máy ngang cứng KHÔNG dùng được; thứ máy cần (reflow) lại đúng thứ không tự động được.
- **Kết luận cho framework**: chỉ nên là **scaffold bán tự động** — tự chèn wrapper Canvas + pipeline decompile/rebuild, chừa hook để mỗi game cắm bảng remap toạ độ + art mới. Không kỳ vọng "bấm nút ra ngay".

## D4 — Nút thắt rủi ro của Kiểu B (theo dõi sát)

1. **Rebuild code obfuscate** (build lại JAR chạy được từ class obfuscate) — nếu decompile→recompile vỡ quan hệ private/synthetic thì chuyển sang **vá bytecode**. Đây là rủi ro số 1, phải chứng minh sớm (spike).
2. **Mất 80px chiều cao** (320→240) — vấn đề thiết kế gameplay, không chỉ kỹ thuật.
3. **Vẽ lại art** — cần giải mã định dạng resource riêng + tay pixel-art.

## D6 — Ràng buộc kỹ thuật đã chốt qua review (product #1, có bằng chứng)

Sau khi decompile + parse resource thật (report `plans/reports/brainstorm-260718-2253-*`):

- **BUỘC vá bytecode** (giải quyết D4-1): recompile bất khả (CFR sinh 9 chỗ `GOTO`). Không dùng đường decompile→recompile. Preverify lại sau mỗi patch (ProGuard `-microedition`).
- **D4-2 và D4-3 nhẹ đi bất ngờ**: (2) không mất gameplay vì camera cuộn có sẵn + clamp + map đủ lớn; (3) màn tĩnh là PNG chuẩn trong pack → không cần encoder định dạng riêng.
- **Rủi ro thật chuyển trọng tâm sang**: (a) **relayout UI** (menu/dialog/HUD) vì toạ độ nằm trong SCRIPT data, phải hook điểm set-vị-trí; (b) **ring buffer map** (swap tile 11×15→15×11).

## D7 — Chốt 2 việc client (2026-07-18)

- **Input: KHÔNG remap phím (TẠM — chờ verify P2).** Client xác nhận E72 vào được mode số (bấm 2 lần phím góc trái-dưới) → mapping số gốc `0-9 * #` dùng được, d-pad lo di chuyển. Giữ nguyên mapping gốc, chơi ở mode số. Việc input co lại còn **verify trên E72 thật ở spike**: (1) d-pad còn ăn khi ở mode số; (2) có màn nào cần gõ chữ (đặt tên save) không. **Nhánh (red-team AD6)**: engine đọc keycode THÔ (`f.java:187-243`, không dùng getGameAction). Nếu E72 phát mã khác `-1..-4` hoặc d-pad chết ở mode số → **cho phép remap / thêm shim dịch phím**; "không remap" không phải ràng buộc cứng.
- **Art màn tĩnh: giao Codex gen.** Client có Codex (đánh giá gen ảnh tốt nhất hiện tại). Tới phase art: chuẩn bị spec (320×240 + asset gốc đã tách ở `products/quan-va-ti-quay/work/art-extract/` + ảnh gốc làm reference style) → Codex dựng → client duyệt. Lối tắt: tái dùng mảnh gốc (logo/nhân vật/khung tre), chỉ mở rộng nền — giữ đúng nét, không vẽ lại từ đầu.
