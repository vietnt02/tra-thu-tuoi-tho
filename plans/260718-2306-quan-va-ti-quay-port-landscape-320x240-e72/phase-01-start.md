---
phase: 1
title: "Toolchain vá/preverify + FreeJ2ME"
status: done  # 2026-07-19: pipeline make.sh xanh, identity round-trip byte-identical. Chốt byte-patch (bỏ preverify). FreeJ2ME→MicroEmulator.
priority: P1
effort: "0.5-1d"
dependencies: []
---

# Phase 1: Toolchain vá/preverify + FreeJ2ME

## Overview
Hoàn tất bộ công cụ để **vá bytecode → preverify CLDC → đóng gói jar/jad → chạy emulator**. Đã có OpenJDK 26 + CFR; còn thiếu preverify, cơ chế patch, và emulator. Không sửa game ở phase này — chỉ dựng đường ray.

## Requirements
- Functional: chạy được chuỗi unjar → sửa 1 class → preverify → repack → mở trong FreeJ2ME.
- Non-functional: chạy trên macOS ARM (JDK 26), không phụ thuộc WTK cũ; script tái lập được.

## Architecture
- **Preverify**: dùng **ProGuard `-microedition`** (chạy trên JDK mới) thay WTK preverify. **CHỈ preverify — KHOÁ mọi pass biến đổi** (red-team FM1): ProGuard mặc định shrink/optimize/obfuscate sẽ làm hỏng app obfuscate (inline/merge hằng đã vá, prune class load reflective, đụng tên trùng). BẮT BUỘC: `-dontshrink -dontoptimize -dontobfuscate -dontusemixedcaseclassnames -keepattributes '*' -keep 'public class CGTQ' -keep 'class **{*;}' -libraryjars <cldc11+midp20 stubs>`.
- **Gate byte-compare (FM1)**: chạy ProGuard trên JAR gốc → so byte với input, **chỉ được khác ở attribute StackMap**. Nếu ProGuard đụng gì khác → fallback preverify phoneME.
- **StackMap vs StackMapTable (red-team Q)**: xác nhận ProGuard `-microedition` emit `StackMap` (CLDC) và E72 KVM chấp nhận — kiểm ngay ở P1.
- **Patch bytecode**: chọn 1 trong — ASM (Java lib) hoặc Krakatau (assemble/disassemble). Ưu tiên ASM cho sửa hằng số/method; giữ Krakatau dự phòng cho ca khó. Lưu ý ASM có thể drop StackMap → BẮT BUỘC re-preverify sau patch.
- **Emulator**: FreeJ2ME (user nói có — xác nhận có trong máy/repo, nếu không thì tải jar chính chủ).
- Mọi thứ làm trên bản copy; `products/quan-va-ti-quay/original/` bất biến.

## Related Code Files
- Create: `tools/` (ProGuard jar, ASM jar hoặc Krakatau, FreeJ2ME jar), `products/quan-va-ti-quay/work/build/` (thư mục build), script `products/quan-va-ti-quay/work/repack.sh` (unjar→patch→preverify→jar+jad).
- Modify: none (chưa đụng game).
- Reference: `docs/toolchain-setup.md` (cập nhật lệnh thực tế sau khi chạy được).

## Implementation Steps
1. Tải + đặt vào `tools/`: ProGuard, ASM (hoặc Krakatau), FreeJ2ME. Ghi version.
2. Viết `repack.sh`: giải nén jar → (chỗ cắm patch) → ProGuard `-microedition` preverify → `jar cfm` + sinh `.jad` (đồng bộ `MIDlet-Jar-Size`, giữ `MIDlet-1=…,CGTQ`).
3. Chạy repack.sh trên JAR gốc **không sửa gì** → phải ra jar mới chạy y hệt trong FreeJ2ME (chứng minh pipeline không tự làm hỏng).
4. Xác nhận preverify chèn StackMap: mở 1 class sau preverify kiểm attribute.
5. Cập nhật `docs/toolchain-setup.md` với lệnh + version thực tế.

## Success Criteria
- [ ] `repack.sh` chạy trên JAR gốc → jar mới mở & chơi được trong FreeJ2ME, không lỗi verify.
- [ ] **Byte-compare gate xanh**: ProGuard output vs input chỉ khác StackMap (FM1).
- [ ] Preverify tạo class CLDC hợp lệ (có StackMap); xác nhận E72 chấp nhận attribute đó.
- [ ] FreeJ2ME xác nhận có, chạy được JAR gốc.
- [ ] `docs/toolchain-setup.md` cập nhật lệnh thật.

## Risk Assessment
- ProGuard `-microedition` không tạo StackMap đúng CLDC-1.0 → thử `-target 1.1`/`-microedition`; fallback preverify của phoneME. Mitigation: kiểm ở bước 4 trước khi đi tiếp.
- FreeJ2ME không chạy JAR gốc trên máy này → thử KEmulator/J2ME-Loader; nhưng verify cuối vẫn là E72 thật.
