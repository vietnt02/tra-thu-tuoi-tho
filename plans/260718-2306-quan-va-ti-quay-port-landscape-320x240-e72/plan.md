---
title: "Quan va Ti Quay - port landscape 320x240 E72"
description: "Port MIDP game Quan và Tí Quậy từ portrait 240x320 sang native landscape 320x240 cho Nokia E72 (RM-530) bằng vá bytecode — reflow world/HUD/màn tĩnh, không scale/rotate."
status: pending
priority: P1
effort: "~2-4 tuần (PROVISIONAL — chờ mechanism spike P5 re-baseline)"
tags: [j2me, midp, bytecode-patch, landscape-port, e72]
created: 2026-07-18
---

# Quan va Ti Quay — port landscape 320x240 (E72)

## Overview

Mod game J2ME **Quan và Tí Quậy** (MIDP-2.0, obfuscate 1 ký tự) chạy đúng trên **Nokia E72 (RM-530), 320×240 QVGA ngang cứng**, chuẩn **native tái tạo** (decisions.md D1/D5): world cho thấy khung nhìn rộng hơn nội dung có sẵn, màn tĩnh dựng lại cho khung ngang, HUD neo góc. KHÔNG scale, KHÔNG rotate. **Buộc vá bytecode** (recompile bất khả — D6). Làm cả game, chia phase, mỗi phase cho ra thứ nghiệm thu được.

Nền tảng đã có: OpenJDK 26 + CFR (`tools/cfr.jar`), JAR decompiled (`products/quan-va-ti-quay/work/decompiled/`), art tách sẵn (`.../work/art-extract/`). Review kỹ thuật: `plans/reports/brainstorm-260718-2253-quan-va-ti-quay-landscape-review.md`.

## Goals

| # | Goal | Priority |
|---|------|----------|
| 1 | Game chạy full 320×240 trên E72 thật, native (không viền/độn/chắp vá) | P1 |
| 2 | World reflow: khung nhìn ngang lộ nội dung có sẵn, không rác mép, không tụt fps | P1 |
| 3 | Màn tĩnh (title, menu) + HUD/dialog dựng lại đúng khung ngang | P1 |
| 4 | Giữ nguyên audio + save + vibrate; chơi trọn game không regression | P1 |
| 5 | Pipeline vá→preverify→repack tái lập được (dùng lại cho game sau) | P2 |

## Phases

| # | Phase | Status |
|---|-------|--------|
| 1 | [Toolchain vá/preverify + FreeJ2ME](./phase-01-start.md) | ✅ Done (2026-07-19) + render harness 320×240 |
| 2 | [Spike: patch→preverify→run trên E72 + đo phím](./phase-02-spike-prove-patch-preverify-run-on-e72.md) | ✅ GO (video: CROCO xanh→đỏ trên E72). Đo phím xong. |
| 3 | [World landscape viewport](./phase-03-world-landscape-viewport.md) | ✅ Harness-verified (2026-07-19): viewport 320×240 + camera 3 site (-160/-120) + ring buffer wrap (g.class %/lưới/offset) — không seam, nhân vật giữa màn. Chờ nghiệm thu E72 thật. |
| 4 | [Màn tĩnh art 320×240 (Codex)](./phase-04-static-screens-art-320x240.md) | 🔄 v1 XONG qua harness: splash CROCO, title, menu (art ngang mới), khung gameplay, màn "Vòng", story. Còn: tinh chỉnh art menu (đầu nhân vật bị dải blend cắt) — chờ client duyệt. |
| 5 | [HUD / dialog / menu reflow](./phase-05-hud-dialog-menu-reflow.md) | 🔄 Menu list/softkey/text vòng/wrap text XONG (k.a SCREEN_W/H flip + ASM). Còn: hộp thoại cuộn tre + toạ độ tường minh trong script data (vá blob, per-dialog). |
| 6 | [Input verify + full regression](./phase-06-input-verify-and-full-regression.md) | Pending |

Phụ thuộc: P1→P2→P3→P5→P6. **P4**: vẽ art + repack script *song song* P3; **tích hợp (ráp vào game) phải SAU P3** vì màn tĩnh vẽ vào buffer chung chưa nới (Codex). P4 dependencies=[2,3].

## Success Criteria

