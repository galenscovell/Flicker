
/**
 * INPUTHANDLER CLASS
 * Sets up desktop specific input detection for player interaction with world (not ui)
 */

package galenscovell.util;

import com.badlogic.gdx.InputAdapter;

import galenscovell.screens.GameScreen;


public class InputHandler extends InputAdapter {
    private GameScreen game;


    public InputHandler(GameScreen game) {
        this.game = game;
    }

    // Desktop only
    @Override
    public boolean scrolled (int amount) {
        game.screenZoom(amount > 0, false);
        return true;
    }
}
