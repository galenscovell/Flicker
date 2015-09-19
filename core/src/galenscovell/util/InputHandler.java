package galenscovell.util;

import galenscovell.ui.screens.GameScreen;

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
    private int startX, startY;

    public InputHandler(GameScreen game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        startX = x;
        startY = y;
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (Math.abs(x - startX) < 10 && Math.abs(y - startY) < 10) {
            Vector3 worldCoordinates = new Vector3(x, y, 0);
            camera.unproject(worldCoordinates);
            if (game.isExamineMode()) {
                // game.playerExamine(worldCoordinates.x, worldCoordinates.y);
            } else if (game.isAttackMode()) {
                game.playerAttack(worldCoordinates.x, worldCoordinates.y);
            } else {
                game.playerMove(worldCoordinates.x, worldCoordinates.y);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean scrolled(int amount) {
        game.zoom(amount * 1000);
        return true;
    }
}
