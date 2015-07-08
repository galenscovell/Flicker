package galenscovell.flicker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import galenscovell.flicker.FlickerMain;

/**
 * DESKTOP LAUNCHER
 *
 */

public class DesktopLauncher {

    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 480;
        config.height = 800;
        config.title = "Flicker";
        config.resizable = true;
        new LwjglApplication(new FlickerMain(), config);
    }
}
