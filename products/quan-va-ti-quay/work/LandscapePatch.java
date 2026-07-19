// Vá toạ độ ĐỔI ĐỘ DÀI lệnh (bipush<->sipush) cho port landscape 320x240.
// Chạy SAU patch-landscape.py (byte-swap cùng độ dài), TRƯỚC preverify.sh
// (ProGuard -microedition sinh lại StackMap CLDC — nên ở đây xoá StackMap cũ).
//
//   java -cp tools/asm.jar:tools/asm-tree.jar LandscapePatch.java <stage_dir>
//
// Mỗi rule = anchor chuỗi lệnh THẬT (bỏ label/line/frame) + đếm khớp chính xác;
// lệch số lượng -> fail build (không vá mù — tránh đụng mask màu 0xF0, keycode...).
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

public class LandscapePatch {
    // ---- khung matcher ----
    interface P { boolean test(AbstractInsnNode n); }
    static P intI(int op, int v) { return n -> n.getOpcode() == op && n instanceof IntInsnNode && ((IntInsnNode) n).operand == v; }
    static P op(int o) { return n -> n.getOpcode() == o; }
    static P getF(String owner, String name) { return n -> n.getOpcode() == Opcodes.GETFIELD && n instanceof FieldInsnNode f && f.owner.equals(owner) && f.name.equals(name); }
    static P putF(String owner, String name) { return n -> n.getOpcode() == Opcodes.PUTFIELD && n instanceof FieldInsnNode f && f.owner.equals(owner) && f.name.equals(name); }
    static P getS(String owner, String name) { return n -> n.getOpcode() == Opcodes.GETSTATIC && n instanceof FieldInsnNode f && f.owner.equals(owner) && f.name.equals(name); }
    static P anyIstore = n -> { int o = n.getOpcode(); return o == Opcodes.ISTORE; };
    static P istore(int var) { return n -> n.getOpcode() == Opcodes.ISTORE && n instanceof VarInsnNode v && v.var == var; }

    static AbstractInsnNode nextReal(AbstractInsnNode n) {
        n = n.getNext();
        while (n != null && n.getOpcode() < 0) n = n.getNext();
        return n;
    }

    /** Tìm mọi vị trí khớp pattern trong method; trả list node đầu chuỗi (node THẬT). */
    static List<AbstractInsnNode[]> find(MethodNode m, P[] pat) {
        List<AbstractInsnNode[]> hits = new ArrayList<>();
        for (AbstractInsnNode n = m.instructions.getFirst(); n != null; n = n.getNext()) {
            if (n.getOpcode() < 0) continue;
            AbstractInsnNode[] seq = new AbstractInsnNode[pat.length];
            AbstractInsnNode c = n; boolean ok = true;
            for (int i = 0; i < pat.length; i++) {
                if (c == null || !pat[i].test(c)) { ok = false; break; }
                seq[i] = c; c = nextReal(c);
            }
            if (ok) hits.add(seq);
        }
        return hits;
    }

    static int applied = 0;
    /** Áp rule: trong các method lọc bởi mf, thay thế theo repl tại index idx của pattern. expect = số site bắt buộc. */
    static void rule(ClassNode cn, Predicate<MethodNode> mf, P[] pat, int idx, IntInsnNode repl, int expect, String label) {
        int total = 0;
        for (MethodNode m : cn.methods) {
            if (!mf.test(m)) continue;
            for (AbstractInsnNode[] seq : find(m, pat)) {
                m.instructions.set(seq[idx], new IntInsnNode(repl.getOpcode(), repl.operand));
                total++;
            }
        }
        if (total != expect) throw new RuntimeException("[FAIL] " + label + ": matched " + total + " != expect " + expect);
        System.out.println("[OK] " + label + " x" + total);
        applied += total;
    }

    static Predicate<MethodNode> any = m -> true;
    static Predicate<MethodNode> named(String name, String desc) { return m -> m.name.equals(name) && m.desc.equals(desc); }

    /** Chèn chuỗi lệnh SAU node thứ idx của pattern (đổi độ dài + stack — cần
     *  ClassWriter COMPUTE_MAXS + ProGuard preverify phía sau). */
    static void insertAfter(ClassNode cn, Predicate<MethodNode> mf, P[] pat, int idx, AbstractInsnNode[] ins, int expect, String label) {
        int total = 0;
        for (MethodNode m : cn.methods) {
            if (!mf.test(m)) continue;
            for (AbstractInsnNode[] seq : find(m, pat)) {
                AbstractInsnNode at = seq[idx];
                for (int i = ins.length - 1; i >= 0; i--) m.instructions.insert(at, ins[i]);
                total++;
            }
        }
        if (total != expect) throw new RuntimeException("[FAIL] " + label + ": matched " + total + " != expect " + expect);
        System.out.println("[OK] " + label + " x" + total);
        applied += total;
    }

