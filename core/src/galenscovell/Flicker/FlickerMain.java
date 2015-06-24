package galenscovell.flicker;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.screens.GameScreen;
import galenscovell.screens.MainMenuScreen;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.Game;

/**
 * FLICKER MAIN
 * Main entry for application.
 *
 * @author Galen Scovell
 */

public class FlickerMain extends Game {
    public SpriteBatch spriteBatch;
    public MainMenuScreen mainMenuScreen;
    public GameScreen gameScreen;

    @Override
    public void create () {
        // Initialize resource manager and load assets
        ResourceManager.create();
        ResourceManager.assetManager.finishLoading();
        ResourceManager.done();

        this.spriteBatch = new SpriteBatch();
        this.mainMenuScreen = new MainMenuScreen(this);
        setScreen(mainMenuScreen);
    }

    public void newGame(String classType) {
        this.gameScreen = new GameScreen(this, classType);
        setScreen(gameScreen);
    }

    public void continueGame() {
        // TODO: Continue recent game from deserialized data
    }

    @Override
    public void dispose() {
        mainMenuScreen.dispose();
        ResourceManager.dispose();
    }
}
