
/**
 * DOOR CLASS
 * Loads door sprite and handles interaction events.
 * @direction is either 0 (vertically facing) or 1 (horizontally facing).
 */

package galenscovell.inanimates;

import com.badlogic.gdx.graphics.g2d.Sprite;

import galenscovell.graphics.SpriteSheet;
import galenscovell.logic.Tile;


public class Door implements Inanimate {
    private int x, y;
    private Sprite sprite;
    private Sprite[] sprites;
    private boolean blocking;


    public Door(int x, int y, int direction) {
        this.x = x;
        this.y = y;

        SpriteSheet sheet = SpriteSheet.tilesheet;
        this.sprites = new Sprite[2];
        this.sprites[0] = new Sprite(sheet.getSprite(96 + direction));
        this.sprites[1] = new Sprite(sheet.getSprite(112 + direction));
        this.sprite = sprites[0];

        this.blocking = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return "Door";
    }

    public Sprite getSprite() {
        return sprite;
    }

    public String interact(Tile tile) {
        if (blocking) {
            this.sprite = sprites[1];
            tile.toggleBlocking();
            tile.toggleOccupied();
            blocking = false;
            return "The door creaks open.";
        } else {
            this.sprite = sprites[0];
            tile.toggleBlocking();
            tile.toggleOccupied();
            blocking = true;
            return "You close the door.";
        }
    }

    public boolean isBlocking() {
        return blocking;
    }
}
