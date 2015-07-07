package galenscovell.inanimates;

import galenscovell.logic.Tile;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * STAIRS INANIMATE
 * Loads stair sprite and handles interaction events.
 *
 * @author Galen Scovell
 */

public class Stairs implements Inanimate {
    private int x, y;
    private Sprite sprite;
    private boolean blocking;

    public Stairs(int x, int y) {
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(ResourceManager.inanimateAtlas.createSprite("stairs"));
        sprite.flip(false, true);
        this.blocking = false;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String examine() {
        return "Stairs descend deeper.";
    }

    public String interact(Tile tile) {
        return "Descending stairs...";
    }

    public void draw(SpriteBatch batch, int tileSize) {
        batch.draw(sprite, x * tileSize, y * tileSize, tileSize, tileSize);
    }
}