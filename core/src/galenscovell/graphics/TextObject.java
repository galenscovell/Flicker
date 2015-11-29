package galenscovell.graphics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import galenscovell.things.entities.Entity;
import galenscovell.util.*;

public class TextObject {
    private final Entity entity;
    public final Sprite sprite;
    private int frame;

    public TextObject(Entity entity, int val) {
        this.entity = entity;
        this.sprite = ResourceManager.numbers[val];
        this.frame = 0;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getX() {
        return entity.getCurrentX() + Constants.TILESIZE / 2;
    }

    public float getY() {
        return entity.getCurrentY() - Constants.TILESIZE;
    }

    public boolean done() {
        frame++;
        return frame == 120;
    }
}
