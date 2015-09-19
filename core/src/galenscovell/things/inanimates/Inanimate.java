package galenscovell.things.inanimates;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.world.Tile;

public interface Inanimate {
    public Sprite getSprite();
    public int getX();
    public int getY();
    public String interact(Tile tile);
    public String examine();
    public void draw(SpriteBatch batch, int tileSize);
}