package galenscovell.ui.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.util.ResourceManager;
import galenscovell.world.Tile;

public class InteractButton extends Button {

    public InteractButton(Inanimate inanimate, Tile tile) {
        super(ResourceManager.frameStyle);
        create(inanimate, tile);
    }

    public void create(final Inanimate inanimate, final Tile tile) {
        Sprite inanimateSprite = new Sprite(inanimate.getSprite());
        inanimateSprite.flip(false, true);
        Image inanimateImage = new Image(inanimateSprite);
        inanimateImage.setScaling(Scaling.fit);
        this.add(inanimateImage).expand().fill();
        this.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                inanimate.interact(tile);
            }
        });
        this.setWidth(90);
        this.setHeight(90);
    }
}
