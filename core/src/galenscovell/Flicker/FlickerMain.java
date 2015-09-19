package galenscovell.flicker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import galenscovell.ui.screens.AbstractScreen;
import galenscovell.ui.screens.GameScreen;
import galenscovell.ui.screens.LoadScreen;
import galenscovell.ui.screens.MainMenuScreen;
import galenscovell.util.ResourceManager;

public class FlickerMain extends Game {
    public SpriteBatch spriteBatch;
    public AbstractScreen loadingScreen, mainMenuScreen;
    public GameScreen gameScreen;

    @Override
    public void create () {
        // Initialize spriteBatch used throughout game
        this.spriteBatch = new SpriteBatch();
        this.loadingScreen = new LoadScreen(this);
        this.mainMenuScreen = new MainMenuScreen(this);
        setScreen(loadingScreen);
    }

    public void newGame() {
        loadingScreen.dispose();
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
