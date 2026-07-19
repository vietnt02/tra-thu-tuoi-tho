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
public final class g {
    private byte[][] a;
    private byte[][] b;
    private int[][] a;
    private int[] a;
    private int j = 0;
    private int k = 0;
    private int l = 0;
    private int m = 0;
    int a;
    int b = 0;
    private int n;
    int c;
    int d;
    private d a;
    private int o;
    private int p;
    int e;
    int f;
    int g = -1;
    private Graphics a;
    private Image a = 0;
    static int h;
    static int i;
    private static int[] b;

    private g(byte[] byArray) {
        int n;
        int n2 = 0;
        this.n = -1;
        this.c = -1;
        ++n2;
        this.d = byArray[0];
        this.a = new byte[this.d][];
        this.a = new int[this.d][];
        for (n = 0; n < this.d; ++n) {
            this.a[n] = new int[7];
        }
        this.b = new byte[this.d][];
        this.a = new int[this.d << 1];
        for (int i2 = 0; i2 < this.d; ++i2) {
            for (n = 0; n < 7; ++n) {
                this.a[i2][n] = byArray[n2++] & 0xFF;
            }
            if ((n = byArray[n2++] & 0xFF) > 0) {
                n2 += n;
            }
            n = this.a[i2][0] * this.a[i2][1];
            this.a[i2] = new byte[n];
            System.arraycopy(byArray, n2, this.a[i2], 0, n);
            n2 += n;
            if ((this.a[i2][6] & 1) == 0) {
                this.b[i2] = new byte[n];
                System.arraycopy(byArray, n2, this.b[i2], 0, n);
                n2 += n;
            }
            if ((this.a[i2][6] & 2) != 0) {
                this.n = i2;
            }
            if ((this.a[i2][6] & 1) == 0) continue;
            this.c = i2;
        }
        this.a = this.a[this.n][0];
        this.b = this.a[this.n][1];
        this.l = this.a * 24;
        this.m = this.b * 24;
        if (this.c >= 0) {
            --this.d;
        }
        this.a(0, 0);
    }

    static g a(int n) {
        return new g(f.a(n));
    }

    final void a(d d2) {
        this.a = d2;
    }

    final void a(int n, int n2) {
        int n3;
        this.j = n;
        this.k = n2;
        n2 = this.a[this.n][2];
        int n4 = this.a[this.n][3];
        for (n3 = 0; n3 < this.d; ++n3) {
            n = n3 << 1;
            if (n3 == this.n) {
                this.a[n] = this.j;
                this.a[n + 1] = this.k;
                continue;
            }
            this.a[n] = this.a[n3][4] * 24 + this.j * this.a[n3][2] / n2;
            this.a[n + 1] = this.a[n3][5] * 24 + this.k * this.a[n3][3] / n4;
        }
        if (this.g >= 0) {
            n = this.g << 1;
            this.o = this.a[n] % 264;
            this.p = this.a[n + 1] % 360;
            n3 = -this.a[n] / 24;
            n = -this.a[n + 1] / 24;
            if (n3 != this.e || n != this.f) {
                if (this.e == -1 && this.f == -1) {
                    n2 = n3;
                    n4 = n3 + 11;
                    int n5 = n2 % 11;
                    int n6 = n;
                    int n7 = n + 15;
                    int n8 = n6 % 15;
                    this.b(n2, n4, n5, n6, n7, n8);
                } else {
                    int n9;
                    if (n3 != this.e) {
                        if (n3 > this.e) {
                            n2 = this.e + 11;
                            n4 = n3 + 11;
                            n9 = n2 % 11;
                        } else {
                            n2 = n3;
                            n4 = this.e;
                            n9 = n2 % 11;
                        }
                        this.b(n2, n4, n9, n, n + 15, n % 15);
                    }
                    if (n != this.f) {
                        if (n > this.f) {
                            n2 = this.f + 15;
                            n4 = n + 15;
                            n9 = n2 % 15;
                        } else {
                            n2 = n;
                            n4 = this.f;
                            n9 = n2 % 15;
                        }
                        this.b(n3, n3 + 11, n3 % 11, n2, n4, n9);
                    }
                }
                this.e = n3;
                this.f = n;
            }
        }
    }

    final void a(Graphics graphics) {
        if (0 == this.g) {
            int n = this.o;
            int n2 = this.p;
            graphics.drawImage(this.a, n, n2, 0);
            graphics.drawImage(this.a, n + 264, n2, 0);
            graphics.drawImage(this.a, n, n2 + 360, 0);
            graphics.drawImage(this.a, n + 264, n2 + 360, 0);
            return;
        }
        this.a(graphics, 0);
    }

