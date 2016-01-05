package galenscovell.flicker.ui.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import galenscovell.flicker.processing.states.StateType;
import galenscovell.flicker.things.inanimates.Inanimate;
import galenscovell.flicker.ui.screens.GameScreen;
import galenscovell.flicker.util.ResourceManager;
import galenscovell.flicker.world.Tile;

public class InteractButton extends Button {
    private final Table buttonTable;

    public InteractButton(GameScreen gameScreen, Inanimate inanimate, Tile tile) {
        super(ResourceManager.frameButtonStyle);
        this.buttonTable = new Table();
        create(gameScreen, inanimate, tile);
    }

    public void create(final GameScreen gameScreen, final Inanimate inanimate, final Tile tile) {
        updateButtonImage(inanimate);
        this.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (gameScreen.getState() == StateType.MENU) {
                    gameScreen.changeState(StateType.ACTION);
                }
                inanimate.interact(tile);
                updateButtonImage(inanimate);
            }
        });

        this.setWidth(90);
        this.setHeight(90);
        this.add(buttonTable).expand().fill();
    }

    private void updateButtonImage(final Inanimate inanimate) {
        buttonTable.clear();
        Sprite inanimateSprite = new Sprite(inanimate.getSprite());
        inanimateSprite.flip(false, true);
        Image buttonSprite = new Image(inanimateSprite);
        buttonSprite.setScaling(Scaling.fit);
        buttonTable.add(buttonSprite).expand().fill();
    }
}
