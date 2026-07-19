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
public final class l {
    int a;
    private int c;
    private int d;
    private int e;
    boolean a;
    boolean b;
    d a;
    int b;

    public l(d d2) {
        this.a = d2;
        this.b = 0;
    }

    final void a(int n, boolean bl) {
        this.a = n;
        this.a = bl;
        this.c = 0;
        this.d = this.a.b[(this.a.a[this.a] & 0xFFFF) + this.c] & 0xFF;
        this.b = false;
        this.e = 0;
    }

    final void a() {
        if (this.b) {
            return;
        }
        this.e += 40;
        while (this.e > 66) {
            this.e -= 66;
            --this.d;
            if (this.d > 0) continue;
            ++this.c;
            if (this.c >= (this.a.a[this.a] & 0xFF)) {
                if (this.a) {
                    this.c = 0;
                } else {
                    --this.c;
                    this.b = true;
                }
            }
            this.d = this.a.b[(this.a.a[this.a] & 0xFFFF) + this.c] & 0xFF;
        }
    }

    final void a(Graphics graphics, int n, int n2) {
        this.a.a(graphics, this.a, this.c, n, n2, this.b);
    }
}

