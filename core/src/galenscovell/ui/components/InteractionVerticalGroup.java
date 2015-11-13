package galenscovell.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

public class InteractionVerticalGroup extends VerticalGroup {

    public InteractionVerticalGroup() {
        create();
    }

    public void create() {
        this.setWidth(100);
        this.setHeight(300);
        this.align(Align.left);
    }
}

