package galenscovell.inanimates;

import galenscovell.graphics.Torchlight;
import galenscovell.logic.Tile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * INANIMATE INTERFACE
 * All inanimates interact(), getType(), draw() and getX()/getY().
 *
 * @author Galen Scovell
 */

public interface Inanimate {
    public int getX();
    public int getY();
    public String interact(Tile tile);
    public String getType();
    public void draw(SpriteBatch batch, int tileSize, Torchlight torchlight);
}