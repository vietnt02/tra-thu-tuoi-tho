/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Graphics
 */
import javax.microedition.lcdui.Graphics;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public final class b {
    int a;
    int b;
    l a;
    i a;
    g a;
    private m a;
    private h a;
    static int c = 0;
    static int d = 0;
    boolean a;
    int[] a;
    int e;
    int f;
    e a;
    e b;
    private c a;
    private j a = false;
    static int g;
    static int[] b;
    static int h;
    static int i;

    public b(int n, int n2) {
        this.a = n;
        this.b = 0;
        this.a = new int[n2];
    }

    final void a() {
        this.b = 1;
        if (this.a == 0) {
            d d2 = f.a[this.a[0]];
            if (this.a == null) {
                this.a = new l(d2);
            } else {
                this.a.a = d2;
            }
            this.a.a(this.a[1], this.a[2] == 1);
            this.a.b = this.a[3];
        }
        if (this.a == 1) {
            if (this.a == null) {
                this.a = new h();
            }
            if (this.a[7] == 0) {
                this.a.c = -1;
            }
            this.e();
        }
        if (this.a == 2) {
            if (this.a == null) {
                this.a = new m();
            }
            this.a.a = 0;
            this.a.b = 0;
        }
        if (this.a == 5) {
            if (this.a == null) {
                this.a = new e();
                this.a.d = this.e;
                this.a.a = true;
            }
            if (this.b == null) {
                this.b = new e();
                this.b.d = this.f;
                this.b.a = true;
            }
        }
    }

    final void b() {
        this.b = 0;
    }

    /*
     * Unable to fully structure code
     */
    final void c() {
        block87: {
            if (this.b == 0) {
                return;
            }
            var1_2 = this;
            if (var1_2.a != null && var1_2.a.a == false) {
                this.e = b.a(this.a);
            }
            var1_2 = this;
            if (var1_2.b != null && var1_2.b.a == false) {
                this.f = b.a(this.b);
            }
            if (this.b()) {
                var1_2 = this;
                var1_2.a.a += 40;
                if (var1_2.a.c > 0 && var1_2.a.a > var1_2.a.c) {
                    var1_2.a = null;
                }
            }
            if (!this.d()) break block87;
            var1_2 = this;
            if (var1_2.a.c <= 0) ** GOTO lbl-1000
            var1_2.a.a += 40;
            if (var1_2.a.a >= var1_2.a.c) {
                var1_2.a = null;
            } else lbl-1000:
            // 2 sources

            {
                switch (var1_2.a.d) {
                    case 0: 
                    case 1: {
                        var1_2.a.b += 40;
                        if (var1_2.a.b < 25) break;
                        var1_2.a.b = 0;
                        if (var1_2.a.d == 0) {
                            var1_2.a.g = f.a(-var1_2.a.e, var1_2.a.e);
                            var1_2.a.h = f.a(-var1_2.a.f, var1_2.a.f);
                            break;
                        }
                        var2_4 = f.a(var1_2.a.e, 0, var1_2.a.a, 0, var1_2.a.c);
                        var3_6 = f.a(var1_2.a.f, 0, var1_2.a.a, 0, var1_2.a.c);
                        var1_2.a.g = f.a(-var2_4, var2_4);
                        var1_2.a.h = f.a(-var3_6, var3_6);
                        break;
                    }
                    case 2: {
                        var1_2.a.g = f.a(var1_2.a.e, 0, var1_2.a.a, 0, var1_2.a.c);
                        var1_2.a.h = f.a(var1_2.a.f, 0, var1_2.a.a, 0, var1_2.a.c);
                        break;
                    }
                    case 3: 
                    case 4: {
                        var1_2.a.b += 40;
                        if (var1_2.a.b < 25) break;
                        var1_2.a.b = 0;
                        if (var1_2.a.d == 3) {
                            var2_4 = var1_2.a.e;
                            var3_6 = var1_2.a.f;
                        } else {
                            var2_4 = f.a(var1_2.a.e, 0, var1_2.a.a, 0, var1_2.a.c);
                            var3_6 = f.a(var1_2.a.f, 0, var1_2.a.a, 0, var1_2.a.c);
                        }
                        if (var1_2.a.g <= -var2_4 || var1_2.a.g == 0) {
                            var1_2.a.g = var2_4;
                        } else if (var1_2.a.g >= var2_4) {
                            var1_2.a.g = -var2_4;
                        }
                        if (var1_2.a.h <= -var3_6 || var1_2.a.h == 0) {
                            var1_2.a.h = var3_6;
                            break;
                        }
                        if (var1_2.a.h < var3_6) break;
                        var1_2.a.h = -var3_6;
                    }
                }
            }
        }
        switch (this.a) {
            case 0: {
                this.a.a();
                return;
            }
            case 1: {
                if (this.a[7] == 0) break;
                if ((f.b & 16416) != 0 && this.a.a && this.a[6] > 0 && this.a.b < this.a.a[0]) {
                    this.a.a = false;
                    this.a.c = 0;
                    this.a.a += this.a[6];
                    this.a.b += this.a[6];
                    if (this.a.b > this.a.a[0]) {
                        this.a.b = this.a.a[0];
                    }
                }
                this.a.d += 40;
                while (this.a.d > this.a[7]) {
                    this.a.d -= this.a[7];
                    ++this.a.c;
                }
                break;
            }
            case 2: {
                this.a.a += 40;
                if (this.a.a > this.a[1]) {
                    this.a.a = this.a[1];
                }
                if (this.a[0] == 0) {
                    this.a.b = f.a(this.a[5], 0, this.a.a, 0, this.a[1]);
                    return;
                }
                this.a.b = f.a(0, this.a[5], this.a.a, 0, this.a[1]);
                return;
            }
            case 3: 
            case 4: {
                return;
            }
            case 5: {
                if (!this.a()) {
                    var1_2 = this.a[0] == 4095 ? null : f.a[this.a[0] >> 8 & 255][this.a[0] & 255];
                    if (var1_2 != null) {
                        var2_4 = var1_2.e - 120;
                        var1_3 = var1_2.f - 160;
                        if (var2_4 < this.a[1]) {
                            var2_4 = this.a[1];
                        }
                        if (var2_4 > this.a[3]) {
                            var2_4 = this.a[3];
                        }
                        if (var1_3 < this.a[2]) {
                            var1_3 = this.a[2];
                        }
                        if (var1_3 > this.a[4]) {
                            var1_3 = this.a[4];
                        }
                        var3_6 = Math.abs(var2_4 - this.e);
                        var4_7 = Math.abs(var1_3 - this.f);
                        var5_8 = var3_6 == 0 ? 0 : 12;
                        v0 = var6_9 = var4_7 == 0 ? 0 : 12;
                        if (var3_6 < 20) {
                            var5_8 = f.a(0, 12, var3_6, 0, 20);
                        }
                        if (var4_7 < 20) {
                            var6_9 = f.a(0, 12, var4_7, 0, 20);
                        }
                        if (var3_6 > 0 && var4_7 > 0) {
                            if (var3_6 > var4_7) {
                                var6_9 = var6_9 * var4_7 / var3_6;
                            } else if (var3_6 < var4_7) {
                                var5_8 = var5_8 * var3_6 / var4_7;
                            }
                        }
                        if (this.a.d < var2_4) {
                            this.a.d += var5_8;
                            if (this.a.d >= var2_4) {
                                this.a.d = var2_4;
                            }
                        } else {
                            this.a.d -= var5_8;
                            if (this.a.d <= var2_4) {
                                this.a.d = var2_4;
                            }
                        }
                        if (this.b.d < var1_3) {
                            this.b.d += var6_9;
                            if (this.b.d >= var1_3) {
                                this.b.d = var1_3;
                            }
                        } else {
                            this.b.d -= var6_9;
                            if (this.b.d <= var1_3) {
                                this.b.d = var1_3;
                            }
                        }
                        this.e = this.a.d;
                        this.f = this.b.d;
                    }
                } else {
                    if (this.e < this.a[1]) {
                        this.a.d = this.e = this.a[1];
                        this.a.a = true;
                    }
                    if (this.e > this.a[3]) {
                        this.a.d = this.e = this.a[3];
                        this.a.a = true;
                    }
                    if (this.f < this.a[2]) {
                        this.b.d = this.f = this.a[2];
                        this.b.a = true;
                    }
                    if (this.f > this.a[4]) {
                        this.b.d = this.f = this.a[4];
                        this.b.a = true;
                    }
                }
                b.c = this.e;
                b.d = this.f;
                if (this.d()) {
                    b.c += this.a.g;
                    b.d += this.a.h;
                }
                if (this.a == null) break;
                this.a.a(-b.c, -b.d);
                return;
            }
            case 6: {
                this.a.a(this.e, this.f);
                if (this.a()) break;
                var1_2 = this.a;
                if (var1_2.a <= 0) break;
                if ((f.b & 1028) != 0) {
                    do {
                        if (var1_2.b <= 0) {
                            var1_2.b = var1_2.a - 1;
                            continue;
                        }
                        --var1_2.b;
                    } while (var1_2.a[var1_2.b] < 0);
                    f.a(3, 1, 1);
                }
                if ((f.b & 2304) != 0) {
                    do {
                        if (var1_2.b >= var1_2.a - 1) {
                            var1_2.b = 0;
                            continue;
                        }
                        ++var1_2.b;
                    } while (var1_2.a[var1_2.b] < 0);
                    f.a(3, 1, 1);
                }
                if ((f.b & 16416) == 0 && ((f.b & 131072) == 0 || f.e != 16)) ** GOTO lbl220
                var3_6 = var1_2.b;
                var2_5 = var1_2;
                var0_1 = var2_5.a[var3_6];
                var3_6 = var2_5.a[var3_6 + var2_5.a];
                if (var0_1 < 0) ** GOTO lbl220
                if (var2_5.g == 0) {
                    v1 = f.a = var0_1 == 3;
                }
                if (var2_5.g == 7) {
                    if (var0_1 == 12) {
                        if (!(f.a = f.a == false)) {
                            f.a(-1);
                        } else {
                            f.a(0, 0, -1);
                        }
                    } else if (f.b = f.b == false) {
                        f.b(200);
                    }
                }
                if (var2_5.g != 2) ** GOTO lbl203
                if (var0_1 == 3) {
                    f.c = true;
                } else {
                    if (f.b[f.d] == 4) {
                        f.d(4);
                    } else {
                        f.c(3);
                    }
lbl203:
                    // 3 sources

                    if (var2_5.g == 1) {
                        if (var0_1 == 3) {
                            f.b();
                            f.a(1, false);
                            f.c(25);
                        } else {
                            f.d(4);
                        }
                    }
                    if (var0_1 == 10) {
                        f.a(3, false);
                        f.a(18, 65534);
                    }
                    if (var3_6 >= 0) {
                        if (f.b[f.d] == 4) {
                            f.d(var3_6);
                        } else {
                            f.c(var3_6);
                        }
                    }
                    if (var2_5.g != 7) {
                        f.a(4, 1, 1);
                    }
                }
lbl220:
                // 6 sources

                if ((f.b & 262144) == 0 || f.f != 17) break;
                if (f.b[f.d] == 4) {
                    if (var1_2.g == 7) {
                        f.a();
                    }
                    if (f.d == f.a[4]) {
                        f.a(3, false);
                        f.a(18, 65534);
                    } else {
                        f.d(4);
                    }
                }
                f.a(3, 1, 1);
            }
        }
    }

    final void a(Graphics graphics, boolean bl) {
        b b3;
        block31: {
            block30: {
                if (b3.b == 0) break block30;
                b n = b3;
                if (!(n.a != null && n.a.a % (n.a.b << 1) >= n.a.b)) break block31;
            }
            return;
        }
        int i2 = b3.e;
        int n = b3.f;
        if (bl) {
            i2 -= c;
            n -= d;
        }
        if (b3.d()) {
            i2 += b3.a.g;
            n += b3.a.h;
        }
        switch (b3.a) {
            case 0: {
                b3.a.a.a = b3.a[4];
                b3.a.a(graphics, i2, n);
                return;
            }
            case 1: {
                d d2 = f.a[b3.a[3]];
                int n5 = f.c;
                f.c = b3.a[2];
                d2.a = b3.a[4];
                byte[] i3 = f.b(b3.a[1]);
                if (d2.a(graphics, i3, b3.a.a, i2, n, b3.a[0], b3.a.c, b3.a.a, b3.a.b)) {
                    b3.a.a = true;
                }
                f.c = n5;
                return;
            }
            case 2: {
                f.a(graphics, i2, n, b3.a[6], b3.a[7], 0xFF000000 | b3.a[2] << 16 | b3.a[3] << 8 | b3.a[4], b3.a.b);
                return;
            }
            case 3: {
                if (b3.a[5] == 255) {
                    graphics.setColor(b3.a[5] << 24 | b3.a[2] << 16 | b3.a[3] << 8 | b3.a[4]);
                    graphics.fillRect(i2, n, b3.a[0], b3.a[1]);
                    return;
                }
                f.a(graphics, i2, n, b3.a[0], b3.a[1], 0xFF000000 | b3.a[2] << 16 | b3.a[3] << 8 | b3.a[4], b3.a[5]);
                return;
            }
            case 4: {
                if (b3.a == f.a.a) break;
                b3.a.a(i2, n);
                b3.a.a(graphics);
                b3.a.b(graphics);
                return;
            }
            case 5: {
                return;
            }
            case 6: {
                b3 = graphics;
                i i3 = b3.a;
                int n4 = i3.e;
                int b4 = i3.a.a;
                n = 0;
                if ((i3.c & 1) != 0) {
                    n = 1;
                } else if ((i3.c & 2) != 0) {
                    n = 2;
                }
                if (i3.g >= 0) {
                    i3.b.a = 1;
                    i3.b.a((Graphics)b3, f.b(i3.g), i3.d, i3.e - i3.f, n | 8, 240);
                }
                int n2 = 0;
                for (int i4 = 0; i4 < i3.a; ++i4) {
                    if (i4 == i3.b) {
                        if (i3.a) {
                            n2 = n4;
                            n4 += i3.f;
                            continue;
                        }
                        i3.a.a = 1;
                    } else {
                        if (i3.a[i4] >= 0 && i3.a) {
                            f.a[2].a((Graphics)b3, 12, i3.d, n4 + 10, 0);
                        }
                        i3.a.a = 0;
                    }
                    i3.a.a((Graphics)b3, i3.a(i4), i3.d, n4, n);
                    n4 += i3.f;
                }
                if (i3.a) {
                    f.a[2].a((Graphics)b3, 11, i3.d, n2 + 10, 0);
                    i3.a.a = 1;
                    i3.a.a((Graphics)b3, i3.a(i3.b), i3.d, n2, n);
                }
                i3.a.a = b4;
                return;
            }
            case 7: {
                b b5 = f.a[b3.a[0] >> 8 & 0xFF][b3.a[0] & 0xFF];
                f.a[2].a(graphics, 10, b5.e - c, b5.f - d, 0);
                return;
            }
            case 8: {
                if (b3.a.b || b3.a.a == 12) {
                    b3.a[1] = 0;
                    b3.a[2] = 0;
                    b3.a.a(12, false);
                    v0.b = 0;
                    return;
                }
                b3.a[2] = b3.a[2] + 40;
                if (b3.a[2] >= b3.a[1]) {
                    b3.a[2] = 0;
                    b3.a[1] = 9999;
                    b3.a.a(13, false);
                }
                b b2 = f.a[b3.a[0] >> 8 & 0xFF][b3.a[0] & 0xFF];
                b3.a.a();
                b3.a.a(graphics, b2.e - c, b2.f - d + b3.a[3]);
            }
        }
    }

    final void a(int n, int n2) {
        if (this.a == 4 && (n == 0 || n == 1)) {
            d d2 = f.a[this.a[0]];
            f.a[this.a[0]].a = this.a[1];
            for (int i2 = 0; i2 < this.a.d; ++i2) {
                this.a.a(d2);
            }
        }
        this.a[n] = n2;
        if (this.b == 0) {
            return;
        }
        if (this.a == 0) {
            if (n == 0) {
                this.a.a = f.a[this.a[0]];
            } else if (n == 1 || n == 2) {
                this.a.a(this.a[1], this.a[2] == 1);
            } else if (n == 3) {
                this.a.b = this.a[3];
            }
        }
        if (this.a == 1 && (n == 1 || n == 2 || n == 3 || n == 5)) {
            this.e();
        }
        if (this.a == 5 && n == 0) {
            this.a.d = this.e;
            this.b.d = this.f;
        }
    }

    private void e() {
        int n = f.c;
        f.c = this.a[2];
        byte[] byArray = f.b(this.a[1]);
        d d2 = f.a[this.a[3]];
        if (this.a != null) {
            this.a.a = false;
        }
        this.a.a = d2.a(byArray, this.a.a, this.a[5], 0, 0);
        f.c = n;
        this.a.b = this.a[6];
        if (this.a.b == 0) {
            this.a.b = this.a.a[0];
        }
    }

    final void b(int n, int n2) {
        this.e = n;
        this.f = n2;
        if (this.a != null) {
            this.a.a = true;
        }
        if (this.b != null) {
            this.b.a = true;
        }
    }

    final boolean a() {
        return this.a != null && !this.a.a || this.b != null && !this.b.a;
    }

    final void a(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        if (n2 == 7) {
            if (n == 0 && this.a != null) {
                this.a.a = true;
            }
            if (n == 1 && this.b != null) {
                this.b.a = true;
            }
            return;
        }
        boolean bl = false;
        if (n == 2) {
            n = 0;
            bl = true;
        }
        if (n == 3) {
            n = 1;
            bl = true;
        }
        e e2 = null;
        int n8 = 0;
        if (n == 0) {
            if (this.a == null) {
                this.a = new e();
            }
            e2 = this.a;
            n8 = this.e;
        }
        if (n == 1) {
            if (this.b == null) {
                this.b = new e();
            }
            e2 = this.b;
            n8 = this.f;
        }
        e2.a = n2;
        e2.b = n3;
        e2.c = 0;
        e2.d = n8;
        e2.h = 0;
        e2.g = 0;
        e2.f = 0;
        e2.e = 0;
        e2.a = false;
        switch (n2) {
            case 0: {
                e2.e = n4;
                break;
            }
            case 1: {
                e2.e = n4;
                break;
            }
            case 2: {
                e2.e = (short)n4;
                e2.f = (short)n5;
                e2.g = n6;
                e2.h = n8;
                break;
            }
            case 3: {
                e2.e = n4;
                break;
            }
            case 4: {
                e2.e = (short)n4;
                e2.f = n5;
                break;
            }
            case 5: {
                e2.e = (short)n4;
                e2.f = (short)n5;
                e2.g = n6;
                e2.h = n8;
                break;
            }
            case 6: {
                e2.e = n4;
                e2.f = (short)n5;
                e2.g = (short)n6;
                e2.h = n7;
            }
        }
        if (bl) {
            e2.b += n8;
        }
    }

    private static int a(e e2) {
        switch (e2.a) {
            case 0: {
                e2.c += 40;
                if (e2.c >= e2.e) {
                    e2.a = true;
                    return e2.b;
                }
                return f.a(e2.d, e2.b, e2.c, 0, e2.e);
            }
            case 1: {
                e2.c += 40;
                int n = e2.e * e2.c / 1000;
                if (n >= Math.abs(e2.b - e2.d)) {
                    e2.a = true;
                    return e2.b;
                }
                if (e2.d > e2.b) {
                    return e2.d - n;
                }
                return e2.d + n;
            }
            case 2: {
                e2.c += 40;
                int n = e2.h + e2.e * e2.c / 1000 + e2.f * e2.c * e2.c / 2000000;
                if (e2.g > 0 && e2.c >= e2.g) {
                    e2.b = n;
                    e2.a = true;
                    return e2.b;
                }
                return n;
            }
            case 3: {
                e2.c += 40;
                if (e2.c >= e2.e) {
                    e2.a = true;
                    return e2.b;
                }
                int n = f.a(0, 90, e2.c, 0, e2.e);
                return f.a(e2.d, e2.b, f.a[n % 360], 0, 256);
            }
            case 4: {
                if (Math.abs(e2.b - e2.d) <= 1) {
                    e2.a = true;
                    return e2.b;
                }
                int n = (e2.b - e2.d) / e2.f;
                int n2 = 40 * e2.e / 1000;
                if (n < -n2) {
                    n = -n2;
                }
                if (n > n2) {
                    n = n2;
                }
                e2.d += n;
                return e2.d;
            }
            case 5: {
                e2.c += 40;
                int n = e2.h + e2.e * e2.c / 1000 + e2.f * e2.c * e2.c / 2000000;
                if (e2.f > 0 && n > e2.b || e2.f < 0 && n < e2.b) {
                    e2.e = -((e2.e + e2.f * e2.c / 1000) * 3 / 4);
                    --e2.g;
                    e2.h = e2.b;
                    e2.c = 0;
                    if (e2.g <= 0 || Math.abs(e2.e) <= 1) {
                        e2.a = true;
                    }
                    return e2.b;
                }
                return n;
            }
            case 6: {
                int n = e2.e;
                e2.c += 40;
                if (e2.h > 0) {
                    if (e2.c >= e2.h) {
                        e2.a = true;
                        return e2.b;
                    }
                    n = f.a(e2.e, 0, e2.c, 0, e2.h);
                }
                return e2.b + (n * f.a[(e2.f * e2.c / 1000 + e2.g) % 360] >> 8);
            }
        }
        return 0;
    }

    final boolean b() {
        return this.a != null;
    }

    final void c(int n, int n2) {
        if (this.a == null) {
            this.a = new c();
        }
        this.a.a = 0;
        this.a.b = n;
        this.a.c = n2;
    }

    private boolean d() {
        return this.a != null;
    }

    final void a(int n, int n2, int n3, int n4) {
        if (n != 0 && n != 3 && n4 == 0) {
            this.a = null;
            return;
        }
        if (this.a == null) {
            this.a = new j();
        }
        this.a.d = n;
        this.a.e = n2;
        this.a.f = n3;
        this.a.c = n4;
    }

    final void b(int n, int n2, int n3, int n4) {
        this.a[1] = 0;
        this.a[2] = 0;
        this.a[3] = n3;
        this.a[4] = n4;
    }

    final boolean c() {
        if (this.a() || this.b() || this.d()) {
            return false;
        }
        switch (this.a) {
            case 0: {
                return this.a == null || this.a.a || this.a.b;
            }
            case 1: {
                return this.a[7] == 0 || this.a == null || this.a.b >= this.a.a[0] && this.a.a;
            }
            case 2: {
                return this.a == null || this.a.a >= this.a[1];
            }
        }
        return true;
    }

    final void a(boolean bl) {
        int n;
        this.a.h = this.e / 24;
        this.b.h = this.f / 24;
        if (this.b.a) {
            if ((f.a & 0x1010) != 0 && bl) {
                if (this.a.a || this.a.b >= this.e) {
                    g = 2;
                    n = this.a(-1, 0);
                    if (n != this.e) {
                        this.a(0, 1, n, 96, 0, 0, 0);
                        this.a(3, true, g);
                    }
                }
            } else if (!this.a.a && this.a.b < this.e) {
                n = this.a.h * 24 + 12;
                this.a.b = n < this.e ? n : n - 24;
            } else if ((f.a & 0x2040) != 0 && bl) {
                if (this.a.a || this.a.b <= this.e) {
                    g = 3;
                    n = this.a(1, 0);
                    if (n != this.e) {
                        this.a(0, 1, n, 96, 0, 0, 0);
                        this.a(3, true, g);
                    }
                }
            } else if (!this.a.a && this.a.b > this.e) {
                n = this.a.h * 24 + 12;
                int n2 = this.a.b = n > this.e ? n : n + 24;
            }
        }
        if (this.a.a) {
            if ((f.a & 0x404) != 0 && bl) {
                if (this.b.a || this.b.b >= this.f) {
                    g = 0;
                    n = this.a(0, -1);
                    if (n != this.f) {
                        this.a(1, 1, n, 96, 0, 0, 0);
                        this.a(3, true, g);
                    }
                }
            } else if (!this.b.a && this.b.b < this.f) {
                n = this.b.h * 24 + 12;
                this.b.b = n < this.f ? n : n - 24;
            } else if ((f.a & 0x900) != 0 && bl) {
                if (this.b.a || this.b.b <= this.f) {
                    g = 1;
                    n = this.a(0, 1);
                    if (n != this.f) {
                        this.a(1, 1, n, 96, 0, 0, 0);
                        this.a(3, true, g);
                    }
                }
            } else if (!this.b.a && this.b.b > this.f) {
                n = this.b.h * 24 + 12;
                int n3 = this.b.b = n > this.f ? n : n + 24;
            }
        }
        if (this.a.a && this.b.a) {
            if ((f.b & 0x4020) != 0 && bl) {
                n = this.a.a(this.a.c, this.a.h, this.b.h);
                n = n >= 90 && n <= 99 ? (n -= 90) : (n >= 40 && n <= 49 ? n - 40 + 10 : -1);
                if (n == -1 || n < 5 && k.a[bl = n + 3] != 0) {
                    g = 1;
                    this.a.a(f.a(0, 1) == 0 ? 14 : 6, false);
                    f.a[1].a(254);
                    return;
                }
                f.a[1].a(n);
                return;
            }
            if (g == 3 && this.a.a != 2 || this.a.a != 0 + g) {
                this.a(0, true, g);
            }
        }
    }

    private int a(int n, int n2) {
        int n3;
        int n4 = f.c.e / 24;
        int n5 = f.c.f / 24;
        if (n != 0) {
            int n6;
            n = n < 0 ? -1 : 1;
            int n7 = n6 = n < 0 ? 0 : this.a.a - 1;
            for (int i2 = this.a.h; i2 != n6; i2 += n) {
                int n8 = this.a.a(this.a.c, i2 + n, this.b.h);
                if ((n8 == -1 || n8 < 100) && (n4 - n != i2 || n5 != this.b.h)) continue;
                return i2 * 24 + 12;
            }
            return n6 * 24 + 12;
        }
        n2 = n2 < 0 ? -1 : 1;
        int n9 = n3 = n2 < 0 ? 0 : this.a.b - 1;
        for (int i3 = this.b.h; i3 != n3; i3 += n2) {
            int n10 = this.a.a(this.a.c, this.a.h, i3 + n2);
            if ((n10 == -1 || n10 < 100) && (n4 != this.a.h || n5 - n2 != i3)) continue;
            return i3 * 24 + 12;
        }
        return n3 * 24 + 12;
    }

    final void a(int n, boolean bl, int n2) {
        if (n2 >= 0) {
            if (n2 == 3) {
                this.a.b = 2;
                n2 = 2;
            } else {
                this.a.b = 0;
            }
            this.a.a(n + n2, bl);
            return;
        }
        this.a.a(n, bl);
    }

    final void d() {
        int n;
        int n2;
        this.a.h = this.e / 24;
        this.b.h = this.f / 24;
        if (this.a != null && (n2 = this.a.a(this.a.c, this.a.h, this.b.h) - 50) >= 0 && n2 < 40 && f.b.b != 2) {
            n = n2;
            int n3 = 0;
            if ((n3 = k.a[n3]) != 0 || n != 1 || i != 3) {
                f.a[2].a(n2);
            }
        }
        if (this.a.a && this.b.a) {
            if ((h += 2) >= b.length) {
                h = 2;
            }
            n2 = b[h];
            n = b[h + 1];
            if (n2 != this.e) {
                this.a(0, 1, n2, 48, 0, 0, 0);
                int n4 = i = n2 < this.e ? 2 : 3;
            }
            if (n != this.f) {
                this.a(1, 1, n, 48, 0, 0, 0);
                i = n < this.f ? 0 : 1;
            }
            this.a(3, true, i);
        }
    }
}

