package galenscovell.flicker.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.*;
import galenscovell.flicker.FlickerMain;

public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useWakelock = true;
        initialize(new FlickerMain(), config);
    }
}
