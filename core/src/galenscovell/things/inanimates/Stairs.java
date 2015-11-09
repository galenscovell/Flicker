package galenscovell.things.inanimates;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.util.ResourceManager;
import galenscovell.world.Tile;

public class Stairs implements Inanimate {
    private final int x;
    private final int y;
    private final Sprite sprite;
    private final boolean blocking;

    public Stairs(int x, int y) {
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(ResourceManager.inanimateAtlas.createSprite("stairs0"));
        this.sprite.flip(false, true);
        this.blocking = false;
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
        return "Stairs descend deeper.";
    }

    @Override
    public String interact(Tile tile) {
        return "Descending stairs...";
    }

    @Override
    public void draw(SpriteBatch batch, int tileSize) {
        batch.draw(this.sprite, this.x * tileSize, this.y * tileSize, tileSize, tileSize);
    }
}