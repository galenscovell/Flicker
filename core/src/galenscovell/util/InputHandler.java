package galenscovell.util;

import galenscovell.screens.GameScreen;

import com.badlogic.gdx.InputAdapter;

/**
 * INPUT HANDLER
 * Sets up desktop specific input detection for player interaction with world (not ui)
 *
 * @author Galen Scovell
 */

public class InputHandler extends InputAdapter {
    private GameScreen game;

    public InputHandler(GameScreen game) {
        this.game = game;
    }

    @Override
    public boolean scrolled (int amount) {
        game.screenZoom(amount > 0, false);
        return true;
    }
}
