package galenscovell.things.inanimates;

import galenscovell.logic.Renderer;
import galenscovell.logic.world.Tile;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * DOOR
 * Loads door sprite and handles interaction events.
 * This object has access to Renderer for handling of Body collision updates.
 *
 * @author Galen Scovell
 */

public class Door implements Inanimate {
    private Renderer renderer;
    private int x, y;
    private Sprite sprite;
    private Sprite[] sprites;
    private boolean blocking;

    public Door(Renderer renderer, int x, int y, String type) {
        this.renderer = renderer;
        this.x = x;
        this.y = y;
        this.sprites = new Sprite[2];
        this.sprites[0] = new Sprite(ResourceManager.inanimateAtlas.createSprite("door" + type + "0"));
        sprites[0].flip(false, true);
        this.sprites[1] = new Sprite(ResourceManager.inanimateAtlas.createSprite("door" + type + "1"));
        sprites[1].flip(false, true);
        this.sprite = sprites[0];
        this.blocking = true;
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
        return "A closed door.";
    }

    public String interact(Tile tile) {
        if (blocking) {
            this.sprite = sprites[1];
            tile.toggleBlocking();
            tile.toggleOccupied();
            blocking = false;
            renderer.updateTileBody(tile.x, tile.y);
            return "The door opens.";
        } else {
            this.sprite = sprites[0];
            tile.toggleBlocking();
            tile.toggleOccupied();
            blocking = true;
            renderer.updateTileBody(tile.x, tile.y);
            return "The door closes.";
        }
    }

    public void draw(SpriteBatch batch, int tileSize) {
        batch.draw(sprite, x * tileSize, y * tileSize, tileSize, tileSize);
    }
}
