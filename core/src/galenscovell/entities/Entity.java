
/**
 * ENTITY INTERFACE
 * All entities move(), interpolate(), attack(), and getX()/getY()
 * They also utilize toggleInView(), isInView() and have movement/attack accessors.
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;


public interface Entity {
    public void move(int dx, int dy, boolean possible);
    public void interpolate(double interpolation);
    public void attack(double interpolation, Player player);
    public Sprite getSprite();

    public int getX();
    public int getY();
    public int getCurrentX();
    public int getCurrentY();
    public void toggleInView();
    public boolean isInView();
    public boolean isAttacking();
    public void toggleAttacking();
    public boolean isMoveTime();
    public void resetMoveTime();
    public void incrementMoveTime();
    public int getSightRange();
}