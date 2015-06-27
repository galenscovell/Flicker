package galenscovell.util;

import galenscovell.screens.GameScreen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;

/**
 * GESTURE HANDLER
 * Sets up gesture detector for player interaction with world (not ui)
 *
 * @author Galen Scovell
 */

public class GestureHandler extends GestureDetector.GestureAdapter {
    private GameScreen game;
    private OrthographicCamera camera;

    public GestureHandler(GameScreen game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if (game.tileSelection()) {
            Vector3 worldCoordinates = new Vector3(x, y, 0);
            camera.unproject(worldCoordinates);
            game.findTile(worldCoordinates.x, worldCoordinates.y);
            game.playerMove(worldCoordinates.x, worldCoordinates.y);
            return true;
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        game.screenPan(deltaX, deltaY);
        return true;
    }

    @Override
    public boolean zoom (float initialDistance, float endDistance){
        game.screenZoom(endDistance < initialDistance);
        return true;
    }
}
