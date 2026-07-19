/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Display
 *  javax.microedition.lcdui.Displayable
 *  javax.microedition.midlet.MIDlet
 */
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

public class CGTQ
extends MIDlet {
    private static f a;

    public CGTQ() {
        a = null;
    }

    public void startApp() {
        if (a == null) {
            a = new f(this);
            new Thread(a).start();
        }
        Display.getDisplay((MIDlet)this).setCurrent((Displayable)a);
        a.showNotify();
    }

    public void pauseApp() {
        a.hideNotify();
    }

    public void destroyApp(boolean bl) {
        this.notifyDestroyed();
    }
}

