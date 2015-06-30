package galenscovell.flicker;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.screens.AbstractScreen;
import galenscovell.screens.GameScreen;
import galenscovell.screens.LoadingScreen;
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
    public AbstractScreen loadingScreen, mainMenuScreen;
    public GameScreen gameScreen;

    @Override
    public void create () {
        // Initialize spriteBatch used throughout game
        this.spriteBatch = new SpriteBatch();
        this.loadingScreen = new LoadingScreen(this);
        this.mainMenuScreen = new MainMenuScreen(this);
        setScreen(loadingScreen);
    }

    public void newGame() {
        this.gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    public void continueGame() {
        // TODO: Continue recent game from deserialized data
    }

    @Override
    public void dispose() {
        mainMenuScreen.dispose();
        if (gameScreen != null) {
            gameScreen.dispose();
        }
        ResourceManager.dispose();
    }
}
