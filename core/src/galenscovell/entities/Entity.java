package galenscovell.entities;

import galenscovell.logic.Point;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * ENTITY INTERFACE
 *
 * @author Galen Scovell
 */

public interface Entity {
    public String examine();
    public int getStat(String key);
    public Sprite getSprite();

    public void setPosition(int newX, int newY);
    public int getX();
    public int getY();
    public float getCurrentX();
    public float getCurrentY();

    public void toggleAggressive();
    public boolean isAggressive();
    public boolean movementTimer();
    public void setBeingAttacked();
    public void setAttacking();

    public void setPathStack(Stack<Point> path);
    public Stack<Point> getPathStack();

    public void move(int dx, int dy, boolean possible);
    public void turn(int dx, int dy);
    public void attack(double interpolation, Entity entity);
    public void interpolate(double interpolation);
    public void animate(double interpolation);
    public void draw(SpriteBatch batch, int tileSize, double interpolation, Entity entity);
}