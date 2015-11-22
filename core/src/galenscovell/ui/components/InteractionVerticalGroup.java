package galenscovell.ui.components;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

public class InteractionVerticalGroup extends VerticalGroup {
    private boolean active;

    public InteractionVerticalGroup() {
        create();
    }

    public void create() {
        this.active = true;
        this.setWidth(90);
        this.setHeight(340);
        this.setPosition(0, 80);
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

