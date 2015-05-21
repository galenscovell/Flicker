
/**
 * FLICKERMAIN CLASS
 * Provides main entry for application. Creates all screens for game and sets to main menu.
 */

package galenscovell.flicker;

import com.badlogic.gdx.Game;

import galenscovell.screens.GameScreen;
import galenscovell.screens.MainMenuScreen;
import galenscovell.screens.OptionsScreen;
import galenscovell.util.ScreenResources;


public class FlickerMain extends Game {
    public static ScreenResources screenResources;
    public MainMenuScreen mainMenuScreen;
    public OptionsScreen optionsScreen;
    public GameScreen gameScreen;


    @Override
    public void create () {
        this.screenResources = new ScreenResources();
        this.mainMenuScreen = new MainMenuScreen(this);
        this.optionsScreen = new OptionsScreen(this);
        this.gameScreen = new GameScreen(this);
        setScreen(mainMenuScreen);
    }

    @Override
    public void dispose() {
        screenResources.dispose();
    }
}
