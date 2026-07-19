/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Canvas
 *  javax.microedition.lcdui.Display
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 *  javax.microedition.media.Manager
 *  javax.microedition.media.Player
 *  javax.microedition.midlet.MIDlet
 *  javax.microedition.rms.RecordStore
 */
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public final class f
extends Canvas
implements Runnable {
    private static CGTQ a;
    private static boolean d;
    private static boolean e;
    private static boolean f;
    static int a;
    static int b;
    private static int g;
    private static int h;
    private static int i;
    private static boolean g;
    private static int j;
    static boolean a;
    private static byte[][] a;
    private static Player[] a;
    private static boolean h;
    private static a a;
    private static int k;
    private static byte[][] b;
    private static InputStream a;
    private static byte[][][] a;
    static int c;
    private static int[] c;
    private static Random a;
    private static int l;
    private static int m;
    private static int n;
    private static int o;
    private static int[] d;
    static boolean b;
    static int[] a;
    private static Image a;
    private static Graphics a;
    static int[] b;
    static int d;
    private static int p;
    private static int q;
    private static boolean i;
    static boolean c;
    static int e;
    static int f;
    private static int r;
    private static int s;
    private static int t;
    private static int u;
    private static int v;
    private static int w;
    private static int x;
    private static int y;
    private static int z;
    static d[] a;
    static k[] a;
    static b[][] a;
    static b[] a;
    static b a;
    private static b e;
    private static k a;
    private static int[][] a;
    private static int[] e;
    private static boolean j;
    private static int[] f;
    private static int[] g;
    private static int[] h;
    private static short[] a;
    private static int A;
    private static int B;
    static b b;
    private static int C;
    private static int D;
    static b c;
    private static boolean k;
    private static boolean l;
    static b d;

    public f(CGTQ cGTQ) {
        a = cGTQ;
        this.setFullScreenMode(true);
        f.b(1, false);
    }

    public final void run() {
        d = true;
        while (d) {
            try {
                if (e) {
                    Thread.sleep(100L);
                    continue;
                }
                long l2 = System.currentTimeMillis();
                h = b;
                i = g;
                f.g();
                if (h == b) {
                    b = 0;
                }
                if (i == g) {
                    g = 0;
                }
                f = true;
                this.repaint();
                this.serviceRepaints();
                d &= !c;
                long l3 = l2 + 40L - System.currentTimeMillis();
                if (l3 <= 0L) continue;
                Thread.sleep(l3);
            }
            catch (Exception exception) {
                d = false;
            }
        }
        a.destroyApp(true);
    }

    public final void paint(Graphics graphics) {
        if (!f) {
            return;
        }
        f = false;
        f.a(graphics);
    }

    public final void hideNotify() {
        if (e) {
            return;
        }
        e = true;
        f.c();
    }

    public final void showNotify() {
        if (!e) {
            return;
        }
        e = false;
        f.d();
    }

    public final void keyPressed(int n) {
        if (g) {
            return;
        }
        int n2 = f.a(n);
        a |= n2;
        b = n2;
    }

    public final void keyReleased(int n) {
        if (g) {
            return;
        }
        int n2 = f.a(n);
        a &= ~n2;
        g = n2;
    }

    private static int a(int n) {
        if (n == -1) {
            return 1024;
        }
        if (n == -2) {
            return 2048;
        }
        if (n == -3) {
            return 4096;
        }
        if (n == -4) {
            return 8192;
        }
        if (n == -5) {
            return 16384;
        }
        if (n == 48) {
            return 1;
        }
        if (n == 49) {
            return 2;
        }
        if (n == 50) {
            return 4;
        }
        if (n == 51) {
            return 8;
        }
        if (n == 52) {
            return 16;
        }
        if (n == 53) {
            return 32;
        }
        if (n == 54) {
            return 64;
        }
        if (n == 55) {
            return 128;
        }
        if (n == 56) {
            return 256;
        }
        if (n == 57) {
            return 512;
        }
        if (n == 35) {
            return 65536;
        }
        if (n == 42) {
            return 32768;
        }
        if (n == -6) {
            return 131072;
        }
        if (n == -7) {
            return 262144;
        }
        return 0;
    }

    private static Player a(byte[] object) {
        try {
            String string = null;
            if (object[0] == 82 && object[1] == 73 && object[2] == 70 && object[3] == 70) {
                string = "audio/x-wav";
            }
            if (object[0] == 35 && object[1] == 33 && object[2] == 65 && object[3] == 77 && object[4] == 82) {
                string = "audio/x-amr";
            }
            if (object[0] == 77 && object[1] == 84 && object[2] == 104 && object[3] == 100) {
                string = "audio/midi";
            }
            Player player = Manager.createPlayer((InputStream)new ByteArrayInputStream((byte[])object), string);
            object = player;
            player.realize();
            return object;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static void e(int n) {
        if (n == -1) {
            for (n = 0; n < 14; ++n) {
                f.e(n);
            }
        } else {
            try {
                if (a[n] != null) {
                    f.a[n] = null;
                }
                return;
            }
            catch (Exception exception) {}
        }
    }

    static void a(int n, int n2, int n3) {
        int n4 = n3;
        n3 = n2;
        n2 = n;
        a a2 = a;
        a.a = n2;
        a2.b = n3;
        a2.c = n4;
        new Thread(a2).start();
    }

    static void b(int n, int n2, int n3) {
        if (!a) {
            return;
        }
        try {
            if (h) {
                return;
            }
            h = true;
            f.a(n2);
            f.a[n2] = f.a(a[n]);
            a[n2].setLoopCount(n3);
            a[n2].start();
        }
        catch (Exception exception) {}
        h = false;
    }

    static void a(int n) {
        if (n == -1) {
            for (n = 0; n < 2; ++n) {
                f.a(n);
            }
        } else {
            try {
                if (a[n] != null) {
                    a[n].stop();
                    a[n].deallocate();
                    a[n].close();
                    f.a[n] = null;
                }
                return;
            }
            catch (Exception exception) {}
        }
    }

    private static void c() {
        j = 0;
        for (int i2 = 0; i2 < 2; ++i2) {
            int n = i2;
            if (!(a[n] != null && a[n].getState() == 400)) continue;
            j |= 1 << i2;
            try {
                a[i2].stop();
                continue;
            }
            catch (Exception exception) {}
        }
    }

    private static void d() {
        if (a == null) {
            return;
        }
        for (int i2 = 0; i2 < 2; ++i2) {
            if ((j & 1 << i2) == 0 || a[i2] == null) continue;
            try {
                a[i2].start();
                continue;
            }
            catch (Exception exception) {}
        }
        j = 0;
    }

    private static byte[] a(String string) {
        try {
            string = RecordStore.openRecordStore((String)string, (boolean)false);
            byte[] byArray = string.getRecord(1);
            string.closeRecordStore();
            return byArray;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static boolean a(String string, byte[] byArray) {
        try {
            string = RecordStore.openRecordStore((String)string, (boolean)true);
            if (string.getNumRecords() == 0) {
                string.addRecord(byArray, 0, byArray.length);
            } else {
                string.setRecord(1, byArray, 0, byArray.length);
            }
            string.closeRecordStore();
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    private static void e() {
        k = -1;
        b = null;
    }

    static byte[] a(int n) {
        int n2 = n >> 8;
        if (k != n2) {
            int n3;
            f.e();
            Object object = "/" + n2;
            if (a != null) {
                f.f();
            }
            try {
                if (((String)object).charAt(0) != '/') {
                    object = "/" + (String)object;
                }
                a = "".getClass().getResourceAsStream((String)object);
            }
            catch (Exception exception) {}
            f.b();
            int n4 = f.a() & 0xFF;
            int[] nArray = new int[n4 + 1];
            int[] nArray2 = nArray;
            nArray[0] = 0;
            for (n3 = 1; n3 <= n4; ++n3) {
                nArray2[n3] = f.b();
            }
            b = new byte[n4][];
            for (n3 = 0; n3 < n4; ++n3) {
                f.b[n3] = new byte[nArray2[n3 + 1] - nArray2[n3]];
                int n5 = b[n3].length;
                boolean bl = false;
                object = b[n3];
                try {
                    a.read((byte[])object, 0, n5);
                    continue;
                }
                catch (Exception exception) {}
            }
            f.f();
            k = n2;
        }
        return b[n & 0xFF];
    }

    private static void f() {
        try {
            a.close();
        }
        catch (Exception exception) {}
        a = null;
    }

    private static int a() {
        try {
            return a.read();
        }
        catch (Exception exception) {
            return 0;
        }
    }

    private static int b() {
        try {
            int n = a.read();
            int n2 = a.read();
            int n3 = a.read();
            int n4 = a.read();
            return n | n2 << 8 | n3 << 16 | n4 << 24;
        }
        catch (Exception exception) {
            return 0;
        }
    }

    static byte[] b(int n) {
        return a[c][n];
    }

    static int a(byte[] byArray, int n) {
        return byArray[n] & 0xFF | (byArray[n + 1] & 0xFF) << 8 | (byArray[n + 2] & 0xFF) << 16 | (byArray[n + 3] & 0xFF) << 24;
    }

    static short a(byte[] byArray, int n) {
        return (short)(byArray[n] & 0xFF | (byArray[n + 1] & 0xFF) << 8);
    }

    static int a(byte[] byArray, int n, int n2) {
        if (n2 == 0) {
            return 0;
        }
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            n3 |= (byArray[n] & 0xFF) << (i2 << 3);
            ++n;
        }
        return n3;
    }

    static final int a(int n, int n2) {
        return n + Math.abs(a.nextInt()) % (n2 - n + 1);
    }

    static Object a(int n, int n2) {
        byte[] byArray = f.a(n);
        int n3 = 0;
        int n4 = f.a(byArray, 0);
        n3 += 2;
        if (n2 == 1) {
            byte[] byArray2 = new byte[n4];
            System.arraycopy(byArray, 2, byArray2, 0, byArray2.length);
            return byArray2;
        }
        if (n2 == 2) {
            short[] sArray = new short[n4];
            for (int i2 = 0; i2 < n4; ++i2) {
                sArray[i2] = f.a(byArray, n3);
                n3 += 2;
            }
            return sArray;
        }
        if (n2 == 4) {
            int[] nArray = new int[n4];
            for (int i3 = 0; i3 < n4; ++i3) {
                nArray[i3] = f.a(byArray, n3);
                n3 += 4;
            }
            return nArray;
        }
        return null;
    }

    static void a(Graphics graphics, int n, int n2, int n3, int n4, int n5, int n6) {
        if (d[0] != (n5 = n5 & 0xFFFFFF | n6 << 24)) {
            f.d[0] = n5;
            for (n5 = 1; n5 < 1024; n5 <<= 1) {
                System.arraycopy(d, 0, d, n5, n5);
            }
        }
        Graphics graphics2 = graphics;
        l = graphics2.getClipX();
        m = graphics2.getClipY();
        f.n = graphics2.getClipWidth();
        o = graphics2.getClipHeight();
        graphics.setClip(n, n2, n3, n4);
        int n7 = n;
        n6 = n2;
        n3 = n + n3;
        n2 += n4;
        while (n6 < n2) {
            while (n7 < n3) {
                graphics.drawRGB(d, 0, 32, n7, n6, 32, 32, true);
                n7 += 32;
            }
            n7 = n;
            n6 += 32;
        }
        Graphics graphics3 = graphics;
        graphics3.setClip(l, m, f.n, o);
    }

    static void b(int n) {
        if (b) {
            Display.getDisplay((MIDlet)a).vibrate(n);
        }
    }

    static int a(int n, int n2, int n3, int n4, int n5) {
        return (n * (n5 - n3) + n2 * n3) / n5;
    }

    /*
     * Unable to fully structure code
     */
    private static void g() {
        if (f.p >= 0) {
            f.a(f.p, true);
            f.p = -1;
        }
        if (f.q >= 0) {
            f.b(f.q, true);
            f.q = -1;
        }
        if (f.i) {
            f.a(true);
            f.i = false;
        }
        switch (f.b[f.d]) {
            case 1: {
                f.h();
                return;
            }
            case 0: {
                if ((f.w & 1) != 0) {
                    --f.d;
                    f.g();
                    ++f.d;
                }
                if ((f.u += 40) >= f.t) {
                    f.u = f.t;
                    f.a(false);
                }
                return;
            }
            case 2: {
                f.h();
                if (f.A >= 0) {
                    if (!f.a[0][4].c()) break;
                    f.A = -1;
                    break;
                }
                if (!f.b.a()) {
                    var1 = 0;
                    var0_2 = k.a[var1];
                    if (f.B != var0_2) {
                        f.i(var0_2);
                    }
                    if ((f.b & 4112) != 0) {
                        var1 = 0;
                        if (k.a[var1] > 0) {
                            var1 = 0;
                            var2_6 = k.a[var1] - 1;
                            var1 = 0;
                            k.a[var1] = var2_6;
                            f.a(3, 1, 1);
                            var1 = 0;
                            f.a[2][31].a(1, 39 + k.a[var1]);
                        }
                    } else if ((f.b & 8256) != 0) {
                        var1 = 0;
                        if (k.a[var1] < k.a[var1 = 1] && k.a[var1 = 0] < 3) {
                            var1 = 0;
                            var2_7 = k.a[var1] + 1;
                            var1 = 0;
                            k.a[var1] = var2_7;
                            f.a(3, 1, 1);
                            var1 = 0;
                            f.a[2][31].a(1, 39 + k.a[var1]);
                        }
                    } else if ((f.b & 147488) != 0) {
                        f.c(15);
                    }
                    if (var0_2 != k.a[var1 = 0]) {
                        var1 = 0;
                        var0_2 = k.a[var1];
                        f.b.a(0, 0, f.a[var0_2 << 1], 500, 0, 0, 0);
                        f.b.a(1, 0, f.a[(var0_2 << 1) + 1], 500, 0, 0, 0);
                    }
                }
                return;
            }
            case 3: {
                f.h();
                if ((f.b & 131072) != 0) {
                    f.a(4, false);
                }
                if (f.l && f.b.b != 2 && f.c.b != 2) {
                    f.c(26);
                    break;
                }
                var1_1 = 0;
                var0_3 = k.a[var1_1];
                if (var0_3 == 0 && f.b.e / 24 == 7 && f.b.f / 24 == 17 && k.a[var1_1 = 5] != 0 && f.b.b != 2) {
                    f.b.b = 2;
                    f.a[1].a(5);
                }
                if (var0_3 == 2 && f.b.e / 24 == 5 && f.b.f / 24 == 18 && f.b.b != 2) {
                    f.b.b = 2;
                    f.a[1].a(11);
                }
                if (f.b.b != 2) {
                    f.k = f.e.a[0] == 256;
                    f.b.a(f.k);
                    if ((f.b & 1) != 0) {
                        v0 = f.e.a[0] = f.k != false ? 257 : 256;
                    }
                }
                if (f.c.b == 1) {
                    f.c.d();
                }
                if (f.D != 0 || f.c.b == 2 || f.c.b == 0) ** GOTO lbl117
                var0_3 = Math.abs(f.b.e - f.c.e);
                var1_1 = Math.abs(f.b.f - f.c.f);
                if (var0_3 <= 120 && var1_1 <= 120) {
                    if ((b.i == 2 && f.b.e <= f.c.e || b.i == 3 && f.b.e >= f.c.e) && var1_1 <= var0_3 << 1) {
                        f.D = 1;
                    }
                    if ((b.i == 0 && f.b.f <= f.c.f || b.i == 1 && f.b.f >= f.c.f) && var0_3 <= var1_1 << 1) {
                        f.D = 1;
                    }
                }
                if (f.D <= 0 || var0_3 <= 0 && var1_1 <= 0) ** GOTO lbl117
                var0_3 = f.b.e / 24;
                var1_1 = f.b.f / 24;
                var2_8 = f.c.e / 24;
                var3_9 = f.c.f / 24;
                var4_10 = var0_3 - var2_8;
                var5_11 = var1_1 - var3_9;
                var6_12 = f.a.a;
                if (Math.abs(var4_10) < Math.abs(var5_11)) ** GOTO lbl110
                var7_13 = var4_10 < 0 ? -1 : 1;
                var8_15 = var2_8;
                while (var8_15 != var0_3) {
                    if (var6_12.a(var6_12.c, var8_15 += var7_13, var9_17 = var3_9 + (var8_15 - var2_8) * var5_11 / var4_10) < 101) continue;
                    f.D = 0;
                    ** GOTO lbl117
                }
                ** GOTO lbl117
lbl110:
                // 1 sources

                var7_14 = var5_11 < 0 ? -1 : 1;
                var8_16 = var3_9;
                while (var8_16 != var1_1) {
                    var9_18 = var2_8 + ((var8_16 += var7_14) - var3_9) * var4_10 / var5_11;
                    if (var6_12.a(var6_12.c, var9_18, var8_16) < 101) continue;
                    f.D = 0;
                    break;
                }
lbl117:
                // 6 sources

                if (f.D == 1) {
                    f.c.b = 2;
                    f.c.a(10, true, b.i);
                    if (f.e.a[0] == 257) {
                        f.b.b = 1;
                    }
                    if (f.e.a[0] != 4095) {
                        f.e.a(0, 4095);
                        var0_3 = (f.b.e + f.c.e) / 2 - 120;
                        var1_1 = (f.b.f + f.c.f) / 2 - 160;
                        f.e.a(0, 0, var0_3, 500, 0, 0, 0);
                        f.e.a(1, 0, var1_1, 500, 0, 0, 0);
                    }
                    f.c.a(0, 7, 0, 0, 0, 0, 0);
                    f.c.a(1, 7, 0, 0, 0, 0, 0);
                    f.a[2].a(255);
                    f.D = 2;
                }
                if (f.D > 0 && f.b.b != 2) {
                    f.c.b = 1;
                    b.g = b.i;
                    f.b.a(0, 7, 0, 0, 0, 0, 0);
                    f.b.a(1, 7, 0, 0, 0, 0, 0);
                    f.c(27);
                    f.a[1].a(255);
                    f.a(1, true);
                    var0_4 = f.a[2][5];
                    var1_1 = 2;
                    var0_4.a.a(11 + k.a[var1_1] - 1, false);
                    var0_4.c(100, 1000);
                }
                if (k.a[var1_1 = 8] == 0 && (var0_5 = f.a[2][7]) != null && !var0_5.b()) {
                    f.f(519);
                    f.f(518);
                }
                if ((f.b & 1) != 0) {
                    if (f.C == 13) {
                        f.c(26);
                    }
                    if (f.C == -13) {
                        f.c(27);
                        f.a(1, true);
                    }
                    f.C = 0;
                }
                if ((f.b & 32768) != 0) {
                    ++f.C;
                }
                if ((f.b & 65536) != 0) {
                    --f.C;
                }
                return;
            }
            case 4: {
                f.d.c();
            }
        }
    }

    private static void a(Graphics graphics) {
        switch (b[d]) {
            case 1: {
                f.c(graphics);
                return;
            }
            case 0: {
                if ((w & 2) != 0) {
                    --d;
                    f.a(graphics);
                    ++d;
                }
                int n = 0;
                n = v == 1 ? f.a(y, z, u, 0, t) : f.a(z, y, u, 0, t);
                f.a(graphics, 0, 0, 240, 320, x, n);
                return;
            }
            case 2: {
                f.c(graphics);
                return;
            }
            case 3: {
                f.c(graphics);
                return;
            }
            case 4: {
                graphics.drawImage(a, 0, 0, 0);
                d.a(graphics, false);
                f.b(graphics);
            }
        }
    }

    static void a(int n, boolean bl) {
        if (bl) {
            f.c(b[d], n);
            f.b(n, b[d]);
            f.b[f.d] = n;
            return;
        }
        p = n;
    }

    private static void b(int n, boolean bl) {
        if (bl) {
            if (d >= 0) {
                int n3 = n;
                n3 = b[d];
                switch (n3) {
                    case 1: {
                        break;
                    }
                    case 0: {
                        g = false;
                    }
                }
                f.b(n, b[d]);
            } else {
                f.b(n, -1);
            }
            f.b[++f.d] = n;
            return;
        }
        q = n;
    }

    private static void a(boolean bl) {
        if (bl) {
            int n = b[d - 1];
            f.c(b[d], n);
            int n3 = b[d];
            n3 = n;
            switch (n3) {
                case 1: {
                    break;
                }
                case 0: {
                    g = true;
                    g = 0;
                    b = 0;
                    a = 0;
                }
            }
            --d;
            return;
        }
        i = true;
    }

    private static void b(int n, int n2) {
        switch (n) {
            case 1: {
                n = n2;
                if (n == -1) {
                    a = new d[14];
                    a = Image.createImage((int)264, (int)360);
                    a = a.getGraphics();
                    a = new b[3][];
                    a = new int[3][];
                    e = new int[3];
                    for (n = 0; n < 3; ++n) {
                        f.a[n] = new b[32];
                        f.a[n] = new int[32];
                        f.e[n] = 0;
                    }
                    a = new b[6];
                    a = new k[3];
                    n = 0;
                    byte[] byArray = f.a(0);
                    n2 = 0;
                    k.a = f.a(byArray, 0) & 0xFFFF;
                    n2 += 2;
                    int n3 = f.a(byArray, 2) & 0xFFFF;
                    n2 += 2;
                    k.a = new byte[k.a][];
                    for (int i2 = 0; i2 < k.a; ++i2) {
                        int n4 = byArray[n2++] & 0xFF;
                        int n5 = 5;
                        k.a[i2] = new byte[n4 + 2];
                        k.a[i2][0] = (byte)n4;
                        for (int i3 = 0; i3 < n4; ++i3) {
                            k.a[i2][i3 + 2] = byArray[n2++];
                            n5 += k.a[i2][i3 + 2];
                        }
                        k.a[i2][1] = (byte)n5;
                    }
                    k.a = new int[n3];
                    k k2 = k.a(1);
                    n2 = 0;
                    f.a[n2] = k2;
                    a = (int[])f.a(2, 4);
                    f.a(3, 4);
                    byArray = f.a("CGTVQ");
                    if (byArray != null) {
                        int n6 = byArray[0];
                        n2 = 1;
                        k.a[n2] = n6;
                        b = byArray[1] != 0;
                    }
                }
                return;
            }
            case 0: {
                g = true;
                g = 0;
                b = 0;
                a = 0;
                return;
            }
            case 2: {
                a = (short[])f.a(1030, 2);
                n2 = 0;
                B = n = k.a[n2];
                b = a[0][6];
                b.b(a[n << 1], a[(n << 1) + 1]);
                for (n = 1; n < 4; ++n) {
                    n2 = 1;
                    if (n <= k.a[n2]) {
                        a[0][n].b(480, 640);
                        continue;
                    }
                    a[0][n].b(a[n << 1], a[(n << 1) + 1]);
                }
                if (A >= 0) {
                    a[0][4].b(a[A << 1], a[(A << 1) + 1]);
                } else {
                    a[0][4].b(480, 640);
                }
                n2 = 0;
                a[2][31].a(1, 39 + k.a[n2]);
                return;
            }
            case 3: {
                return;
            }
            case 4: {
                f.g(6);
                f.a(16, 17);
                f.d(4);
                f.a(3, 1, 1);
            }
        }
    }

    private static void c(int n, int n2) {
        switch (n) {
            case 1: {
                return;
            }
            case 0: {
                g = false;
                return;
            }
            case 2: {
                a = null;
                b = null;
                return;
            }
            case 3: {
                return;
            }
            case 4: {
                d = null;
            }
        }
    }

    static void a(int n, int n2) {
        if (n != 65535) {
            e = n;
            r = f.b(n);
        }
        if (n2 != 65535) {
            f = n2;
            s = f.b(n2);
            if (s != 65534) {
                ++s;
            }
        }
    }

    private static void b(Graphics graphics) {
        if (e != 65534) {
            a[1].a(graphics, r, 0, 320, 0);
        }
        if (f != 65534) {
            a[1].a(graphics, s, 240, 320, 0);
        }
    }

    private static int b(int n) {
        if (n == 16) {
            return 0;
        }
        if (n == 17) {
            return 2;
        }
        if (n == 18) {
            return 4;
        }
        if (n == 19) {
            return 6;
        }
        return 65534;
    }

    private static void a(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        v = n;
        t = n2;
        w = n3;
        x = -16777216;
        y = 0;
        z = 255;
        u = 0;
    }

    private static void h() {
        int n;
        int n2;
        for (n2 = 2; n2 >= 0; --n2) {
            if ((a = a[n2]) == null) continue;
            if (b != 0 && f.a.d > 0) {
                for (n = 0; n < f.a.d; n += 2) {
                    if ((b & f.a.c[n]) == 0 || (b & 0x20000) != 0 && e == 65534 || (b & 0x40000) != 0 && f == 65534) continue;
                    f.c(f.a.c[n + 1]);
                    f.a.d = 0;
                    break;
                }
            }
            while (!a.a() && f.a(a.a(), f.a.b, true, a.d())) {
                a.a();
            }
        }
        for (n2 = 0; n2 < 3; ++n2) {
            int n3 = e[n2];
            for (n = 0; n < n3; ++n) {
                a[n2][a[n2][n]].c();
            }
        }
    }

    private static void c(Graphics graphics) {
        int n;
        g g2;
        if (j) {
            graphics.drawImage(a, 0, 0, 0);
        }
        if ((g2 = a == null ? null : f.a.a) != null) {
            g2.a(graphics);
        }
        int n2 = g2 != null ? (g2.d >= 2 ? 1 : -1) : -1;
        boolean bl = e != null;
        for (int i2 = 0; i2 < 2; ++i2) {
            if (i2 == 1 && n2 >= 0) {
                int n3;
                int n4 = n2;
                boolean bl2 = bl;
                n = i2;
                Graphics graphics2 = graphics;
                int n5 = e[n];
                if (h[0] == -1) {
                    for (n3 = 0; n3 < n5; ++n3) {
                        f.h[n3] = n3;
                    }
                }
                for (n3 = 0; n3 < n5 - 1; ++n3) {
                    for (int i3 = n3; i3 < n5; ++i3) {
                        if (f.a[n][f.a[n][f.h[n3]]].f <= f.a[n][f.a[n][f.h[i3]]].f) continue;
                        int n6 = h[n3];
                        f.h[n3] = h[i3];
                        f.h[i3] = n6;
                    }
                }
                for (n3 = 0; n3 < n5; ++n3) {
                    b b2 = a[n][a[n][h[n3]]];
                    b2.a(graphics2, bl2);
                    if (n4 < 0 || !b2.a) continue;
                    f.a.a.a(graphics2, b2);
                }
                continue;
            }
            n = e[i2];
            for (int i4 = 0; i4 < n; ++i4) {
                a[i2][a[i2][i4]].a(graphics, bl);
            }
        }
        if (g2 != null) {
            g2.b(graphics);
        }
        n = e[2];
        for (int i5 = 0; i5 < n; ++i5) {
            a[2][a[2][i5]].a(graphics, false);
        }
        if (graphics != a) {
            f.b(graphics);
        }
    }

    private static boolean a(int n, int n2) {
        int n3 = n2;
        int n4 = k.a(n);
        while (n2 < n4) {
            f.f[n2] = k.a(n, n2);
            ++n2;
        }
        return f.a(n, f, false, n3);
    }

    private static boolean a(int n, int[] object, boolean n2, int n3) {
        switch (n) {
            case 0: {
                return true;
            }
            case 1: {
                a.a(object[0]);
                return true;
            }
            case 2: {
                if (f.a.c == null) {
                    f.a.c = new int[12];
                    f.a.d = 0;
                }
                if ((n2 = object[0]) == 0) {
                    f.a.d = 0;
                } else {
                    if (n2 == 1) {
                        f.a.c[f.a.d] = 147488;
                    }
                    if (n2 == 2) {
                        f.a.c[f.a.d] = 262144;
                    }
                    if (n2 == 3) {
                        f.a.c[f.a.d] = 1028;
                    }
                    if (n2 == 4) {
                        f.a.c[f.a.d] = 2304;
                    }
                    if (n2 == 5) {
                        f.a.c[f.a.d] = 4112;
                    }
                    if (n2 == 6) {
                        f.a.c[f.a.d] = 8256;
                    }
                    f.a.c[f.a.d + 1] = object[1];
                    f.a.d += 2;
                }
                return true;
            }
            case 3: {
                n2 = f.a(object, 0, n3 - 1);
                int n4 = a.b();
                while (n4 == 4 || n4 == 5) {
                    a.a();
                    k k2 = a;
                    object = k2.b;
                    n3 = a.d();
                    n2 = n4 == 4 ? (n2 &= f.a(object, 0, n3 - 1)) : (n2 |= f.a(object, 0, n3 - 1));
                    n4 = a.b();
                }
                if (n2 == 0) {
                    k k3 = a;
                    while (true) {
                        int n5;
                        if ((n5 = k3.a()) == 6 || n5 == 7) {
                            k3.b();
                            break;
                        }
                        k3.b += k3.c();
                    }
                }
                return true;
            }
            case 4: {
                return true;
            }
            case 5: {
                return true;
            }
            case 6: {
                k k4 = a;
                while (true) {
                    int n6;
                    if ((n6 = k4.a()) == 7) break;
                    k4.b += k4.c();
                }
                k4.b();
                return true;
            }
            case 7: {
                return true;
            }
            case 8: {
                n3 = 16;
                int n7 = object[0];
                n = object[1];
                f.a[n] = d.a(n7, 16);
                if (n2 != 0) {
                    a.a();
                }
                return false;
            }
            case 9: {
                f.a[object[0]] = null;
                return true;
            }
            case 10: {
                int n8 = object[0];
                n = object[1];
                try {
                    f.e(n);
                    byte[] byArray = f.a(n8);
                    f.a[n] = byArray;
                }
                catch (Exception exception) {}
                if (n2 != 0) {
                    a.a();
                }
                return false;
            }
            case 11: {
                f.e(object[0]);
                return true;
            }
            case 12: {
                f.a(object[0], object[0] >= 3 ? 1 : 0, object[1] == 1 ? -1 : 1);
                return true;
            }
            case 13: {
                f.a(0);
                return true;
            }
            case 14: {
                int n9 = object[0];
                n = object[1];
                byte[] byArray = f.a(n9);
                int n10 = 0;
                int n11 = f.a(byArray, 0) & 0xFFFF;
                n10 += 2;
                int[] nArray = new int[n11];
                for (n9 = 0; n9 < n11; ++n9) {
                    nArray[n9] = f.a(byArray, n10) & 0xFFFF;
                    n10 += 2;
                }
                f.a[n] = new byte[n11][];
                for (n9 = 0; n9 < n11; ++n9) {
                    f.a[n][n9] = new byte[nArray[n9]];
                    System.arraycopy(byArray, n10, a[n][n9], 0, nArray[n9]);
                    n10 += nArray[n9];
                }
                if (n2 != 0) {
                    a.a();
                }
                return false;
            }
            case 15: {
                n = object[0];
                if (n == -1) {
                    for (int i2 = 0; i2 < 2; ++i2) {
                        f.a[i2] = null;
                    }
                } else {
                    f.a[n] = null;
                }
                return true;
            }
            case 16: {
                c = object[0];
                return true;
            }
            case 17: {
                f.a.c += 40;
                if (f.a.c >= object[0]) {
                    f.a.c = 0;
                    return true;
                }
                return false;
            }
            case 18: {
                n2 = object[0] >> 8 & 0xFF;
                int n12 = object[0] & 0xFF;
                if (!f.b(n2, n12)) {
                    if (n2 < 3) {
                        for (n3 = 0; n3 < e[n2]; ++n3) {
                            b b2 = a[n2][a[n2][n3]];
                            if (b2.c()) continue;
                            return false;
                        }
                        return true;
                    }
                    for (n = 0; n < 3; ++n) {
                        for (n3 = 0; n3 < e[n]; ++n3) {
                            b b3 = a[n][a[n][n3]];
                            if (b3.c()) continue;
                            return false;
                        }
                    }
                    return true;
                }
                for (n = 0; n < 16; ++n) {
                    n2 = object[n] >> 8 & 0xFF;
                    n12 = object[n] & 0xFF;
                    if (!f.b(n2, n12)) {
                        return true;
                    }
                    b b4 = a[n2][n12];
                    if (b4 == null || b4.c()) continue;
                    return false;
                }
                return true;
            }
            case 19: {
                f.a.a = System.currentTimeMillis();
                return true;
            }
            case 20: {
                return System.currentTimeMillis() - f.a.a >= (long)object[0];
            }
            case 21: {
                f.a(0, object[0], object[1] == 1 ? 3 : 2, 0, 0, 0, 0, 255);
                f.b(0, true);
                if (n2 != 0) {
                    a.a();
                }
                return false;
            }
            case 22: {
                f.a(1, object[0], object[1] == 1 ? 3 : 2, 0, 0, 0, 0, 255);
                f.b(0, true);
                if (n2 != 0) {
                    a.a();
                }
                return false;
            }
            case 23: {
                if (n2 != 0) {
                    a.a();
                }
                return false;
            }
            case 24: {
                n2 = a == a[object[0]] ? 1 : 0;
                k k5 = k.a(object[1]);
                n = object[0];
                f.a[n] = k5;
                return n2 == 0;
            }
            case 25: {
                n2 = a == a[object[0]] ? 1 : 0;
                n = object[0];
                f.a[n] = null;
                return n2 == 0;
            }
            case 26: {
                return false;
            }
            case 27: {
                f.e();
                System.gc();
                if (n2 != 0) {
                    a.a();
                }
                return false;
            }
            case 28: {
                f.a(object[0], object[1]);
                return true;
            }
            case 29: {
                f.g(object[0]);
                return true;
            }
            case 30: {
                j = object[0] == 1;
                return true;
            }
            case 31: {
                n2 = object[0] >> 8 & 0xFF;
                int n13 = object[0] & 0xFF;
                if (f.b(n2, n13)) {
                    a[n2][n13].a();
                } else if (n2 >= 0 && n2 <= 3) {
                    for (n = 0; n < 32; ++n) {
                        if (a[n2][n] == null || f.a[n2][n].b != 0) continue;
                        a[n2][n].a();
                    }
                } else {
                    for (n = 0; n < 3; ++n) {
                        for (n3 = 0; n3 < 32; ++n3) {
                            if (a[n][n3] == null || f.a[n][n3].b != 0) continue;
                            a[n][n3].a();
                        }
                    }
                }
                return true;
            }
            case 32: {
                n2 = object[0] >> 8 & 0xFF;
                int n14 = object[0] & 0xFF;
                if (f.b(n2, n14)) {
                    a[n2][n14].b();
                } else if (n2 >= 0 && n2 <= 3) {
                    for (n = 0; n < 32; ++n) {
                        if (a[n2][n] == null || f.a[n2][n].b == 0) continue;
                        a[n2][n].b();
                    }
                } else {
                    for (n = 0; n < 3; ++n) {
                        for (n3 = 0; n3 < 32; ++n3) {
                            if (a[n][n3] == null || f.a[n][n3].b == 0) continue;
                            a[n][n3].b();
                        }
                    }
                }
                return true;
            }
            case 33: {
                a[object[0] >> 8 & 0xFF][object[0] & 0xFF].b((short)object[1], (short)object[2]);
                return true;
            }
            case 35: {
                a[object[0] >> 8 & 0xFF][object[0] & 0xFF].a(object[1], object[2]);
                if (b[d] == 3 && a == a[2] && object[0] == 513 && object[1] == 0) {
                    if (object[2] == 256) {
                        f.b.b = 1;
                    }
                    if (object[2] == 257) {
                        f.b.b = 2;
                        b.a(false);
                    }
                }
                return true;
            }
            case 34: {
                b b5 = a[object[0] >> 8 & 0xFF][object[0] & 0xFF];
                b5.a.a(object[1], object[2] == 1);
                b5.a.b = object[3];
                return true;
            }
            case 36: {
                f.f(object[0]);
                return true;
            }
            case 37: {
                f.a[object[1]] = a[object[0] >> 8 & 0xFF][object[0] & 0xFF];
                return true;
            }
            case 38: {
                f.a(object[1], 0, 1);
                b b6 = null;
                b6 = a[object[0]];
                if (b6.a == 6) {
                    b6.a.b = 0;
                }
                f.a[object[1] >> 8 & 0xFF][object[1] & 0xFF] = b6;
                return true;
            }
            case 39: {
                if (object[0] == 4095) {
                    for (n2 = 0; n2 < 6; ++n2) {
                        f.a[n2] = null;
                    }
                } else {
                    f.a[object[0]] = null;
                }
                return true;
            }
            case 40: {
                b b7 = f.a(object[0], 2, 8);
                b7.b(object[7], object[8]);
                b7.a(0, object[1]);
                b7.a(1, object[2]);
                b7.a(2, object[3]);
                b7.a(3, object[4]);
                b7.a(4, object[5]);
                b7.a(5, object[6]);
                b7.a(6, object[9]);
                b7.a(7, object[10]);
                return true;
            }
            case 41: {
                b b8 = f.a(object[0], 4, 2);
                f.a(object[0], 4, 2).a = g.a(object[1]);
                b8.b(object[4], object[5]);
                b8.a(0, object[2]);
                b8.a(1, object[3]);
                return true;
            }
            case 42: {
                b b9 = f.a(object[0], 6, 0);
                b9.b(object[2], object[3]);
                n = object[1];
                b9.a = new i((short[])f.a(n, 2));
                d d2 = a[object[5]];
                object = a[object[4]];
                i i3 = b9.a;
                b9.a.a = object;
                i3.b = d2;
                return true;
            }
            case 43: 
            case 44: 
            case 45: 
            case 46: {
                n2 = 0;
                int n15 = 0;
                if (n == 43) {
                    n2 = 3;
                    n15 = 6;
                } else if (n == 44) {
                    n2 = 0;
                    n15 = 5;
                } else if (n == 45) {
                    n2 = 1;
                    n15 = 8;
                } else if (n == 46) {
                    n2 = 5;
                    n15 = 5;
                }
                b b10 = f.a(object[0], n2, n15);
                b10.b(object[1], object[2]);
                for (n3 = 0; n3 < n15; ++n3) {
                    b10.a(n3, object[n3 + 3]);
                }
                return true;
            }
            case 47: {
                int n16;
                if (e != null) {
                    f.e.a = null;
                }
                if ((e = f.b(n2 = object[0] >> 8 & 0xFF, n16 = object[0] & 0xFF) ? a[n2][n16] : null) != null) {
                    if (f.e.b == 0) {
                        e.a();
                    }
                    if (a != null) {
                        f.e.a = f.a.a;
                        f.e.a.a();
                        e.b(0, 0, g.h, g.i);
                    }
                }
                return true;
            }
            case 48: {
                n2 = object[0] >> 8 & 0xFF;
                int n17 = object[0] & 0xFF;
                a = f.b(n2, n17) ? a[n2][n17] : null;
                if (a != null) {
                    if (f.a.b == 0) {
                        a.a();
                    }
                    if (e != null) {
                        f.e.a = f.a.a;
                        f.e.a.a();
                        e.b(0, 0, g.h, g.i);
                    }
                    f.a.a.a(0, a, a);
                }
                return true;
            }
            case 49: {
                a[object[0] >> 8 & 0xFF][object[0] & 0xFF].a(object[1], object[2], (short)object[3], object[4], object[5], object[6], object[7]);
                return true;
            }
            case 50: {
                a[object[0] >> 8 & 0xFF][object[0] & 0xFF].c(object[1], object[2]);
                return true;
            }
            case 51: {
                a[object[0] >> 8 & 0xFF][object[0] & 0xFF].a(object[1], object[2], object[3], object[4]);
                if (b[d] == 3 && object[0] == 513) {
                    f.b(object[4]);
                }
                return true;
            }
            case 52: {
                n3 = f.a(object, 1, n3 - 1, null);
                n = object[0];
                k.a[n] = n3;
                return true;
            }
            case 53: {
                f.a(28, 0);
                f.a(36, 0);
                f.a(2, 0);
                return true;
            }
            case 54: {
                f.a(object[0], false);
                if (n2 != 0) {
                    a.a();
                }
                return false;
            }
            case 55: {
                f.b();
                if (n2 != 0) {
                    a.a();
                }
                return false;
            }
            case 56: {
                f.a();
                return true;
            }
            case 57: {
                n = 0;
                n3 = k.a[n] + 1;
                n = 0;
                k.a[n] = n3;
                n = 0;
                int n18 = k.a[n];
                n = 1;
                if (n18 > k.a[n] && k.a[n = 1] < 3) {
                    n = 0;
                    n3 = A = k.a[n];
                    n = 1;
                    k.a[n] = n3;
                    f.a();
                }
                return true;
            }
            case 58: {
                n3 = object[2];
                int n19 = object[1];
                n = object[0];
                a[n >> 8 & 0xFF][n & 0xFF].b(n19 * 24 + 12, n3 * 24 + 12);
                return true;
            }
            case 59: {
                int[] nArray = new int[80];
                int n20 = 0;
                g g2 = f.a[2][0].a;
                for (n3 = 0; n3 < g2.b; ++n3) {
                    for (int i4 = 0; i4 < g2.a; ++i4) {
                        int n21 = g2.a(g2.c, i4, n3);
                        if (n21 < 0 || n21 >= 40) continue;
                        nArray[n21 << 1] = i4 * 24 + 12;
                        nArray[(n21 << 1) + 1] = n3 * 24 + 12;
                        if (n20 >= n21) continue;
                        n20 = n21;
                    }
                }
                b.b = new int[++n20 << 1];
                System.arraycopy(nArray, 0, b.b, 0, n20 << 1);
                c = a[1][1];
                a[1][1].a = new e();
                f.c.b = new e();
                b = a[1][0];
                a[1][0].a = new e();
                f.b.b = new e();
                f.b.a = true;
                f.c.a = true;
                int n22 = 0;
                if (k.a[n22] == 3) {
                    f.a[1][8].a = true;
                    f.a[1][9].a = true;
                }
                return true;
            }
            case 60: {
                f.b.a = f.a.a;
                f.c.a = f.a.a;
                f.h(0);
                b b11 = b;
                f.e.e = b11.e - 120;
                f.e.f = b11.f - 160;
                if (f.e.e < f.e.a[1]) {
                    f.e.e = f.e.a[1];
                }
                if (f.e.e > f.e.a[3]) {
                    f.e.e = f.e.a[3];
                }
                if (f.e.f < f.e.a[2]) {
                    f.e.f = f.e.a[2];
                }
                if (f.e.f > f.e.a[4]) {
                    f.e.f = f.e.a[4];
                }
                e.a(0, 256);
                b11 = a[2][5];
                n = 2;
                b11.a.a(1 + k.a[n] - 1, false);
                b.g = 1;
                f.h[0] = -1;
                l = false;
                D = 0;
                return true;
            }
            case 61: {
                b b12 = a[object[0] >> 8 & 0xFF][object[0] & 0xFF];
                b12.a(object[1], object[2] == 1, object[0] == 256 ? b.g : b.i);
                return true;
            }
            case 62: {
                b b13 = f.a(object[0], 7, 1);
                b13.a[0] = object[1];
                return true;
            }
            case 63: {
                b b14 = f.a(object[0], 8, 4);
                b14.a[0] = object[1];
                b14.a[1] = 0;
                b14.a[2] = 0;
                b14.a = new l(a[7]);
                b14.a.a(12, false);
                return true;
            }
            case 64: {
                b b15 = a[object[0] >> 8 & 0xFF][object[0] & 0xFF];
                a[object[0] >> 8 & 0xFF][object[0] & 0xFF].b = object[1];
                if (b15 == b && b15.b == 2) {
                    b15.a(0, 7, 0, 0, 0, 0, 0);
                    b15.a(1, 7, 0, 0, 0, 0, 0);
                }
                return true;
            }
            case 65: {
                f.b(object[0] * 10);
                return true;
            }
            case 66: {
                n2 = object[0];
                int n23 = object[1];
                n = f.b.e / 24;
                n3 = f.b.f / 24;
                if (!(b.g == 0 && n23 < n3 || b.g == 1 && n23 > n3 || b.g == 2 && n2 < n || b.g == 3 && n2 > n)) {
                    f.c(255);
                }
                return true;
            }
            case 67: {
                n2 = object[0];
                int n24 = (byte)object[1] * 24 + 12;
                n = (byte)object[2] * 24 + 12;
                b b16 = a[n2 >> 8 & 0xFF][n2 & 0xFF];
                if (n24 >= 0 && n24 != b16.e) {
                    b16.a(0, 1, n24, object[3], 0, 0, 0);
                }
                if (n >= 0 && n != b16.f) {
                    b16.a(1, 1, n, object[3], 0, 0, 0);
                }
                if (object[4] != 255) {
                    n3 = 0;
                    if (n > b16.f) {
                        n3 = 1;
                    }
                    if (n24 < b16.e) {
                        n3 = 2;
                    }
                    if (n24 > b16.e) {
                        n3 = 3;
                    }
                    if (n2 == 256) {
                        b.g = n3;
                    }
                    if (n2 == 257) {
                        b.i = n3;
                    }
                    b16.a(object[4], true, n3);
                }
                return true;
            }
            case 68: {
                f.a.a.a(object[0], object[1], object[2], object[3], object[4], object[5]);
                return true;
            }
            case 69: {
                n2 = object[0];
                int n25 = object[1];
                if (n2 == 256) {
                    b.g = n25;
                }
                if (n2 == 257) {
                    b.i = n25;
                }
                return true;
            }
            case 70: {
                f.h(object[0]);
                return true;
            }
            case 71: {
                n3 = 2;
                n = 3 + object[0];
                k.a[n] = n3;
                n2 = 0;
                for (int i5 = 0; i5 < 5; ++i5) {
                    n = i5 + 3;
                    if (k.a[n] != 2) continue;
                    ++n2;
                }
                b b17 = a[2][4];
                b17.a.a(n2 + 4, false);
                b17.c(100, 2000);
                if (n2 == 5) {
                    l = true;
                }
                return true;
            }
            case 72: {
                n3 = object[0];
                n = 8;
                k.a[n] = n3;
                if (object[0] == 0) {
                    a[2][7].c(100, 1000);
                    a[2][6].c(100, 1000);
                } else {
                    f.a(8, 1, 1);
                    b b18 = f.a(518, 0, 5);
                    b18.a(0, 2);
                    b18.a(1, 10);
                    b18.b(48, 370);
                    b18.a();
                    b18.a(1, 3, 320, 500, 0, 0, 0);
                    b b19 = a[object[3] >> 8 & 0xFF][object[3] & 0xFF];
                    b18 = f.a(519, 0, 5);
                    b18.a(0, object[1]);
                    b18.a(1, object[2]);
                    b18.b(b19.e - b.c, b19.f - b.d);
                    b18.a();
                    b18.a(0, 3, 48, 500, 0, 0, 0);
                    b18.a(1, 3, 315, 500, 0, 0, 0);
                    b19.b(-128, -128);
                }
                return true;
            }
            case 73: {
                n2 = object[0];
                if (n2 == 0) {
                    a[2].a(255);
                }
                return true;
            }
            case 74: {
                b b20 = a[object[0] >> 8 & 0xFF][object[0] & 0xFF];
                b20.a();
                b20.a.a(object[1], object[1] != 12);
                b20.a[2] = 0;
                b20.a[1] = object[2];
                b20.a[3] = b20.a[0] == 257 && f.a[1][1].a.a >= 18 && f.a[1][1].a.a <= 20 ? -64 : -48;
                return true;
            }
        }
        return true;
    }

    private static b a(int n, int n2, int n3) {
        int n4 = n >> 8 & 0xFF;
        n &= 0xFF;
        if (n4 == 1) {
            f.h[0] = -1;
        }
        f.a[n4][n] = new b(n2, n3);
        for (n2 = 0; n2 < e[n4]; ++n2) {
            if (a[n4][n2] == n) {
                return a[n4][n];
            }
            if (a[n4][n2] <= n) continue;
            for (n3 = e[n4]; n3 > n2; --n3) {
                f.a[n4][n3] = a[n4][n3 - 1];
            }
            f.a[n4][n2] = n;
            int n5 = n4;
            e[n5] = e[n5] + 1;
            return a[n4][n];
        }
        int n6 = n4;
        int n7 = e[n6];
        e[n6] = n7 + 1;
        f.a[n4][n7] = n;
        return a[n4][n];
    }

    private static void f(int n) {
        f.d(n >> 8 & 0xFF, n & 0xFF);
    }

    private static void d(int n, int n2) {
        if (f.b(n, n2)) {
            for (int i2 = 0; i2 < e[n]; ++i2) {
                if (a[n][i2] != n2) continue;
                int n3 = n;
                e[n3] = e[n3] - 1;
                for (int i3 = i2; i3 < e[n]; ++i3) {
                    f.a[n][i3] = a[n][i3 + 1];
                }
                break;
            }
            if (a[n][n2] == e) {
                e = null;
            }
            if (a[n][n2] == a) {
                a = null;
            }
            f.a[n][n2] = null;
        } else if (n >= 0 && n < 3) {
            for (int i4 = 0; i4 < 32; ++i4) {
                f.d(n, i4);
            }
        } else {
            for (int i5 = 0; i5 < 3; ++i5) {
                for (int i6 = 0; i6 < 32; ++i6) {
                    f.d(i5, i6);
                }
            }
        }
        f.h[0] = -1;
    }

    private static boolean b(int n, int n2) {
        return n >= 0 && n < 3 && n2 >= 0 && n2 < 32;
    }

    static void c(int n) {
        f.f[0] = n;
        f.a(1, 1);
    }

    private static int a(int[] nArray, int n, int n2, int[] nArray2) {
        int n3;
        int n4;
        int n5;
        int n6;
        if (n == n2) {
            return nArray[n];
        }
        int n7 = 0;
        if (nArray2 == null) {
            n6 = n2 + 1;
            n5 = 0;
            nArray2 = g;
            for (n4 = 0; n4 < n6; ++n4) {
                nArray2[n4] = 255;
            }
            for (n4 = n; n4 < n6; ++n4) {
                short s = (short)nArray[n4];
                n3 = s;
                if (s == -32766 || n3 == -32765) {
                    nArray2[n4] = n5;
                    continue;
                }
                if (n3 == -32764 || n3 == -32763) {
                    nArray2[n4] = n5 + 1;
                    continue;
                }
                if (n3 == Short.MIN_VALUE) {
                    n5 += 2;
                    continue;
                }
                if (n3 != -32767) continue;
                n5 -= 2;
            }
        }
        n3 = 254;
        n6 = 255;
        n5 = 255;
        for (n4 = n; n4 <= n2; ++n4) {
            if (nArray2[n4] <= n3) {
                n3 = nArray2[n4];
                n6 = n4;
                continue;
            }
            if (nArray2[n4] != 255 || nArray[n4] == Short.MIN_VALUE || nArray[n4] == -32767) continue;
            n5 = n4;
        }
        if (n3 == 254) {
            n7 = nArray[n5];
        } else {
            n4 = f.a(nArray, n, n6 - 1, nArray2);
            n = f.a(nArray, n6 + 1, n2, nArray2);
            short s = (short)nArray[n6];
            if (s == -32766) {
                n7 = n4 + n;
            }
            if (s == -32765) {
                n7 = n4 - n;
            }
            if (s == -32764) {
                n7 = n4 * n;
            }
            if (s == -32763) {
                n7 = n4 / n;
            }
        }
        return n7;
    }

    private static boolean a(int[] nArray, int n, int n2) {
        int n3;
        n = 255;
        short s = 0;
        for (n3 = 0; n3 <= n2; n3 += 1) {
            s = (short)nArray[n3];
            if (s != -32762 && s != -32761 && s != -32760 && s != -32759 && s != -32758 && s != -32757) continue;
            n = n3;
            break;
        }
        if (n == 255) {
            return false;
        }
        n3 = 0;
        int n4 = f.a(nArray, 0, n - 1, null);
        int n5 = f.a(nArray, n + 1, n2, null);
        if (s == -32762) {
            int n6 = n3 = n4 > n5 ? 1 : 0;
        }
        if (s == -32761) {
            int n7 = n3 = n4 >= n5 ? 1 : 0;
        }
        if (s == -32760) {
            int n8 = n3 = n4 < n5 ? 1 : 0;
        }
        if (s == -32759) {
            int n9 = n3 = n4 <= n5 ? 1 : 0;
        }
        if (s == -32758) {
            int n10 = n3 = n4 == n5 ? 1 : 0;
        }
        if (s == -32757) {
            n3 = n4 != n5 ? 1 : 0;
        }
        return n3 != 0;
    }

    private static void g(int n) {
        Object object;
        if (n == 0) {
            object = f.a(4);
            a.setColor(0xFFFFFF);
            a.fillRect(0, 0, 240, 320);
            a.drawImage((Image)object, 120, 160, 3);
        }
        if (n == 1) {
            object = f.a(512);
            a.setColor(6801708);
            a.fillRect(0, 0, 240, 320);
            a.drawImage((Image)object, 120, 160, 3);
        }
        if (n == 2) {
            object = f.a(768);
            a.drawImage((Image)object, 120, 160, 3);
        }
        if (n == 4) {
            a.setColor(6801708);
            a.fillRect(0, 0, 240, 320);
            object = f.a(512);
            a.drawImage((Image)object, 120, 65, 3);
            a[5].a(a, 1, 120, 210, 0);
        }
        if (n == 5) {
            object = f.a(1024);
            a.drawImage((Image)object, 0, 0, 0);
            int n2 = 0;
            f.i(k.a[n2]);
        }
        if (n == 6) {
            object = f.a.a;
            ((g)object).f = -1;
            ((g)object).e = -1;
            f.a.a.g = -1;
            Graphics graphics = a;
            f.c(graphics);
            int n3 = ((g)object).d >= 2 ? 1 : -1;
            if (n3 >= 0) {
                ((g)object).a(a, 1);
            }
            f.a(a, 0, 0, 240, 320, 0, 128);
            a[5].a(a, 0, 120, 140, 0);
            ((g)object).a(0, a, a);
        }
        if (n == 7) {
            a[1].a(a, false);
        }
        if (n == 8) {
            f.a(a, 0, 0, 240, 320, 0, 128);
            a[5].a(a, 1, 120, 160, 0);
        }
        if (n == 9 || n == 10) {
            a.setColor(0);
            a.fillRect(0, 0, 240, 320);
            object = f.a(n == 9 ? 2560 : 2561);
            a.drawImage((Image)object, 120, 160, 3);
        }
    }

    private static Image a(int n) {
        byte[] byArray = f.a(n);
        return Image.createImage((byte[])byArray, (int)0, (int)byArray.length);
    }

    static void a() {
        byte[] byArray = new byte[2];
        byte[] byArray2 = byArray;
        int n = 1;
        byArray[0] = (byte)k.a[n];
        byArray2[1] = (byte)(b ? 1 : 0);
        f.a("CGTVQ", byArray2);
    }

    private static void h(int n) {
        b b2 = c;
        b.h = n << 1;
        b2.b(b.b[b.h], b.b[b.h + 1]);
        b2.d();
    }

    private static void i(int n) {
        Image image = f.a(n + 1025);
        a.drawImage(image, 120, 125, 17);
        B = n;
    }

    static void b() {
        int n;
        b = null;
        c = null;
        b.b = null;
        for (n = 8; n < 14; ++n) {
            f.a[n] = null;
        }
        f.e(0);
        for (n = 13; n < 14; ++n) {
            f.e(13);
        }
        n = 1;
        while (n < 3) {
            int n2 = n++;
            f.a[n2] = null;
        }
    }

    static void d(int n) {
        d = a[n];
        a[n].a.b = 0;
        f.d.a.a(120, 160);
        d.b(120, 160);
    }

    static {
        g = false;
        j = 0;
        a = false;
        a = new byte[14][];
        a = new Player[2];
        h = false;
        a = new a();
        k = -1;
        b = null;
        a = null;
        a = new byte[2][][];
        c = 0;
        c = new int[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 58, 59, 60, 61, 62, 63, 64, 92, 32, 33, 35};
        a = new Random(System.currentTimeMillis());
        d = new int[1024];
        b = true;
        b = new int[4];
        d = -1;
        p = -1;
        q = -1;
        i = false;
        e = 65534;
        f = 65534;
        r = 65534;
        s = 65534;
        j = false;
        f = new int[16];
        g = new int[16];
        h = new int[32];
        A = -1;
        C = 0;
    }
}

