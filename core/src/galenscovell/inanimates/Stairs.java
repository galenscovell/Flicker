package galenscovell.inanimates;

import galenscovell.graphics.SpriteSheet;
import galenscovell.logic.Tile;

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
        SpriteSheet sheet = SpriteSheet.tilesheet;
        this.sprite = new Sprite(sheet.getSprite(98));
        this.blocking = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return "Stairs";
    }

    public String interact(Tile tile) {
        return "The stairs descend deeper.";
    }

    public void draw(SpriteBatch batch, int tileSize) {
        batch.draw(sprite, x * tileSize, y * tileSize, tileSize, tileSize);
    }
}