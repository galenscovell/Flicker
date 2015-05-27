
/**
 * FLICKERMAIN CLASS
 * Provides main entry for application. Creates all screens for game and sets to main menu.
 */

package galenscovell.flicker;

import com.badlogic.gdx.Game;

import galenscovell.screens.GameScreen;
import galenscovell.screens.MainMenuScreen;
import galenscovell.screens.OptionsScreen;
import galenscovell.util.ResourceManager;


public class FlickerMain extends Game {
    public MainMenuScreen mainMenuScreen;
    public OptionsScreen optionsScreen;
    public GameScreen gameScreen;


    @Override
    public void create () {
        ResourceManager.load();
        this.mainMenuScreen = new MainMenuScreen(this);
        this.optionsScreen = new OptionsScreen(this);
        setScreen(mainMenuScreen);
    }

    @Override
    public void dispose() {
        ResourceManager.dispose();
    }

    public void newGame() {
        this.gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    public void continueGame() {

    }
}
