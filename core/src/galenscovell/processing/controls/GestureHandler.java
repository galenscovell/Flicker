package galenscovell.processing.controls;

import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import galenscovell.ui.screens.GameScreen;

public class GestureHandler extends GestureAdapter {
    private final GameScreen game;

    public GestureHandler(GameScreen game) {
        this.game = game;
    }

    @Override
    public boolean zoom (float initialDistance, float endDistance){
        this.game.screenZoom(endDistance - initialDistance);
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        this.game.screenPan(deltaX, deltaY);
        return true;
    }
}
