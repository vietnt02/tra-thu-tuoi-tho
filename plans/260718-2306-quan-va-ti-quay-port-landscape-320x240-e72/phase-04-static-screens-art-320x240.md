---
phase: 4
title: "Màn tĩnh art 320×240 (Codex)"
status: pending
priority: P1
effort: "dev ~1-2d + art/duyệt (calendar, client-gated)"
dependencies: [2, 3]   # art-gen bắt đầu sau P2; TÍCH HỢP phải sau P3 (buffer nới)
---

# Phase 4: Màn tĩnh art 320×240 (Codex gen)

## Overview
Dựng bản ngang 320×240 cho các màn tĩnh (title, nền menu, panel) — tái tạo native, tái dùng mảnh gốc + mở rộng nền. Art giao **Codex gen** (D7), client duyệt mockup. Thay blob PNG trong pack + patch toạ độ vẽ. **Ordering (Codex)**: *vẽ art + repack script* chạy SONG SONG với P3; nhưng *tích hợp* (ráp PNG 320-rộng vào game) phải **SAU P3** — vì màn tĩnh vẽ vào buffer chung `264×360` (`f.java:868`), chưa nới buffer thì ảnh 320 bị cắt.

## Requirements
- Functional: title + nền menu + panel hiện đúng khung 320×240, liền mạch, không méo/chắp vá; client đã duyệt.
- Non-functional: giữ nét vẽ gốc (không lệch phong cách); pack repack lossless, game vẫn load được art.

## Architecture
- **Nguồn**: art gốc đã tách ở `products/quan-va-ti-quay/work/art-extract/` — title `res3_png0_240x320`, nền menu `res4_png0_240x320`, panel menu `res4_png1..4_204x74`, logo/panel khác (`res0,res2,res10`).
- **Cách dựng**: reuse mảnh gốc (logo, 2 nhân vật, khung tre) + Codex gen phần mở rộng nền cho đủ 320 ngang. Spec giao Codex: kích thước 320×240, ảnh gốc làm reference style, danh sách mảnh cần giữ.
- **Thay vào game**: PNG chuẩn nằm trong pack `[u32 bỏ][u8 count][u32 end-offset×count][blobs]` (verified). Viết script repack lossless (~15 dòng Python) thay blob mục tiêu, giữ blob khác nguyên.
- **Patch toạ độ vẽ — PER-SITE, KHÔNG pair-swap (AD3/FM4)**: màn tĩnh là **ảnh GHÉP nhiều lớp** ở các y khác nhau (tuned cho 320 cao), không phải 1 PNG center. Dùng **bảng (màn, phần tử) → (x,y,anchor)** tính lại cho 320×240. x-center `120→160` cơ học; **mỗi y là giá trị landscape riêng**. Chú ý anchor: `f.java:2121` `drawImage(image,120,125,17)` anchor 17=HCENTER|TOP → `125` là offset đỉnh, KHÔNG phải nửa-cao.
- **Kiểm kê màn tĩnh ĐẦY ĐỦ (Codex #3)**: full-PNG `n==0,2,9,10`; **thêm `n==1`** (fill + 1 ảnh center, `f.java:2045`); màn ghép `n==4,6,8` (`f.java:2059,2060,2080,2087,2088`); **thêm `n==5`** (nền pack-4 + art chọn, `f.java:2062` — thuộc scope menu). Không sót nhánh nào.
- Site: `f.g()` `f.java:2039-2095` (gồm các `fillRect(0,0,240,320)` → `320,240`; **P3/P4 cùng đụng vùng này → dùng bảng patch gộp/merge order**), `f.i` `f.java:2119-2121`, `f.d()` `f.java:2147-2148`.

## Related Code Files
- Create: `products/quan-va-ti-quay/work/art-320/` (PNG ngang mới), script `products/quan-va-ti-quay/work/repack-res.py`.
- Modify (bytecode): `f.class` các site vẽ màn tĩnh (2039-2095, 2121, 2147-2148).
- Modify (resource, bản copy): **pack `0,2,3,4,10`** (Codex #4 — màn tĩnh dùng cả 5 gói này, không chỉ 3/4; resource id = pack `n>>8`, blob `n&255` tại `f.java:395`). Nếu scope thực chỉ title+menu → thu hẹp danh sách gói + tiêu chí nghiệm thu cho khớp.
- Reference: `work/art-extract/*` (asset gốc).

## Implementation Steps
1. Viết `repack-res.py`: parse pack, thay blob i bằng PNG mới, **fix bảng offset cộng dồn cho MỌI blob sau** (đổi size = dịch hết offset sau). **Test size-changing (FM6)**: thay bằng blob khác-size, repack, re-parse, assert mọi blob sau target vẫn decode + khớp nguồn. (Test identity "không đổi gì" KHÔNG kiểm được cascade — vô dụng.) Offset là **u32** (Codex #8: `f.b()` đọc 4 byte LE tại `f.java:454` → chốt, **bỏ lo overflow u16**).
2. Chuẩn bị spec + asset cho Codex; Codex gen title 320×240 + nền menu 320×240 (+ panel nếu cần đổi tỉ lệ).
3. Client duyệt mockup (cổng duyệt). Chỉnh theo feedback.
4. **(Tích hợp — SAU P3, Codex ordering)** Nhét PNG mới vào pack qua repack-res.py.
5. **Patch toạ độ vẽ theo BẢNG per-site (màn,phần tử)→(x,y,anchor)** — KHÔNG dùng "(120,160)→(160,120)" chung (Codex #7: mâu thuẫn với bảng per-site; các y thật là 65/125/140/160/210...).
6. FreeJ2ME → E72: mọi màn tĩnh (kể cả `n==1,5`) hiện đúng, liền mạch, không méo.

## Success Criteria
- [ ] Title + nền menu bản 320×240 hiển thị đúng, native, client duyệt.
- [ ] Pack repack lossless (blob khác không đổi), game load art không lỗi.
- [ ] Toạ độ vẽ màn tĩnh căn giữa đúng khung ngang.
- [ ] Mọi màn tĩnh (gồm `n==1`, `n==5`) hiện đúng 320×240.
- [ ] *(Panel menu khớp vùng liệt kê — chuyển thành tiêu chí P5/tích hợp cuối, Codex; không phải cổng độc lập của P4.)*

## Risk Assessment
- **Tách dev vs duyệt (SC4)**: effort dev nhỏ (~1-2d: repack + patch per-site coord); vòng duyệt art là **thời gian calendar client-gated, KHÔNG tính vào dev-day**, có thể lặp nhiều vòng → theo dõi riêng để không âm thầm phá "~2-4 tuần".
- Codex gen không khớp nét gốc → reuse tối đa mảnh gốc, chỉ gen phần nền; lặp mockup tới khi client OK.
- Panel menu 204×74 gắn toạ độ theo layout menu (P5) → nếu đổi tỉ lệ phải đồng bộ với reflow menu; cân nhắc giữ panel, chỉ đổi vị trí ở P5.
- Sai bảng offset khi repack → hỏng cả pack. Mitigation: test **size-changing** (bước 1), không phải identity.
