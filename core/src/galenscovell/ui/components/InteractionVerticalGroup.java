package galenscovell.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.world.Tile;

public class InteractionVerticalGroup extends VerticalGroup {

    public InteractionVerticalGroup() {
        create();
    }

    public void create() {
        this.setWidth(100);
        this.setHeight(300);
        this.align(Align.left);
    }

    public void clearBoxes() {
        this.clear();
    }

    public void addBox(Inanimate inanimate, Tile tile) {
        InteractButton btn = new InteractButton(inanimate, tile);
        this.addActor(btn);
    }
}

