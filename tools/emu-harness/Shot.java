import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import org.microemu.DisplayAccess;
import org.microemu.DisplayComponent;
import org.microemu.EmulatorContext;
import org.microemu.MIDletAccess;
import org.microemu.MIDletBridge;
import org.microemu.app.Common;
import org.microemu.app.ui.Message;
import org.microemu.app.ui.noui.NoUiDisplayComponent;
import org.microemu.app.util.DeviceEntry;
import org.microemu.device.DeviceDisplay;
import org.microemu.device.FontManager;
import org.microemu.device.InputMethod;
import org.microemu.device.j2se.J2SEDevice;
import org.microemu.device.j2se.J2SEDeviceDisplay;
import org.microemu.device.j2se.J2SEFontManager;
import org.microemu.device.j2se.J2SEInputMethod;

/** Offscreen 320x240 renderer for the game, matching E72's canvas.
 *  Usage: Shot <gameJar> <outDir> <script>
 *  script = comma steps: "wait:1200" | "shot:name" | "key:CODE" (press+release)
 */
public class Shot {
    static final NoUiDisplayComponent DC = new NoUiDisplayComponent();
    static final EmulatorContext ctx = new EmulatorContext() {
        private DisplayComponent dc = DC;
        private InputMethod im = new J2SEInputMethod();
        private DeviceDisplay dd = new J2SEDeviceDisplay(this);
        private FontManager fm = new J2SEFontManager();
        public DisplayComponent getDisplayComponent() { return dc; }
        public InputMethod getDeviceInputMethod() { return im; }
        public DeviceDisplay getDeviceDisplay() { return dd; }
        public FontManager getDeviceFontManager() { return fm; }
        public InputStream getResourceAsStream(String name) {
            return MIDletBridge.getCurrentMIDlet().getClass().getResourceAsStream(name);
        }
        public boolean platformRequest(String url) { return false; }
    };

    static String outDir;
    static int shotIdx = 0;

    public static void main(String[] args) throws Exception {
        String gameJar = args[0];
        outDir = args[1];
        String script = args.length > 2 ? args[2] : "wait:2000,shot:00";
        new File(outDir).mkdirs();

        Common emu = new Common(ctx);
        DeviceEntry dev = new DeviceEntry("E72", null, "e72/device.xml", true, false);
        List<String> params = new ArrayList<String>();
        params.add("--rms"); params.add("memory");
        params.add(gameJar);
        emu.initParams(params, dev, J2SEDevice.class);
        emu.initMIDlet(true);
        System.out.println("[Shot] MIDlet started");
        try {
            org.microemu.device.Device d = org.microemu.device.DeviceFactory.getDevice();
            org.microemu.device.DeviceDisplay dd = d.getDeviceDisplay();
            System.out.println("[dev] name=" + d.getName() + " full=" + dd.getFullWidth() + "x" + dd.getFullHeight()
                + " wh=" + dd.getWidth() + "x" + dd.getHeight() + " fullscreen=" + dd.isFullScreenMode());
        } catch (Throwable t) { System.out.println("[dev] ERR " + t); }

        for (String step : script.split(",")) {
            String[] p = step.split(":");
            String cmd = p[0];
            if (cmd.equals("wait")) {
                Thread.sleep(Integer.parseInt(p[1]));
            } else if (cmd.equals("shot")) {
                snap(p[1]);
            } else if (cmd.equals("key")) {
                key(Integer.parseInt(p[1]));
            }
        }
        System.out.println("[Shot] done");
        System.exit(0);
    }

    static DisplayAccess da() {
        MIDletAccess ma = MIDletBridge.getMIDletAccess();
        return ma == null ? null : ma.getDisplayAccess();
    }

    static void snap(String name) {
        try {
            org.microemu.device.MutableImage mi = DC.getDisplayImage();
            if (mi == null) { System.out.println("[snap] framebuffer null (no repaint yet)"); return; }
            BufferedImage full = (BufferedImage) ((org.microemu.device.j2se.J2SEMutableImage) mi).getImage();
            int w = Math.min(320, full.getWidth()), h = Math.min(240, full.getHeight());
            BufferedImage bi = full.getSubimage(0, 0, w, h);
            File f = new File(outDir, String.format("s%02d_%s.png", shotIdx++, name));
            ImageIO.write(bi, "png", f);
            System.out.println("[snap] " + f.getName() + " (fb " + full.getWidth() + "x" + full.getHeight() + ")");
        } catch (Throwable t) { System.out.println("[snap] ERR " + t); }
    }

    static void key(int code) {
        try {
            DisplayAccess da = da();
            if (da == null) return;
            da.keyPressed(code);
            Thread.sleep(80);
            da.keyReleased(code);
            System.out.println("[key] " + code);
        } catch (Throwable t) { System.out.println("[key] ERR " + t); }
    }
}
