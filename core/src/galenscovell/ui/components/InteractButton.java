package galenscovell.ui.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import galenscovell.processing.states.StateType;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.ui.screens.GameScreen;
import galenscovell.util.ResourceManager;
import galenscovell.world.Tile;

public class InteractButton extends Button {

    public InteractButton(GameScreen gameScreen, Inanimate inanimate, Tile tile) {
        super(ResourceManager.frameStyle);
        create(gameScreen, inanimate, tile);
    }

    public void create(final GameScreen gameScreen, final Inanimate inanimate, final Tile tile) {
        Sprite inanimateSprite = new Sprite(inanimate.getSprite());
        inanimateSprite.flip(false, true);
        Image inanimateImage = new Image(inanimateSprite);
        inanimateImage.setScaling(Scaling.fit);
        this.add(inanimateImage).expand().fill();
        this.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (gameScreen.getState() == StateType.MENU) {
                    gameScreen.changeState(StateType.ACTION);
                }
                inanimate.interact(tile);
            }
        });
        this.setWidth(90);
        this.setHeight(90);
    }
}
