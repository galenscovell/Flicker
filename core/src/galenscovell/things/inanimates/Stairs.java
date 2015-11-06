package galenscovell.things.inanimates;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.util.ResourceManager;
import galenscovell.world.Tile;

public class Stairs implements Inanimate {
    private int x, y;
    private Sprite sprite;
    private boolean blocking;

    public Stairs(int x, int y) {
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(ResourceManager.inanimateAtlas.createSprite("stairs0"));
        sprite.flip(false, true);
        this.blocking = false;
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

    public void displayEvent() {

    }

    public String examine() {
        return "Stairs descend deeper.";
    }

    public String interact(Tile tile) {
        return "Descending stairs...";
    }

    public void draw(SpriteBatch batch, int tileSize) {
        batch.draw(sprite, x * tileSize, y * tileSize, tileSize, tileSize);
    }
}