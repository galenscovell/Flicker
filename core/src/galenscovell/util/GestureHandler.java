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
        Vector3 worldCoordinates = new Vector3(x, y, 0);
        camera.unproject(worldCoordinates);
        if (game.examineMode()) {
            game.findTile(worldCoordinates.x, worldCoordinates.y);
        }
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        Vector3 worldCoordinates = new Vector3(x, y, 0);
        camera.unproject(worldCoordinates);
        if (game.examineMode()) {
            game.findTile(worldCoordinates.x, worldCoordinates.y);
        } else {
            game.playerMove(worldCoordinates.x, worldCoordinates.y);
        }
        return true;
    }

    @Override
    public boolean zoom (float initialDistance, float endDistance){
        game.screenZoom(endDistance < initialDistance);
        return true;
    }
}
