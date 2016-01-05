package galenscovell.flicker.processing.controls;

import com.badlogic.gdx.input.GestureDetector;
import galenscovell.flicker.ui.screens.GameScreen;

public class GestureHandler extends GestureDetector.GestureAdapter {
    private final GameScreen game;

    public GestureHandler(GameScreen game) {
        this.game = game;
    }

    @Override
    public boolean zoom (float initialDistance, float endDistance){
        game.screenZoom(endDistance - initialDistance);
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        game.screenPan(deltaX, deltaY);
        return true;
    }
}
