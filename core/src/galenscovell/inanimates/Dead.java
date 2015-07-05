package galenscovell.inanimates;

import galenscovell.logic.Tile;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

/**
 * DEAD INANIMATE
 * Enemy death sprite loading and location.
 *
 * @author Galen Scovell
 */

public class Dead implements Inanimate {
    private int x, y;
    private Sprite sprite;
    private boolean blocking;

    public Dead(int x, int y) {
        this.x = x;
        this.y = y;
        Random random = new Random();
        int choice = random.nextInt(4);
        this.sprite = new Sprite(ResourceManager.inanimateAtlas.findRegion("corpse" + choice));
        sprite.flip(false, true);
        this.blocking = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return "Dead";
    }

    public String interact(Tile tile) {
        return "A corpse lies here.";
    }

    public void draw(SpriteBatch batch, int tileSize) {
        batch.draw(sprite, x * tileSize, y * tileSize, tileSize, tileSize);
    }
}