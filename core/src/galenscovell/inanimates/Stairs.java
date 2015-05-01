
/**
 * STAIRS CLASS
 * Loads stair sprite and handles interaction events.
 */

package galenscovell.inanimates;

import com.badlogic.gdx.graphics.g2d.Sprite;

import galenscovell.graphics.SpriteSheet;
import galenscovell.logic.Tile;


public class Stairs implements Inanimate {
    private int x, y, tileSize;
    private boolean blocking;
    public Sprite sprite;


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

    public void interact(Tile tile) {

    }

    public boolean isBlocking() {
        return blocking;
    }

    public Sprite getSprite() {
        return sprite;
    }
}