    /** Swap ĐỒNG THỜI mọi IntInsn (opA,valA) <-> (opB,valB) trong các method lọc bởi mf
     *  (1 pass — tránh rule 2 chiều khớp nhầm site vừa đổi). */
    static void swap(ClassNode cn, Predicate<MethodNode> mf, int opA, int valA, int opB, int valB, int expA, int expB, String label) {
        int ca = 0, cb = 0;
        for (MethodNode m : cn.methods) {
            if (!mf.test(m)) continue;
            for (AbstractInsnNode n = m.instructions.getFirst(); n != null; n = n.getNext()) {
                if (!(n instanceof IntInsnNode ii)) continue;
                if (ii.getOpcode() == opA && ii.operand == valA) {
                    AbstractInsnNode r = new IntInsnNode(opB, valB);
                    m.instructions.set(n, r); n = r; ca++;
                } else if (ii.getOpcode() == opB && ii.operand == valB) {
                    AbstractInsnNode r = new IntInsnNode(opA, valA);
                    m.instructions.set(n, r); n = r; cb++;
                }
            }
        }
        if (ca != expA || cb != expB) throw new RuntimeException("[FAIL] " + label + ": " + ca + "/" + cb + " != " + expA + "/" + expB);
        System.out.println("[OK] " + label + " x" + (ca + cb));
        applied += ca + cb;
    }

