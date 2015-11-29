package galenscovell.things.inanimates;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.graphics.Lighting;
import galenscovell.util.ResourceManager;
import galenscovell.world.Tile;

public class Door implements Inanimate {
    private final int x;
    private final int y;
    private final Sprite[] sprites;
    private final Lighting lighting;
    private Sprite sprite;
    private boolean blocking;

    public Door(int x, int y, String type, Lighting lighting) {
        this.x = x;
        this.y = y;
        this.sprites = new Sprite[2];
        this.sprites[0] = new Sprite(ResourceManager.inanimateAtlas.createSprite("door" + type + "0"));
        this.sprites[0].flip(false, true);
        this.sprites[1] = new Sprite(ResourceManager.inanimateAtlas.createSprite("door" + type + "1"));
        this.sprites[1].flip(false, true);
        this.sprite = sprites[0];
        this.blocking = true;
        this.lighting = lighting;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public String examine() {
        return "A closed door.";
    }

    @Override
    public void interact(Tile tile) {
        if (blocking) {
            sprite = sprites[1];
            tile.toggleBlocking();
            tile.toggleOccupied();
            blocking = false;
            updateTileBody(tile);
        } else {
            if (!tile.isOccupied()) {
                sprite = sprites[0];
                tile.toggleBlocking();
                tile.toggleOccupied();
                blocking = true;
                updateTileBody(tile);
            }
        }
    }

    public void updateTileBody(Tile tile) {
        lighting.updateTileBody(tile.x, tile.y);
    }

    @Override
    public void draw(SpriteBatch batch, int tileSize) {
        batch.draw(sprite, x * tileSize, y * tileSize, tileSize, tileSize);
    }
}