- [ ] Chơi trọn game trên E72 thật ở 320×240: world/menu/dialog/HUD đều native, không viền đen/độn/chắp vá.
- [ ] Đủ 5 map đi lại full màn, camera clamp đúng, không lộ mép, không rác tile.
- [ ] Title + nền menu là bản ngang mới (Codex), client đã duyệt mockup.
- [ ] HUD neo góc đúng (mũi tên trái / 5 bàn tay giữa / avatar+tim phải), thoại wrap 320 gọn.
- [ ] Audio, save (RecordStore), vibrate hoạt động như bản gốc.
- [ ] JAR + JAD đóng gói lại, preverify sạch, cài chạy trên E72.

## Ràng buộc & quyết định neo

- **Không remap phím** (D7): chơi ở mode số của E72. Verify d-pad ăn ở mode số + có màn gõ chữ không.
- **Art giao Codex gen** (D7): tái dùng mảnh gốc + mở rộng nền; client duyệt.
- Giữ `original/` bất biến; làm trên bản copy.
- Verify từng phase trên FreeJ2ME, cuối cùng trên **E72 thật** (perf/crash ẩn chỉ lộ ở máy thật).

## Open Questions

- Có màn nào cần gõ chữ (đặt tên save) buộc chuyển mode chữ giữa game không? → đóng ở P2/P6.
- fps trên E72 — vẫn đo ở P2/P3, nhưng rủi ro THẤP (Codex: 320×240 = 240×320 về số px, tải/frame gần như không đổi).
- FreeJ2ME đã có sẵn hay cần tải → xác nhận ở P1.

## Validation Log

### Verification Results (2026-07-18)
- Tier: Full (6 phases). Claims checked: 5 then-chốt / Verified: 5 / Failed: 0 / Unverified: 0.
- Bằng chứng: `g.java:218` `l-240-1`/`m-320-1` (clamp) ✓; `f.java:868` `createImage(264,360)` (ring buffer) ✓; `f.java:996,999` softkey `(0,320)/(240,320)` ✓; `d.java:390,495` wrap `240` ✓.

### Quyết định (validate session 1)
1. **Nghiệm thu (P6) = chơi tuần tự thật, KHÔNG nhảy save.** Client chỉ ra game gated (bắt xong màn này mới qua màn khác), không phải bản hack → phải chơi tới đâu kiểm tới đó, giữ save tích luỹ theo tiến trình. Không giả định shortcut.
2. **P5 làm đều tất cả màn UI** — không tier/không để màn hiếm lại sau. Coi P5 xong khi mọi màn UI reflow chuẩn.
3. **Spike P2 vỡ → tự cố đường vá khó hơn** (Krakatau / vá hex) trước khi báo; không dừng dự án vì 1 trắc trở kỹ thuật.

### Whole-Plan Consistency Sweep
- P6: acceptance đổi sang playthrough tuần tự (đã khớp — P6 vốn "chơi xuyên suốt"; bỏ mọi ý "nhảy đoạn").
- P5: gỡ mitigation "ưu tiên màn chính, màn phụ sau" → làm đều hết.
- P2: bước fail đổi từ "dừng đánh giá lại" → "tự thử Krakatau/hex trước khi escalate".
- Không còn mâu thuẫn tồn đọng.

## Red Team Review

3 reviewer đối kháng (Failure Mode, Assumption Destroyer, Scope/Complexity), đọc code decompiled lấy bằng chứng. 15 finding sau dedup, **tất cả Accept** (đều có `file:line`, không cái nào yếu).

