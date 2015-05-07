
/**
 * ENTITY INTERFACE
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;


public interface Entity {
    public Sprite getSprite();

    public void setPosition(int newX, int newY);
    public int getX();
    public int getY();
    public int getCurrentX();
    public int getCurrentY();

    public void toggleInView();
    public boolean isInView();
    public int getSightRange();

    public boolean movementTimer();
    public void toggleMovement();
    public void move(int dx, int dy, boolean possible);
    public void turn(int dx, int dy);
    public void interpolate(double interpolation);

    public boolean isAttacking();
    public void toggleAttack();
    public void attack(double interpolation, Entity entity);
}