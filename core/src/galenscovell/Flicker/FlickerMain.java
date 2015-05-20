
/**
 * FLICKERMAIN CLASS
 * Provides main entry for application. Creates all screens for game and sets to main menu.
 */

package galenscovell.flicker;

import com.badlogic.gdx.Game;

import galenscovell.screens.GameScreen;
import galenscovell.screens.MainMenuScreen;


public class FlickerMain extends Game {
    public MainMenuScreen mainMenuScreen;
    public GameScreen gameScreen;


    @Override
    public void create () {
        this.mainMenuScreen = new MainMenuScreen(this);
        this.gameScreen = new GameScreen(this);
        setScreen(mainMenuScreen);
    }
}