| # | Sev | Finding | Đã áp vào |
|---|-----|---------|-----------|
| 1 | Crit | ProGuard mặc định biến đổi app obfuscate → khoá `-dontshrink/-dontoptimize/-dontobfuscate/-keep/-libraryjars` + byte-compare gate | P1 |
| 2 | Crit | Swap hằng theo giá trị đụng bảng keycode `f.java:2164` → vá theo từng lệnh; mở rộng avoid-list | P3 |
| 3 | Crit | "5 map đủ lớn" chưa verify → gate dump map-dim, assert ≥320×240 trước swap | P3 (step 0) |
| 4 | Crit | Cơ chế P5 (HUD+camera vị trí trong script) chưa verify → mechanism spike trước khi cam kết | P5 |
| 5 | High | Màn tĩnh là ảnh ghép nhiều lớp, không phải PNG center-swap → bảng per-site (màn,phần tử,anchor) | P4 |
| 6 | High | Thiếu `fillRect(0,0,240,320)` `f.java:2042,2048,2057,2092` | P3/P4 |
| 7 | High | Class helper + bảng generic = over-engineering → vá inline/static-on-`f`, không class mới | P5 |
| 8 | High | Ring buffer ATOMIC, không bisect được → 1 commit + bảng kiểm kê + test seam | P3 |
| 9 | High | Test repack identity vô dụng → test blob khác-size + kiểm offset u16/u32 | P4 |
| 10 | Med | Spike P2 quá dễ → thêm patch multi-constant trong method obfuscate + check StackMap | P2 |
| 11 | Med | D7 "không remap" mâu thuẫn P2 đo phím → D7 TẠM, định nhánh cho remap nếu mã khác | P2, D7 |
| 12 | Med | opcode-40 `k.java:125,128` cần audit consumer, không flip mù | P3 |
| 13 | Med | Tách P4 dev-effort vs vòng duyệt art (calendar) | P4 |
| 14 | Med | P5 literal-first làm MVP slice, tách hook | P5 |
| 15 | Med | "~2-4 tuần" provisional chờ spike P5 | plan.md/P5 |

### Whole-Plan Consistency Sweep (red-team)
- P1: ProGuard đổi từ "preverify" chung → khoá pass + gate. P2: spike thêm path rủi ro + D7 tạm. P3: thêm gate map-dim, ring-buffer atomic, avoid-list keycode, fillRect, opcode audit, camera-bound cross-ref P5. P4: per-site table + fillRect + repack size-test + tách effort. P5: mechanism spike + bỏ class + literal-first. P6: save hiệu chỉnh low-risk.
- Effort P4/P5 và top-line đổi sang provisional — đồng bộ frontmatter + plan.md.
- Không còn mâu thuẫn tồn đọng.

## Codex Review (model độc lập, 2026-07-18)

Codex xác nhận phần lõi (hằng số camera/viewport/ring buffer, avoid-list, softkey/wrap/save, opcode-40) ĐÚNG. 8 flaw + risk + ordering, **tất cả đã áp**:

| # | Vấn đề | Đã áp |
|---|--------|-------|
| 1 | Số `f.java:NNN` là dòng CFR, không phải vị trí bytecode → định vị thật lúc vá (owner+desc+idx) | P3 |
| 2 | "+33% pixel/frame" SAI (320×240=240×320 về px) → rủi ro fps thấp | P3 |
| 3 | Sót màn tĩnh `n==1`, `n==5` | P4 |
| 4 | Màn tĩnh dùng pack 0,2,3,4,10 (không chỉ 3,4) | P4 |
| 5 | Hook vị trí KHÔNG sửa được mốc camera (là gán trường riêng, không qua b(x,y)) → cơ chế tách | P5 |
| 6 | Dẫn chứng `f.java:927` sai (không phải literal, đọc từ `a[]`) | P5 |
| 7 | P4 bước 5 còn "120,160→160,120" mâu thuẫn bảng per-site → bỏ | P4 |
| 8 | Offset pack chốt = u32 (đọc 4 byte LE) → bỏ câu hỏi u16 | P4 |
| R1 | opcode-40 operand tường minh bỏ qua default đã vá → kiểm kê operand script | P3 |
| R2 | wrap text 3 caller khác nhau → per-site, không thay 240→320 mù | P5 |
| R3 | input P6 phải tiêu thụ kết quả đo P2 (không cứng "không remap") | P6 |
| O1 | P4 tích hợp phải sau P3 (buffer chung chưa nới) → deps=[2,3] | P4/plan |
| O2 | P3 nghiệm thu loại trừ pan script (xử lý ở P5) | P3 |
| O3 | P3/P4 cùng đụng `f.java:2039-2095` → 1 bảng patch gộp/merge order | P3/P4 |
| O4 | P5 spike là cổng cho phần HOOK, không chặn slice literal | P5 |

Codex KHÔNG lật hướng đi — chủ yếu vá chi tiết + gỡ mâu thuẫn nội bộ.

<!-- slug: quan-va-ti-quay-port-landscape-320x240-e72 -->
<!-- Updated: Validation S1 + Red Team S1 + Codex Review S1 -->

