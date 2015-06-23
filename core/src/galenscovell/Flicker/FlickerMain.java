package galenscovell.flicker;

import galenscovell.screens.GameScreen;
import galenscovell.screens.MainMenuScreen;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.Game;

/**
 * FLICKER MAIN
 * Provides main entry for application. Creates all screens for game and sets to main menu.
 *
 * @author Galen Scovell
 */

public class FlickerMain extends Game {
    public MainMenuScreen mainMenuScreen;
    public GameScreen gameScreen;

    @Override
    public void create () {
        ResourceManager.load();
        this.mainMenuScreen = new MainMenuScreen(this);
        setScreen(mainMenuScreen);
    }

    @Override
    public void dispose() {
        ResourceManager.dispose();
    }

    public void newGame(String classType) {
        this.gameScreen = new GameScreen(this, classType);
        setScreen(gameScreen);
    }

    public void continueGame() {
        // TODO: Continue recent game from deserialized data
    }
}
