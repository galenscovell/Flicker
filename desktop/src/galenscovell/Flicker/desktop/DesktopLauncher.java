
package galenscovell.flicker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import galenscovell.flicker.FlickerMain;
import galenscovell.util.Constants;


public class DesktopLauncher {

    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.WINDOW_X;
        config.height = Constants.WINDOW_Y;
        config.title = "Flicker";
        config.resizable = false;
        new LwjglApplication(new FlickerMain(), config);
    }
}
