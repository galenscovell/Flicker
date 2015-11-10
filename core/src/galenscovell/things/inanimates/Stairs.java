package galenscovell.things.inanimates;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.util.ResourceManager;
import galenscovell.world.Tile;

public class Stairs implements Inanimate {
    private final Sprite sprite;
    private int x, y;
    private boolean blocking;

    public Stairs(int x, int y) {
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(ResourceManager.inanimateAtlas.createSprite("stairs0"));
        sprite.flip(false, true);
        this.blocking = false;
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
        batch.draw(sprite, x * tileSize, y * tileSize, tileSize, tileSize);
    }
}