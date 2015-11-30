package galenscovell.graphics;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.things.entities.Entity;
import galenscovell.util.*;

public class TextObject {
    private final Entity entity;
    public final Sprite sprite;
    private int frame, size;

    public TextObject(Entity entity, int val) {
        this.entity = entity;
        this.sprite = ResourceManager.numbers[val];
        this.frame = 160;
        this.size = Constants.TILESIZE / 2;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getX() {
        return entity.getCurrentX() + Constants.TILESIZE / 2;
    }

    public float getY() {
        if (frame > 100) {
            return entity.getCurrentY() - ((1.0f - ((frame - 100) / 100.0f)) * Constants.TILESIZE);
        } else {
            return entity.getCurrentY() - Constants.TILESIZE;
        }
    }

    public float getAlpha() {
        if (frame > 30) {
            return 1.0f;
        } else {
            return (frame / 30.0f);
        }
    }

    public boolean done() {
        frame--;
        return frame == 1;
    }

    public void draw(SpriteBatch batch) {
        batch.setColor(1.0f, 1.0f, 1.0f, getAlpha());
        batch.draw(sprite, getX(), getY(), size, size);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
