package galenscovell.util;

import galenscovell.screens.GameScreen;

import com.badlogic.gdx.input.GestureDetector;

/**
 * GESTURE HANDLER
 * Sets up gesture detector for player interaction with world (not ui)
 *
 * @author Galen Scovell
 */

public class GestureHandler extends GestureDetector.GestureAdapter {
    private GameScreen game;

    public GestureHandler(GameScreen game) {
        this.game = game;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if (game.tileSelection()) {
            System.out.println("Tile selection made.");
            return true;
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        game.screenPan(deltaX, deltaY);
        return true;
    }

    @Override
    public boolean zoom (float initialDistance, float endDistance){
        game.screenZoom(endDistance < initialDistance, true);
        return true;
    }
}
