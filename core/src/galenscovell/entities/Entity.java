package galenscovell.entities;

import galenscovell.logic.Point;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * ENTITY INTERFACE
 *
 * @author Galen Scovell
 */

public interface Entity {
    public void setPosition(int newX, int newY);
    public int getX();
    public int getY();
    public int getCurrentX();
    public int getCurrentY();

    public void toggleAggressive();
    public boolean isAggressive();
    public boolean movementTimer();
    public void setBeingAttacked();
    public void setAttacking();

    public void setPathStack(Stack<Point> path);
    public Stack<Point> getPathStack();

    public void attack(double interpolation, Entity entity);
    public void interpolate(double interpolation);
    public void move(int dx, int dy, boolean possible);
    public void draw(SpriteBatch batch, int tileSize, double interpolation, Entity entity);

    public int getStat(String key);
}