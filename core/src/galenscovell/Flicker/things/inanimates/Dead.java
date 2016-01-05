package galenscovell.flicker.things.inanimates;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.flicker.util.ResourceManager;
import galenscovell.flicker.world.Tile;

import java.util.Random;

public class Dead implements Inanimate {
    private final Sprite sprite;
    private int x, y;
    private boolean blocking;

    public Dead(int x, int y) {
        this.x = x;
        this.y = y;
        Random random = new Random();
        int choice = random.nextInt(4);
        this.sprite = new Sprite(ResourceManager.inanimateAtlas.createSprite("corpse" + choice));
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
    public String examine() {
        return "A corpse lies here.";
    }

    @Override
    public void interact(Tile tile) {

    }

    @Override
    public void draw(SpriteBatch batch, int tileSize) {
        batch.draw(sprite, x * tileSize, y * tileSize, tileSize, tileSize);
    }
}