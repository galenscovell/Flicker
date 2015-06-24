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
        config.width = 800;
        config.height = 480;
        config.title = "Flicker";
        config.resizable = true;
        new LwjglApplication(new FlickerMain(), config);
    }
}
