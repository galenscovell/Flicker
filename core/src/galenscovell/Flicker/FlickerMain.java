package galenscovell.flicker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import galenscovell.ui.screens.*;
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
        this.setScreen(this.loadingScreen);
    }

    public void newGame() {
        this.loadingScreen.dispose();
        this.gameScreen = new GameScreen(this);
        this.setScreen(this.gameScreen);
    }

    public void continueGame() {
        // TODO: Continue recent game from deserialized data
    }

    @Override
    public void dispose() {
        this.mainMenuScreen.dispose();
        if (this.gameScreen != null) {
            this.gameScreen.dispose();
        }
        ResourceManager.dispose();
    }
}
