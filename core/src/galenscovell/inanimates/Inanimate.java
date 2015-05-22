
/**
 * INANIMATE INTERFACE
 * All inanimates have interact(), getType(), draw() and getX()/getY().
 */

package galenscovell.inanimates;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.graphics.Torchlight;
import galenscovell.logic.Tile;


public interface Inanimate {
    public int getX();
    public int getY();
    public String interact(Tile tile);
    public String getType();
    public void draw(SpriteBatch batch, int tileSize, Torchlight torchlight);
}