    public static void main(String[] args) throws Exception {
        Path stage = Paths.get(args[0]);

        // ================= f.class =================
        ClassNode f = load(stage.resolve("f.class"));
        Predicate<MethodNode> gI = named("g", "(I)V");
        // painter màn tĩnh g(int): fillRect/dim 240x320 -> 320x240 (6 cặp)
        rule(f, gI, new P[]{intI(Opcodes.SIPUSH,240), intI(Opcodes.SIPUSH,320)}, 0, si(320), 6, "f.g(I) rect w 240->320");
        rule(f, gI, new P[]{intI(Opcodes.SIPUSH,320), intI(Opcodes.SIPUSH,320)}, 1, si(240), 6, "f.g(I) rect h 320->240");
        // draw/text center (120,160) -> (160,120): 4 drawImage + 1 text n==8
        rule(f, gI, new P[]{intI(Opcodes.BIPUSH,120), intI(Opcodes.SIPUSH,160)}, 0, si(160), 5, "f.g(I) center x 120->160");
        rule(f, gI, new P[]{intI(Opcodes.SIPUSH,160), intI(Opcodes.SIPUSH,160)}, 1, bi(120), 5, "f.g(I) center y 160->120");
        // n==4: logo (120,65)->(160,65); text (120,210)->(160,170)
        rule(f, gI, new P[]{intI(Opcodes.BIPUSH,120), intI(Opcodes.BIPUSH,65)}, 0, si(160), 1, "f.g(I) n4 logo x");
        rule(f, gI, new P[]{intI(Opcodes.BIPUSH,120), intI(Opcodes.SIPUSH,210)}, 0, si(160), 1, "f.g(I) n4 text x");
        rule(f, gI, new P[]{intI(Opcodes.SIPUSH,160), intI(Opcodes.SIPUSH,210)}, 1, bi(170), 1, "f.g(I) n4 text y 210->170");
        // n==6: text dialog (120,140)->(160,140)
        rule(f, gI, new P[]{intI(Opcodes.BIPUSH,120), intI(Opcodes.SIPUSH,140)}, 0, si(160), 1, "f.g(I) n6 text x");
        // camera A: (b.e+c.e)/2-120 -> -160 ; (b.f+c.f)/2-160 -> -120
        // (istore_0/istore_1 phân biệt site x/y — pattern trần sẽ khớp nhầm site vừa vá)
        rule(f, any, new P[]{op(Opcodes.IADD), op(Opcodes.ICONST_2), op(Opcodes.IDIV), intI(Opcodes.BIPUSH,120), op(Opcodes.ISUB), istore(0)}, 3, si(160), 1, "f camA x -120->-160");
        rule(f, any, new P[]{op(Opcodes.IADD), op(Opcodes.ICONST_2), op(Opcodes.IDIV), intI(Opcodes.SIPUSH,160), op(Opcodes.ISUB), istore(1)}, 3, bi(120), 1, "f camA y -160->-120");
        // camera B: e.e = b.e-120 -> -160 ; e.f = b.f-160 -> -120
        rule(f, any, new P[]{getF("b","e"), intI(Opcodes.BIPUSH,120), op(Opcodes.ISUB), putF("b","e")}, 1, si(160), 1, "f camB x");
        rule(f, any, new P[]{getF("b","f"), intI(Opcodes.SIPUSH,160), op(Opcodes.ISUB), putF("b","f")}, 1, bi(120), 1, "f camB y");
        // softkey: (0,320)->(0,240) trái; (240,320)->(320,240) phải
        rule(f, any, new P[]{getS("f","r"), op(Opcodes.ICONST_0), intI(Opcodes.SIPUSH,320), op(Opcodes.ICONST_0)}, 2, si(240), 1, "f softkey L y");
        rule(f, any, new P[]{getS("f","s"), intI(Opcodes.SIPUSH,240), intI(Opcodes.SIPUSH,320), op(Opcodes.ICONST_0)}, 1, si(320), 1, "f softkey R x");
        rule(f, any, new P[]{getS("f","s"), intI(Opcodes.SIPUSH,320), intI(Opcodes.SIPUSH,320), op(Opcodes.ICONST_0)}, 2, si(240), 1, "f softkey R y");
        // case 72 (nhặt item): ô item HUD neo đáy màn dọc -> kéo lên cho khớp màn 240.
        // widget 518 khung: start (48,370) -> đích y320; widget 519 icon: đích y315.
        // Anchor bằng sipush 370 (duy nhất) TRƯỚC khi đổi nó, để bắt đúng sipush 320 đích.
        rule(f, any, new P[]{intI(Opcodes.SIPUSH,370), op(Opcodes.INVOKEVIRTUAL), op(Opcodes.ALOAD), op(Opcodes.INVOKEVIRTUAL), op(Opcodes.ALOAD), op(Opcodes.ICONST_1), op(Opcodes.ICONST_3), intI(Opcodes.SIPUSH,320)}, 7, si(240), 1, "item box target y 320->240");
        rule(f, any, new P[]{intI(Opcodes.SIPUSH,370)}, 0, si(290), 1, "item box start y 370->290");
        rule(f, any, new P[]{intI(Opcodes.SIPUSH,315)}, 0, si(235), 1, "item icon y 315->235");
        // fade toàn màn: a(g,0,0,240,320,x,alpha) -> 320x240
        rule(f, any, new P[]{op(Opcodes.ICONST_0), op(Opcodes.ICONST_0), intI(Opcodes.SIPUSH,240), intI(Opcodes.SIPUSH,320), getS("f","x")}, 2, si(320), 1, "f fade w");
        rule(f, any, new P[]{op(Opcodes.ICONST_0), op(Opcodes.ICONST_0), intI(Opcodes.SIPUSH,320), intI(Opcodes.SIPUSH,320), getS("f","x")}, 3, si(240), 1, "f fade h");
        // bảng tên vòng trong khung gameplay i(int): (120,125,TOP|HCENTER) -> x 160
        rule(f, any, new P[]{intI(Opcodes.BIPUSH,120), intI(Opcodes.BIPUSH,125), intI(Opcodes.BIPUSH,17)}, 0, si(160), 1, "f round plaque x");
        // d(int): neo hộp thoại vào tâm màn (120,160) -> (160,120), 2 call
        Predicate<MethodNode> dI = named("d", "(I)V");
        rule(f, dI, new P[]{intI(Opcodes.BIPUSH,120), intI(Opcodes.SIPUSH,160)}, 0, si(160), 2, "f dialog anchor x");
        rule(f, dI, new P[]{intI(Opcodes.SIPUSH,160), intI(Opcodes.SIPUSH,160)}, 1, bi(120), 2, "f dialog anchor y");
        save(f, stage.resolve("f.class"));

        // ================= b.class =================
        ClassNode b = load(stage.resolve("b.class"));
        // camera C (follow mượt case 5): e-120 -> -160 ; f-160 -> -120
        rule(b, any, new P[]{getF("b","e"), intI(Opcodes.BIPUSH,120), op(Opcodes.ISUB), anyIstore}, 1, si(160), 1, "b camC x");
        rule(b, any, new P[]{getF("b","f"), intI(Opcodes.SIPUSH,160), op(Opcodes.ISUB), anyIstore}, 1, bi(120), 1, "b camC y");
        // wrap tiêu đề list: |8, 240 -> 320
        rule(b, any, new P[]{intI(Opcodes.BIPUSH,8), op(Opcodes.IOR), intI(Opcodes.SIPUSH,240)}, 2, si(320), 1, "b list title wrap");
        save(b, stage.resolve("b.class"));

        // ================= d.class =================
        ClassNode d = load(stage.resolve("d.class"));
        // default wrap khi n<=0: 240 -> 320 (sau IFGT)
        rule(d, any, new P[]{op(Opcodes.IFGT), intI(Opcodes.SIPUSH,240), anyIstore}, 1, si(320), 1, "d default wrap");
        // hard wrap trong a(Graphics,byte[],IIII)Z: n4=240 -> 320
        rule(d, named("a", "(Ljavax/microedition/lcdui/Graphics;[BIIII)Z"), new P[]{intI(Opcodes.SIPUSH,240)}, 0, si(320), 1, "d hard wrap");
        save(d, stage.resolve("d.class"));

        // ================= g.class =================
        // Ring buffer đã xoay 264x360 -> 360x264 (f.createImage), nhưng toán wrap
        // của buffer vẫn theo khổ dọc -> seam/tile cắt dở. Đảo theo trục:
        ClassNode g = load(stage.resolve("g.class"));
        String GFX = "(Ljavax/microedition/lcdui/Graphics;)V";
        // a(II): o=x%264 <-> p=y%360 ; lưới tile 11 cột <-> 15 hàng (24px/tile)
        swap(g, named("a","(II)V"), Opcodes.SIPUSH,264, Opcodes.SIPUSH,360, 1, 1, "g.a(II) wrap 264<->360");
        swap(g, named("a","(II)V"), Opcodes.BIPUSH,11, Opcodes.BIPUSH,15, 8, 8, "g.a(II) tiles 11<->15");
        // a(Graphics): blit 4 bản buffer, offset +264(x) <-> +360(y)
        swap(g, named("a",GFX), Opcodes.SIPUSH,264, Opcodes.SIPUSH,360, 2, 2, "g.a(Gfx) offsets");
        // b(IIIIII): ngưỡng wrap pixel khi vẽ tile vào buffer
        swap(g, named("b","(IIIIII)V"), Opcodes.SIPUSH,264, Opcodes.SIPUSH,360, 1, 1, "g.b wrap thresholds");
        save(g, stage.resolve("g.class"));

        // ================= k.class =================
        ClassNode k = load(stage.resolve("k.class"));
        // hằng script SCREEN_W/H (opcode 40, idx 9/10): 240<->320
        rule(k, named("a", "(II)I"), new P[]{intI(Opcodes.BIPUSH,9), op(Opcodes.IF_ICMPNE), intI(Opcodes.SIPUSH,240), op(Opcodes.IRETURN)}, 2, si(320), 1, "k SCREEN_W 240->320");
        rule(k, named("a", "(II)I"), new P[]{intI(Opcodes.BIPUSH,10), op(Opcodes.IF_ICMPNE), intI(Opcodes.SIPUSH,320), op(Opcodes.IRETURN)}, 2, si(240), 1, "k SCREEN_H 320->240");
        save(k, stage.resolve("k.class"));

        System.out.println("[DONE] " + applied + " site(s) patched");
    }

    static IntInsnNode si(int v) { return new IntInsnNode(Opcodes.SIPUSH, v); }
    static IntInsnNode bi(int v) { return new IntInsnNode(Opcodes.BIPUSH, v); }

    static ClassNode load(Path p) throws Exception {
        ClassNode cn = new ClassNode();
        // SKIP_FRAMES + bỏ attribute lạ (StackMap CLDC cũ) — ProGuard sinh lại sau.
        new ClassReader(Files.readAllBytes(p)).accept(cn, ClassReader.SKIP_FRAMES);
        cn.attrs = null;
        for (MethodNode m : cn.methods) m.attrs = null;
        return cn;
    }
    static void save(ClassNode cn, Path p) throws Exception {
        // COMPUTE_MAXS: có rule chèn lệnh làm stack sâu hơn; không cần hierarchy (khác COMPUTE_FRAMES)
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        Files.write(p, cw.toByteArray());
    }
}