    final void b(Graphics graphics) {
        for (int i2 = 2; i2 < this.d; ++i2) {
            this.a(graphics, i2);
        }
    }

    final void a(Graphics graphics, int n) {
        int n2 = n << 1;
        int n3 = n2 + 1;
        int n4 = -this.a[n2] / 24;
        int n5 = -this.a[n3] / 24;
        if (n4 < 0) {
            n4 = 0;
        }
        if (n5 < 0) {
            n5 = 0;
        }
        int n6 = n4 * 24 + this.a[n2];
        int n7 = n5 * 24 + this.a[n3];
        n4 += n5 * this.a[n][0];
        n5 = 240;
        int n8 = 320;
        int n9 = this.a[n][0];
        if ((240 - this.a[n2]) / 24 >= n9) {
            n5 = this.a[n2] + n9 * 24;
        }
        if ((320 - this.a[n3]) / 24 >= this.a[n][1]) {
            n8 = this.a[n3] + this.a[n][1] * 24;
        }
        while (n7 < n8) {
            n3 = n6;
            n2 = n4;
            while (n3 < n5) {
                byte by = this.a[n][n2];
                if (by != -1) {
                    this.a.b(graphics, by, n3, n7, this.b[n][n2]);
                }
                n3 += 24;
                ++n2;
            }
            n7 += 24;
            n4 += n9;
        }
    }

    final void a() {
        h = this.l - 240 - 1;
        i = this.m - 320 - 1;
        if (h < 0) {
            h = 0;
        }
        if (i < 0) {
            i = 0;
        }
    }

    final int a(int n, int n2, int n3) {
        return this.a[n][n2 + n3 * this.a[n][0]];
    }

    final void a(int n, int n2, int n3, int n4, int n5, int n6) {
        n5 += n3;
        n6 += n4;
        int n7 = this.a[n][0];
        while (n4 < n6) {
            for (int i2 = n3; i2 < n5; ++i2) {
                this.a[n][i2 + n4 * n7] = (byte)n2;
            }
            ++n4;
        }
        if (n == this.g) {
            this.e = -1;
            this.f = -1;
        }
    }

    private void b(int n, int n2, int n3, int n4, int n5, int n6) {
        int n7;
        int n8 = this.a[this.g][0];
        if (n2 >= n8) {
            n2 = n8;
        }
        if (n5 >= this.a[this.g][1]) {
            n5 = this.a[this.g][1];
        }
        int n9 = n + n4 * n8;
        n6 *= 24;
        int n10 = n7 = this.d >= 2 ? 1 : -1;
        while (n4 < n5) {
            if (n6 >= 360) {
                n6 = 0;
            }
            int n11 = n9;
            int n12 = n;
            int n13 = n3 * 24;
            while (n12 < n2) {
                byte by;
                if (n13 >= 264) {
                    n13 = 0;
                }
                if ((by = this.a[this.g][n11]) != -1) {
                    this.a.b(this.a, by, n13, n6, this.b[this.g][n11]);
                }
                if (n7 >= 0 && (by = this.a[n7][n11]) != -1) {
                    this.a.b(this.a, by, n13, n6, this.b[n7][n11]);
                }
                ++n12;
                n13 += 24;
                ++n11;
            }
            ++n4;
            n6 += 24;
            n9 += n8;
        }
    }

    final void a(int n, Image image, Graphics graphics) {
        this.g = 0;
        this.e = -1;
        this.f = -1;
        this.a = image;
        this.a = graphics;
    }

    final void a(Graphics graphics, b b2) {
        int n = this.d >= 2 ? 1 : -1;
        if (n < 0) {
            return;
        }
        n = b2.e / 24;
        int n2 = b2.f / 24;
        int n3 = b.length;
        int n4 = 0;
        while (n4 < n3) {
            int n5;
            byte by;
            int n6 = n + b[n4++];
            int n7 = n2 + b[n4++];
            if (n6 < 0 || n6 >= this.a || n7 < 0 || n7 >= this.b || (by = this.a[1][n5 = n6 + n7 * this.a[1][0]]) == -1) continue;
            this.a.b(graphics, by, n6 * 24 - b.c, n7 * 24 - b.d, this.b[1][n5]);
        }
    }

    static {
        b = new int[]{-1, -1, 0, -1, 1, -1, -1, 0, 0, 0, 1, 0, -1, 1, 0, 1, 1, 1};
    }
}

