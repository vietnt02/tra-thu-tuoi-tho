---
phase: 5
title: "HUD / dialog / menu reflow"
status: pending
priority: P1
effort: "PROVISIONAL 4-8d — re-baseline sau mechanism spike (bước 2)"
dependencies: [3, 4]
---

# Phase 5: HUD / dialog / menu reflow

## Overview
Phần thủ công nặng nhất (D3 "60-70% tay"): xếp lại HUD (neo góc), hộp thoại, menu cho khung ngang. Khó vì **vị trí UI nằm trong SCRIPT data**, không trong hằng Java — phải hook điểm set-vị-trí, không remap hằng.

## Requirements
- Functional: HUD neo đúng góc (mũi tên trái / 5 bàn tay giữa / avatar+tim phải, khoảng cách giãn); hộp thoại + menu nằm gọn trong 320×240; thoại wrap 320 đúng.
- Non-functional: không đụng resource script (giữ tương thích), giữ save format.

## Architecture
- **⚠ MECHANISM SPIKE — cổng cho PHẦN HOOK (SC1/AD1)**: là cổng cho việc hook vị trí HUD/camera (bước 3+), **KHÔNG chặn slice literal độc lập ở bước 1** (softkey/anim/wrap không cần spike). Giải mâu thuẫn thứ tự Codex: literal đi trước được vì độc lập; effort hook mới cần spike. Giả định "vị trí UI đi qua 1 chokepoint hook được" CHƯA verify. Bằng chứng ngược: chỗ đặt vị trí `f.java:927` **KHÔNG phải literal** — nó nạp từ mảng `a[]` (resource-derived), nên không thể coi là "1 site literal/chokepoint duy nhất" (Codex #6). Vị trí literal thật nằm ở `f.java:1824,1835,2148`; opcode 58 `f.java:1638` là TILE world (`*24+12`), không phải UI. → **chạy game, dump callsite nào fire cho từng phần tử HUD**, chốt tuple (opcode, entity-id, màn) TRƯỚC khi cam kết effort.
- **HUD neo góc**: sau spike, remap vị trí từng phần tử (mũi tên trái / 5 bàn tay giữa / avatar+tim phải) sang toạ độ landscape.
- **Vá trực tiếp, KHÔNG dựng class/bảng generic (SC2/SC3)**: phần lớn site là literal, byte-patch thẳng — softkey `f.java:996,999`, anim `f.java:1824`, static `f.java:2148`, wrap `d.java:390,495`,`b.java:471`. Site script-driven (opcode 40-46) chỉ ~4 chỗ compile trong `f.class` (`f.java:1481,1495,1502,1532`) — vá inline (cộng offset vào `object[i]` trước khi gọi `b()`), hoặc static method trên `f` sẵn có. **KHÔNG tạo class mới** (tránh downgrade toolchain + đụng tên obfuscate). Chỉ dựng bảng (màn,entity) nếu spike lộ >~20 site động.
- **Camera bound script — CƠ CHẾ RIÊNG, không chung hook vị trí (Codex #5)**: `b.c()` clamp bằng `a[1..4]` (`b.java:212`), KHÔNG qua đường đặt-vị-trí `b(x,y)`. Opcode 43-46 đặt vị trí bằng `b10.b(object[1],object[2])` **rồi set trường riêng** `b10.a(index,object[index+3])` (`f.java:1531`). → chặn đường **gán thuộc tính** (hoặc remap operand script), KHÔNG phải chặn `b(int,int)`. Hai cơ chế tách biệt.
- **Softkey label**: neo (0,320)/(240,320) → (0,240)/(320,240) tại `f.java:996,999`.
- **Wrap text — PER-SITE, không thay 240→320 mù (Codex)**: 3 caller khác nhau — `d.java:390` chỉ đổi default khi width ≤0; `d.java:495` luôn ép 240; `b.java:471` truyền 240 thẳng — khác X/anchor. Mỗi chỗ tính **bề rộng dùng được riêng** cho vị trí của nó, không dùng chung "~320". (thoại ít dòng hơn, phân trang đổi — đúng native, đã ghi nhận client).
- **Anim thả item** (đích y≈320→240): `f.java:1824-1834`.
- Panel menu (từ P4) đặt lại vị trí cho khung ngang.

## Related Code Files
- Modify (bytecode, vá inline theo lệnh): `f.class` (softkey 996/999, anim 1824-1834, opcode-driven setpos 1481/1495/1502/1532, static 2148), `d.class`/`b.class` (wrap text 390/495/471).
- Create (chỉ nếu spike lộ >~20 site động): static method trên `f` + bảng remap. Mặc định KHÔNG.
- Reference: opcode script `f.java:1479-1537`, `b.b` `f.java:927`, camera bound `b.java:216-220`, HUD layout gốc (ảnh 2 màn client gửi).

## Implementation Steps
1. **MVP slice literal-first (SC5)**: patch ngay các site literal — softkey `f.java:996,999`, anim thả item `f.java:1824` về đáy 240, wrap text `d.java:390,495`/`b.java:471` 240→~320. Ship + verify E72 sớm (cùng độ khó P2), không chờ hook.
2. **Mechanism spike (SC1/AD1)**: chạy game, dump callsite `b(x,y)` fire cho từng phần tử HUD + mốc camera pan → chốt tuple (opcode, entity, màn). Đây là cổng: chưa có tuple thì chưa cam kết effort hook.
3. Remap vị trí HUD theo tuple đã chốt (vá inline / static-on-`f`; KHÔNG class mới). Test HUD neo đúng góc, khoảng cách giãn.
4. Xử lý mốc camera script (AD1) cùng cơ chế; kiểm cutscene/pan không lộ vùng ngoài ở 320-rộng.
5. Đặt lại menu + panel (khớp art P4) cho khung ngang.
6. Duyệt từng màn UI trên FreeJ2ME → E72; client duyệt bố cục.

## Success Criteria
- [ ] HUD: mũi tên góc trái, 5 bàn tay giữa, avatar+tim góc phải — đúng khung ngang, khoảng cách giãn tự nhiên.
- [ ] Hộp thoại + menu nằm gọn 320×240, không tràn/che.
- [ ] Thoại wrap 320 đúng, phân trang mượt.
- [ ] Softkey + anim thả item đúng đáy 240.
- [ ] Không đụng resource script; save vẫn tương thích.

## Risk Assessment
- **Hook vị trí UI** là phần khó & rộng nhất — nhiều màn UI, mỗi cái vị trí riêng trong script. Mitigation: làm từng màn tuần tự tới khi **đủ hết** (client chốt làm đều, không tier — validate session 1); bảng remap tường minh để không sót màn.
- Thêm class helper Java: javac 26 không target classfile cổ → downgrade qua ProGuard `-target`/Krakatau (đã lường ở P1).
- Đổi wrap có thể làm vỡ phân trang thoại dài → test các đoạn thoại dài nhất.
