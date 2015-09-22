package galenscovell.things.inanimates;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.graphics.Lighting;
import galenscovell.util.ResourceManager;
import galenscovell.world.Tile;

public class Door implements Inanimate {
    private int x, y;
    private Sprite sprite;
    private Sprite[] sprites;
    private boolean blocking;
    private Lighting lighting;

    public Door(int x, int y, String type, Lighting lighting) {
        this.x = x;
        this.y = y;
        this.sprites = new Sprite[2];
        this.sprites[0] = new Sprite(ResourceManager.inanimateAtlas.createSprite("door" + type + "0"));
        sprites[0].flip(false, true);
        this.sprites[1] = new Sprite(ResourceManager.inanimateAtlas.createSprite("door" + type + "1"));
        sprites[1].flip(false, true);
        this.sprite = sprites[0];
        this.blocking = true;
        this.lighting = lighting;
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
            lighting.updateTileBody(tile.x, tile.y);
            return "The door opens.";
        } else {
            this.sprite = sprites[0];
            tile.toggleBlocking();
            tile.toggleOccupied();
            blocking = true;
            lighting.updateTileBody(tile.x, tile.y);
            return "The door closes.";
        }
    }

    public void draw(SpriteBatch batch, int tileSize) {
        batch.draw(sprite, x * tileSize, y * tileSize, tileSize, tileSize);
    }
}
