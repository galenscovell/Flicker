
/**
 * ENTITY INTERFACE
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public interface Entity {
    public void setPosition(int newX, int newY);
    public int getX();
    public int getY();
    public int getCurrentX();
    public int getCurrentY();

    public void toggleInView();
    public boolean isInView();
    public boolean movementTimer();
    public void setBeingAttacked();
    public void setAttacking();

    public void move(int dx, int dy, boolean possible);
    public void draw(SpriteBatch batch, int tileSize, double interpolation, Entity entity);

    public int getStat(String key);
}