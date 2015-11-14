package galenscovell.things.inanimates;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.world.Tile;

public interface Inanimate {
    Sprite getSprite();
    int getX();
    int getY();
    void interact(Tile tile);
    String examine();
    void draw(SpriteBatch batch, int tileSize);
}