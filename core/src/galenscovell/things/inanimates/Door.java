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
        return this.sprite;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void displayEvent() {

    }

    @Override
    public String examine() {
        return "A closed door.";
    }

    @Override
    public String interact(Tile tile) {
        if (this.blocking) {
            this.sprite = this.sprites[1];
            tile.toggleBlocking();
            tile.toggleOccupied();
            this.blocking = false;
            this.updateTileBody(tile);
            return "The door opens.";
        } else {
            this.sprite = this.sprites[0];
            tile.toggleBlocking();
            tile.toggleOccupied();
            this.blocking = true;
            this.updateTileBody(tile);
            return "The door closes.";
        }
    }

    public void updateTileBody(Tile tile) {
        this.lighting.updateTileBody(tile.x, tile.y);
    }

    @Override
    public void draw(SpriteBatch batch, int tileSize) {
        batch.draw(this.sprite, this.x * tileSize, this.y * tileSize, tileSize, tileSize);
    }
}
