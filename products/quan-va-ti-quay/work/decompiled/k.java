/*
 * Decompiled with CFR 0.152.
 */
/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public final class k {
    static byte[][] a;
    static int a;
    static int[] a;
    int b;
    private byte[] a;
    int[] b = new int[16];
    long a = 0L;
    int c;
    int[] c = 0;
    int d;

    private k(byte[] byArray) {
        this.a = new byte[byArray.length - 2];
        System.arraycopy(byArray, 2, this.a, 0, this.a.length);
        this.b();
    }

    static k a(int n) {
        return new k(f.a(n));
    }

    final void a() {
        this.b += this.c();
        this.b();
    }

    final void a(int n) {
        this.b = 0;
        while (true) {
            int n2;
            if ((n2 = this.a()) == 0 && n == this.c(0)) {
                this.b();
                return;
            }
            this.b += this.c();
        }
    }

    final boolean a() {
        return this.b >= this.a.length;
    }

    final void b() {
        if (this.a()) {
            return;
        }
        int n = this.a();
        int n2 = k.a(n);
        for (int i2 = 0; i2 < n2; ++i2) {
            this.b[i2] = n == 52 && i2 == 0 ? this.b(i2) : this.c(i2);
        }
    }

    final int a() {
        return f.a(this.a, this.b) & 0xFFFF;
    }

    final int b() {
        int n = this.b;
        this.b += this.c();
        int n2 = this.a();
        this.b = n;
        return n2;
    }

    private int b(int n) {
        int n2 = this.a();
        if (n >= this.d()) {
            return k.a(n2, n);
        }
        int n3 = this.b + 5;
        for (int i2 = 0; i2 < n; ++i2) {
            n3 += k.b(n2, i2);
        }
        return f.a(this.a, n3, k.b(n2, n));
    }

    private int c(int n) {
        int n2 = this.b(n);
        if (!((f.a(this.a, this.b + 2, 2) & 1 << n) != 0) || (short)n2 <= -32757) {
            return n2;
        }
        return a[n2];
    }

    final int c() {
        int n = this.a();
        int n2 = this.d();
        if (n2 == k.a(n)) {
            int n3 = n;
            return a[n3][1] & 0xFF;
        }
        int n4 = this.b + 5;
        for (int i2 = 0; i2 < n2; ++i2) {
            n4 += k.b(n, i2);
        }
        return n4 - this.b;
    }

    final int d() {
        return this.a[this.b + 4] & 0xFF;
    }

    static int a(int n) {
        return a[n][0] & 0xFF;
    }

    private static int b(int n, int n2) {
        return a[n][n2 + 2] & 0xFF;
    }

    static int a(int n, int n2) {
        if (n == 40) {
            if (n2 == 6) {
                return 255;
            }
            if (n2 == 9) {
                return 240;
            }
            if (n2 == 10) {
                return 320;
            }
        }
        if (n == 18 || n == 46 && n2 == 3 || n == 47 && n2 == 0 || n == 48 && n2 == 0 || n == 36 && n2 == 0 || n == 31 && n2 == 0 || n == 32 && n2 == 0 || n == 39) {
            return 4095;
        }
        if (n == 11 && n2 == 0 || n == 13 && n2 == 0) {
            return -1;
        }
        if (n == 46) {
            if (n2 == 4) {
                return Short.MIN_VALUE;
            }
            if (n2 == 5) {
                return Short.MIN_VALUE;
            }
            if (n2 == 6) {
                return Short.MAX_VALUE;
            }
            if (n2 == 7) {
                return Short.MAX_VALUE;
            }
        }
        if (n == 43 && n2 == 8) {
            return 255;
        }
        if (n == 28) {
            return 65534;
        }
        if (n == 30) {
            return 1;
        }
        if (n == 68 && (n2 == 4 || n2 == 5)) {
            return 1;
        }
        if (n == 67 && n2 == 4) {
            return 255;
        }
        return 0;
    }
}

