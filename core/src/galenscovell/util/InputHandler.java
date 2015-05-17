
/**
 * INPUTHANDLER CLASS
 * Sets up input processor for player input with world (not ui)
 */

package galenscovell.util;

import com.badlogic.gdx.InputAdapter;

import galenscovell.screens.GameScreen;


public class InputHandler extends InputAdapter {
    private GameScreen game;


    public InputHandler(GameScreen game) {
        this.game = game;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        System.out.println(x + ", " + y + ", " + pointer + ", " + button);
        return true;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        return true;
    }

    @Override
    public boolean scrolled (int amount) {
        return true;
    }
}
