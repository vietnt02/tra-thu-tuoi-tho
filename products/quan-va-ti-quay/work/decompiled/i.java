/*
 * Decompiled with CFR 0.152.
 */
/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public final class i {
    int a;
    int b;
    int c;
    int d;
    int e;
    int f;
    int g;
    short[] a;
    d a;
    d b;
    boolean a;

    public i(short[] sArray) {
        this.g = sArray[0];
        this.c = sArray[1];
        this.a = sArray[2];
        this.b = 0;
        this.a = f.a[0];
        this.b = f.a[0];
        this.a = new short[this.a << 1];
        for (int i2 = 0; i2 < this.a; ++i2) {
            this.a[i2] = sArray[3 + (i2 << 1)];
            this.a[i2 + this.a] = sArray[3 + (i2 << 1) + 1];
        }
        this.f = this.a.a();
    }

    final void a(int n, int n2) {
        this.a = f.b[f.d] != 4 && this.g != 0;
        this.f = this.a ? 17 : this.a.a();
        this.d = n;
        i i2 = this;
        n = n2;
        n2 = this.f * this.a;
        if (this.g >= 0) {
            this.b.a(f.b(this.g), 0, 0);
            n2 += this.f + d.b;
        }
        if ((this.c & 4) != 0) {
            n -= n2 >> 1;
        } else if ((this.c & 8) != 0) {
            n -= n2;
        }
        if (this.g >= 0) {
            n += this.f + d.b;
        }
        i2.e = n;
    }

    final byte[] a(int n) {
        if (this.g == 7) {
            if (this.a[n] == 12) {
                return f.b(f.a ? 12 : 13);
            }
            if (this.a[n] == 14) {
                return f.b(f.b ? 14 : 15);
            }
        }
        return f.b(Math.abs(this.a[n]));
    }
}

