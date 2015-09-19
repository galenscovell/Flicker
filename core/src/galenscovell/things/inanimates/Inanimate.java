package galenscovell.things.inanimates;

import galenscovell.logic.world.Tile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * INANIMATE INTERFACE
 * All inanimates interact(), getType(), draw() and getX()/getY().
 *
 * @author Galen Scovell
 */

public interface Inanimate {
    public Sprite getSprite();
    public int getX();
    public int getY();
    public String interact(Tile tile);
    public String examine();
    public void draw(SpriteBatch batch, int tileSize);
}