package galenscovell.ui.components;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

public class InteractionVerticalGroup extends VerticalGroup {
    private boolean active;

    public InteractionVerticalGroup() {
        create();
    }

    public void create() {
        this.active = true;
        this.setWidth(100);
        this.setHeight(300);
        this.align(Align.left);
    }

    public void toggle() {
        if (active) {
            active = false;
            for (Actor actor : this.getChildren()) {
                actor.setTouchable(Touchable.disabled);
            }
        } else {
            active = true;
            for (Actor actor : this.getChildren()) {
                actor.setTouchable(Touchable.enabled);
            }
        }
    }
}

