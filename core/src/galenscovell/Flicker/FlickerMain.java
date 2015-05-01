
/**
 * FLICKERMAIN CLASS
 * Provides main entry for application.
 */

package galenscovell.flicker;

import com.badlogic.gdx.Game;

import galenscovell.screens.GameScreen;


public class FlickerMain extends Game {

    @Override
    public void create () {
        setScreen(new GameScreen());
    }
}
