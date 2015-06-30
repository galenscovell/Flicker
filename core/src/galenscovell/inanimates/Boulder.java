package galenscovell.inanimates;

import galenscovell.graphics.SpriteSheet;
import galenscovell.logic.Tile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * DOOR INANIMATE
 * Loads door sprite and handles interaction events.
 *
 * @author Galen Scovell
 */

public class Boulder implements Inanimate {
    private int x, y;
    private Sprite sprite;
    private Sprite[] sprites;
    private boolean blocking, stateChanged;

    public Boulder(int x, int y) {
        this.x = x;
        this.y = y;
        SpriteSheet sheet = SpriteSheet.tilesheet;
        this.sprites = new Sprite[2];
        this.sprites[0] = new Sprite(sheet.getSprite(96));
        this.sprites[1] = new Sprite(sheet.getSprite(97));
        this.sprite = sprites[0];
        this.blocking = true;
        this.stateChanged = false;
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

    public String interact(Tile tile) {
        if (blocking) {
            this.sprite = sprites[1];
            tile.toggleBlocking();
            tile.toggleOccupied();
            blocking = false;
            stateChanged = true;
            return "The door creaks open.";
        } else {
            this.sprite = sprites[0];
            tile.toggleBlocking();
            tile.toggleOccupied();
            blocking = true;
            stateChanged = true;
            return "You close the door.";
        }
    }

    public void draw(SpriteBatch batch, int tileSize) {
        batch.draw(sprite, x * tileSize, y * tileSize, tileSize, tileSize);
        if (stateChanged) {
            stateChanged = false;
        }
    }
}
