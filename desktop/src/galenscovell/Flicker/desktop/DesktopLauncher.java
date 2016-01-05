package galenscovell.flicker.desktop;

import com.badlogic.gdx.backends.lwjgl.*;
import galenscovell.flicker.FlickerMain;
import galenscovell.flicker.util.Constants;

public class DesktopLauncher {

    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.UI_X;
        config.height = Constants.UI_Y;
        config.title = "Flicker";
        config.resizable = true;
        new LwjglApplication(new FlickerMain(), config);
    }
}
