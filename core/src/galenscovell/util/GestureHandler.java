package galenscovell.util;

import galenscovell.screens.GameScreen;

import com.badlogic.gdx.input.GestureDetector;

/**
 * GESTURE HANDLER
 * Handles gesture input.
 *
 * @author Galen Scovell
 */

public class GestureHandler extends GestureDetector.GestureAdapter {
    private GameScreen game;

    public GestureHandler(GameScreen game) {
        this.game = game;
    }

    @Override
    public boolean zoom (float initialDistance, float endDistance){
        game.zoom(endDistance - initialDistance);
        return true;
    }
}
