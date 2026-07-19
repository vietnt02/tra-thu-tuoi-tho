/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 */
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public final class d {
    private int c;
    private int d;
    private int e;
    private int f;
    private byte[] c;
    private byte[] d;
    private byte[] e;
    private byte[] f;
    private byte[] g;
    private byte[] h;
    private byte[] i;
    private short[] b;
    byte[] a;
    private byte[] j;
    byte[] b;
    private byte[] k;
    private byte[] l;
    private byte[] m;
    short[] a;
    private int g;
    private int h;
    int a;
    private int i;
    private int j;
    private int[][] a;
    private byte[] n;
    private int[] a;
    private byte[] o;
    private int[] b;
    private static int[] c = new int[5120];
    private Image[][] a;
    private boolean a = false;
    private static int k;
    static int b;
    private static int[] d;

    private d(byte[] byArray, int n) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7 = 0;
        int n8 = byArray[0] & 0xFF;
        this.c = n8 | n;
        this.d = byArray[1] & 0xFF;
        this.e = byArray[2] & 0xFF;
        this.f = byArray[3] & 0xFF;
        this.c = new byte[this.d];
        this.d = new byte[this.d];
        System.arraycopy(byArray, 4, this.c, 0, this.d);
        n7 = 4 + this.d;
        System.arraycopy(byArray, n7, this.d, 0, this.d);
        int n9 = f.a(byArray, n7 += this.d) & 0xFFFF;
        n7 += 2;
        if (this.e > 0) {
            this.e = new byte[this.e];
            this.b = new short[this.e];
            System.arraycopy(byArray, n7, this.e, 0, this.e);
            n7 += this.e;
            n8 = 0;
            for (n = 0; n < this.e; ++n) {
                this.b[n] = (short)n8;
                n8 += this.e[n] & 0xFF;
            }
        }
        if (n9 > 0) {
            this.f = new byte[n9];
            this.g = new byte[n9];
            this.h = new byte[n9];
            this.i = new byte[n9];
            System.arraycopy(byArray, n7, this.f, 0, n9);
            n7 += n9;
            for (n = 0; n < n9; ++n) {
                this.h[n] = byArray[n7++];
                this.i[n] = byArray[n7++];
            }
            System.arraycopy(byArray, n7, this.g, 0, n9);
            n7 += n9;
        }
        n9 = f.a(byArray, n7) & 0xFFFF;
        n7 += 2;
        if (this.f > 0) {
            this.a = new byte[this.f];
            this.a = new short[this.f];
            System.arraycopy(byArray, n7, this.a, 0, this.f);
            n7 += this.f;
            n8 = 0;
            for (n = 0; n < this.f; ++n) {
                this.a[n] = (short)n8;
                n8 += this.a[n] & 0xFF;
            }
        }
        if (n9 > 0) {
            this.j = new byte[n9];
            this.b = new byte[n9];
            this.k = new byte[n9];
            this.l = new byte[n9];
            this.m = new byte[n9];
            System.arraycopy(byArray, n7, this.j, 0, n9);
            n7 += n9;
            for (n = 0; n < n9; ++n) {
                this.l[n] = byArray[n7++];
                this.m[n] = byArray[n7++];
            }
            System.arraycopy(byArray, n7, this.b, 0, n9);
            System.arraycopy(byArray, n7 += n9, this.k, 0, n9);
            n7 += n9;
        }
        n8 = byArray[n7++] & 0xFF;
        this.g = n8 >> 5 & 7;
        this.h = n8 & 0x1F;
        n9 = byArray[n7++] & 0xFF;
        this.a = new int[this.h][];
        for (n = 0; n < this.h; ++n) {
            this.a[n] = new int[n9];
        }
        if (this.g == 0) {
            for (n = 0; n < this.h; ++n) {
                for (n8 = 0; n8 < n9; ++n8) {
                    n6 = f.a(byArray, n7) & 0xFFFF;
                    n7 += 2;
                    n5 = (n6 & 0x8000) == 32768 ? 255 : 0;
                    n4 = ((n6 & 0x7C00) >> 10) * 255 / 31;
                    n3 = ((n6 & 0x3E0) >> 5) * 255 / 31;
                    n2 = (n6 & 0x1F) * 255 / 31;
                    this.a[n][n8] = n5 << 24 | n4 << 16 | n3 << 8 | n2;
                }
            }
        }
        if (this.g == 1) {
            for (n = 0; n < this.h; ++n) {
                for (n8 = 0; n8 < n9; ++n8) {
                    n6 = f.a(byArray, n7) & 0xFFFF;
                    n7 += 2;
                    n5 = ((n6 & 0xF000) >> 12) * 255 / 15;
                    n4 = ((n6 & 0xF00) >> 8) * 255 / 15;
                    n3 = ((n6 & 0xF0) >> 4) * 255 / 15;
                    n2 = (n6 & 0xF) * 255 / 15;
                    this.a[n][n8] = n5 << 24 | n4 << 16 | n3 << 8 | n2;
                }
            }
        }
        if (this.g == 2) {
            for (n = 0; n < this.h; ++n) {
                for (n8 = 0; n8 < n9; ++n8) {
                    this.a[n][n8] = f.a(byArray, n7);
                    n7 += 4;
                }
            }
        }
        if (this.g == 3) {
            for (n = 0; n < this.h; ++n) {
                for (n8 = 0; n8 < n9; ++n8) {
                    n6 = f.a(byArray, n7);
                    n7 += 4;
                    n4 = n6 >>> 24;
                    n3 = n6 >> 16 & 0xFF;
                    n2 = n6 >> 8 & 0xFF;
                    n5 = n6 & 0xFF;
                    this.a[n][n8] = n5 << 24 | n4 << 16 | n3 << 8 | n2;
                }
            }
        }
        this.i = 0;
        for (n8 = n9 - 1; n8 > 0; n8 >>= 1) {
            ++this.i;
        }
        this.j = (1 << this.i) - 1;
        n9 = 0;
        this.a = new int[this.d + 1];
        this.a[0] = 0;
        for (n = 1; n <= this.d; ++n) {
            n6 = f.a(byArray, n7) & 0xFFFF;
            n7 += 2;
            this.a[n] = n9 += n6 == 0 ? 65536 : n6;
        }
        this.n = new byte[n9];
        System.arraycopy(byArray, n7, this.n, 0, n9);
        n7 += n9;
        if ((this.c & 4) != 0) {
            n5 = 0;
            this.b = new int[this.d + 1];
            this.b[0] = 0;
            for (n = 1; n <= this.d; ++n) {
                this.b[n] = n5 += f.a(byArray, n7 += 2) & 0xFFFF;
            }
            this.o = new byte[n5];
            System.arraycopy(byArray, n7, this.o, 0, n5);
        }
        if ((this.c & 0x10) != 0) {
            for (n = 0; n < this.h; ++n) {
                this.a(n, 0, -1);
            }
            v0.a = null;
            this.n = null;
            this.a = null;
            this.o = null;
            this.b = null;
        }
    }

    static d a(int n, int n2) {
        return new d(f.a(n), n2);
    }

    private int[] a(int n, int n2) {
        int n3;
        int n4 = this.a[n2 + 1] & 0xFFFF;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int[] nArray = this.a[n];
        if (this.i == 8) {
            for (n3 = this.a[n2] & 0xFFFF; n3 < n4; ++n3) {
                d.c[n5] = nArray[this.n[n3] & 0xFF];
                ++n5;
            }
        } else if (this.i == 7) {
            while (n3 < n4) {
                if ((n6 = this.n[n3++]) >= 0) {
                    n7 = nArray[n6];
                    d.c[n5++] = n7;
                    continue;
                }
                while (n6 < 0) {
                    d.c[n5++] = n7;
                    ++n6;
                }
            }
        } else {
            while (n3 < n4) {
                n6 = this.n[n3++] & 0xFF;
                n7 = nArray[n6 & this.j];
                n6 >>= this.i;
                d.c[n5++] = n7;
                while (n6 > 0) {
                    d.c[n5++] = n7;
                    --n6;
                }
            }
        }
        if ((this.c & 4) != 0) {
            n3 = this.b[n2];
            n4 = this.b[n2 + 1];
            n5 = 0;
            while (n3 < n4) {
                if ((n6 = this.o[n3++]) >= 0) {
                    n7 = (n6 & 0x7F) << 25;
                    if (c[n5] != 0xFF00FF) {
                        d.c[n5] = c[n5] & 0xFFFFFF | n7;
                    }
                    ++n5;
                    continue;
                }
                while (n6 < 0) {
                    if (c[n5] != 0xFF00FF) {
                        d.c[n5] = c[n5] & 0xFFFFFF | n7;
                    }
                    ++n5;
                    ++n6;
                }
            }
        }
        return c;
    }

    final void a(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5 = this.b[n] & 0xFFFF;
        n = n5 + (this.e[n] & 0xFF);
        n4 &= 7;
        for (int i2 = n5; i2 < n; ++i2) {
            int n6 = this.f[i2] & 0xFF;
            n5 = this.h[i2];
            int n7 = this.i[i2];
            int n8 = this.g[i2] ^ n4;
            if ((n8 & 8) != 0) {
                n5 = (n4 & 2) != 0 ? n2 - n5 : (n5 += n2);
                n7 = (n4 & 1) != 0 ? n3 - n7 : (n7 += n3);
                this.a(graphics, n6, n5, n7, n8);
                continue;
            }
            n5 = (n4 & 2) != 0 ? n2 - n5 - (this.c[n6] & 0xFF) : (n5 += n2);
            n7 = (n4 & 1) != 0 ? n3 - n7 - (this.d[n6] & 0xFF) : (n7 += n3);
            this.b(graphics, n6, n5, n7, n8);
        }
    }

    final void a(Graphics graphics, int n, int n2, int n3, int n4, int n5) {
        n = (this.a[n] & 0xFFFF) + n2;
        n3 = ((n5 &= 7) & 2) != 0 ? (n3 -= this.l[n]) : (n3 += this.l[n]);
        n4 = (n5 & 1) != 0 ? (n4 -= this.m[n]) : (n4 += this.m[n]);
        this.a(graphics, this.j[n] & 0xFF, n3, n4, n5 ^ this.k[n]);
    }

    private void a(int n, int n2, int n3) {
        if (n3 == -1) {
            n3 = this.d - 1;
        }
        if (this.a == null) {
            this.a = new Image[this.h][];
        }
        if (this.a[n] == null) {
            this.a[n] = new Image[this.d];
        }
        while (n2 <= n3) {
            if (this.a[n][n2] == null) {
                this.a[n][n2] = Image.createRGBImage((int[])this.a(n, n2), (int)(this.c[n2] & 0xFF), (int)(this.d[n2] & 0xFF), (boolean)true);
            }
            ++n2;
        }
    }

    final void b(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5 = this.c[n] & 0xFF;
        int n6 = this.d[n] & 0xFF;
        if (this.a == null || this.a[this.a] == null || this.a[this.a][n] == null) {
            if ((this.c & 8) != 0) {
                this.a(this.a, n, n);
            } else {
                if (n2 + n5 < graphics.getClipX() || n2 > graphics.getClipX() + graphics.getClipWidth() || n3 + n6 < graphics.getClipY() || n3 > graphics.getClipY() + graphics.getClipHeight()) {
                    return;
                }
                graphics.drawRGB(this.a(this.a, n), 0, n5, n2, n3, n5, n6, true);
                return;
            }
        }
        graphics.drawRegion(this.a[this.a][n], 0, 0, n5, n6, n4 & 7, n2, n3, 0);
    }

    private int a(int n) {
        return (this.c[this.f[this.b[n] & 0xFFFF] & 0xFF] & 0xFF) - 1;
    }

    final int a() {
        return this.d[this.f[this.b[82] & 0xFFFF] & 0xFF] & 0xFF;
    }

    final void a(byte[] byArray, int n, int n2) {
        k = 0;
        b = this.a();
        int n3 = 0;
        if (n2 <= n) {
            n2 = byArray.length;
        }
        while (n < n2) {
            int n4 = byArray[n] & 0xFF;
            if (n4 == 81) {
                if (n < n2 - 1 && byArray[n + 1] == 49) {
                    if (n3 < k) {
                        n3 = k;
                    }
                    k = 0;
                    b += this.a();
                    n += 2;
                    continue;
                }
                if (n < n2 - 3 && byArray[n + 1] == 38) {
                    n += 4;
                    continue;
                }
            }
            k += this.a(n4);
            ++n;
        }
        if (k < n3) {
            k = n3;
        }
    }

    final int[] a(byte[] byArray, int[] nArray, int n, int n2, int n3) {
        if (nArray == null) {
            nArray = new int[61];
        }
        if (n <= 0) {
            n = 240;
        }
        if (n3 <= n2) {
            n3 = byArray.length;
        }
        int n4 = 1;
        int n5 = 1;
        int n6 = -1;
        int n7 = -1;
        int n8 = n2;
        nArray[1] = n2;
        nArray[3] = 0;
        while (n8 < n3) {
            n2 = byArray[n8] & 0xFF;
            if (n2 == 81 && n8 < n3 - 3 && byArray[n8 + 1] == 38) {
                n8 += 4;
                continue;
            }
            if (n2 == 81 && n8 < n3 - 1 && byArray[n8 + 1] == 49) {
                nArray[n4 + 1] = n8;
                nArray[n4 + 3] = n8 + 2;
                nArray[n4 + 5] = 0;
                n8 += 2;
                n4 += 3;
                ++n5;
                n6 = -1;
                continue;
            }
            if (n2 == 82) {
                n6 = n8++;
                n7 = nArray[n4 + 2];
                int n9 = n4 + 2;
                nArray[n9] = nArray[n9] + this.a(n2);
                continue;
            }
            int n10 = n4 + 2;
            nArray[n10] = nArray[n10] + this.a(n2);
            if (nArray[n4 + 2] > n && n6 >= 0) {
                n2 = nArray[n4 + 2] - n7 - this.a(82);
                nArray[n4 + 1] = n6;
                nArray[n4 + 2] = n7;
                ++n5;
                nArray[n4 += 3] = n6 + 1;
                nArray[n4 + 2] = n2;
                n6 = -1;
            }
            ++n8;
        }
        nArray[n4 + 1] = n8;
        nArray[0] = n5;
        return nArray;
    }

    final void a(Graphics graphics, byte[] byArray, int n, int n2, int n3) {
        this.a(graphics, byArray, n, n2, n3, 0, -1);
    }

    private void a(Graphics graphics, byte[] byArray, int n, int n2, int n3, int n4, int n5) {
        n2 += this.a();
        if (n3 != 0) {
            this.a(byArray, n4, n5);
            if ((n3 & 1) != 0) {
                n -= k >> 1;
            } else if ((n3 & 2) != 0) {
                n -= k;
            }
            if ((n3 & 4) != 0) {
                n2 -= b >> 1;
            } else if ((n3 & 8) != 0) {
                n2 -= b;
            }
        }
        n3 = n;
        if (n5 <= n4) {
            n5 = byArray.length;
        }
        while (n4 < n5) {
            int n6 = byArray[n4] & 0xFF;
            if (n6 == 81) {
                if (n4 < n5 - 3 && byArray[n4 + 1] == 38) {
                    n6 = byArray[n4 + 2];
                    byte by = byArray[n4 + 3];
                    this.a = n6 * 10 + by;
                    n4 += 4;
                    continue;
                }
                if (n4 < n5 - 1 && byArray[n4 + 1] == 49) {
                    n2 += this.a();
                    n = n3;
                    n4 += 2;
                    continue;
                }
            }
            this.a(graphics, n6, n, n2, 0);
            n += this.a(n6);
            ++n4;
        }
    }

    final boolean a(Graphics graphics, byte[] byArray, int n, int n2, int n3, int n4) {
        int n5 = -1;
        n5 = 0;
        n5 = -1;
        n5 = 0;
        n5 = -1;
        n4 = 240;
        d d2 = this;
        d2.a(byArray, d, n4, 0, -1);
        return d2.a(graphics, byArray, d, n, n2, n3, -1, 0, -1);
    }

    final boolean a(Graphics graphics, byte[] byArray, int[] nArray, int n, int n2, int n3, int n4, int n5, int n6) {
        if (n4 == 0) {
            return false;
        }
        int n7 = this.a();
        if (n4 < 0) {
            n4 = byArray.length;
        }
        if (n6 <= n5) {
            n6 = nArray[0];
        }
        int n8 = n6 - n5;
        if ((n3 & 4) != 0) {
            n2 -= n8 * n7 >> 1;
        } else if ((n3 & 8) != 0) {
            n2 -= n8 * n7;
        }
        n8 = this.a;
        int n9 = n5 * 3 + 1;
        for (int i2 = n5; i2 < n6; ++i2) {
            n5 = (n3 & 1) != 0 ? n - (nArray[n9 + 2] >> 1) : ((n3 & 2) != 0 ? n - nArray[n9 + 2] : n);
            if ((n4 -= nArray[n9 + 1] - nArray[n9]) <= 0) {
                this.a(graphics, byArray, n5, n2, 0, nArray[n9], nArray[n9 + 1] + n4);
                break;
            }
            this.a(graphics, byArray, n5, n2, 0, nArray[n9], nArray[n9 + 1]);
            n9 += 3;
            n2 += n7;
        }
        this.a = n8;
        return n4 > 0;
    }

    static {
        d = new int[61];
    }
}

