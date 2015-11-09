package galenscovell.processing.controls;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import galenscovell.ui.screens.GameScreen;

public class InputHandler extends InputAdapter {
    private final GameScreen game;
    private final OrthographicCamera camera;
    private int startX, startY;

    public InputHandler(GameScreen game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        this.startX = x;
        this.startY = y;
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (Math.abs(x - this.startX) < 10 && Math.abs(y - this.startY) < 10) {
            Vector3 worldCoordinates = new Vector3(x, y, 0);
            this.camera.unproject(worldCoordinates);
            this.game.passInputToState(worldCoordinates.x, worldCoordinates.y);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean scrolled(int amount) {
        this.game.screenZoom(amount * 1000);
        return true;
    }
}
