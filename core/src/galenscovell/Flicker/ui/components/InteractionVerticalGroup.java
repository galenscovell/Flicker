package galenscovell.flicker.ui.components;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import galenscovell.flicker.util.Constants;

public class InteractionVerticalGroup extends VerticalGroup {
    private boolean active;

    public InteractionVerticalGroup() {
        create();
    }

    public void create() {
        this.setName("interactionGroup");
        this.active = true;
        this.setWidth(90);
        this.setHeight(340);
        this.setPosition(0, 0.1f * Constants.UI_Y);
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

