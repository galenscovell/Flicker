package galenscovell.inanimates;

import galenscovell.logic.Renderer;
import galenscovell.logic.Tile;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * OBSTRUCTION INANIMATE
 * Loads obstruction sprite and handles interaction events.
 * This object has access to Renderer for handling of Body collision updates.
 *
 * @author Galen Scovell
 */

public class Obstruction implements Inanimate {
    private Renderer renderer;
    private int x, y;
    private Sprite sprite;
    private Sprite[] sprites;
    private boolean blocking;

    public Obstruction(Renderer renderer, int x, int y) {
        this.renderer = renderer;
        this.x = x;
        this.y = y;
        this.sprites = new Sprite[2];
        // TODO: Varied obstructions (depending on environs)
        this.sprites[0] = new Sprite(ResourceManager.inanimateAtlas.createSprite("boulder0"));
        sprites[0].flip(false, true);
        this.sprites[1] = new Sprite(ResourceManager.inanimateAtlas.createSprite("boulder1"));
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
        // TODO: Change description based on obstruction type
        return "A boulder blocks the path.";
    }

    public String interact(Tile tile) {
        if (blocking) {
            this.sprite = sprites[1];
            tile.toggleBlocking();
            tile.toggleOccupied();
            blocking = false;
            renderer.updateTileBody(tile.x, tile.y);
            return "The boulder shatters.";
        } else {
            return "A pile of rubble.";
        }
    }

    public void draw(SpriteBatch batch, int tileSize) {
        batch.draw(sprite, x * tileSize, y * tileSize, tileSize, tileSize);
    }
}