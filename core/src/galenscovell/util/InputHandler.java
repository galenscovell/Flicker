package galenscovell.util;

import galenscovell.screens.GameScreen;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * INPUT HANDLER
 * Handles basic input not relating to gestures.
 *
 * @author Galen Scovell
 */

public class InputHandler extends InputAdapter {
    private GameScreen game;
    private OrthographicCamera camera;

    public InputHandler(GameScreen game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        Vector3 worldCoordinates = new Vector3(x, y, 0);
        camera.unproject(worldCoordinates);
        if (game.isExamineMode()) {

        } else if (game.isAttackMode()) {

        } else {
            game.playerMove(worldCoordinates.x, worldCoordinates.y);
        }
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        game.zoom(amount * 1000);
        return true;
    }
}
