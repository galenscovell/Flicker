package galenscovell.flicker.things.entities;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.flicker.processing.Point;

import java.util.Stack;

public interface Entity {
    Sprite getSprite();
    String examine();
    int getStat(Stats stat);
    int doPhysicalDamage();
    void takePhysicalDamage(int damage);
    boolean isDead();

    void setPosition(int newX, int newY);
    int getX();
    int getY();
    float getCurrentX();
    float getCurrentY();

    void toggleAggressive();
    boolean isAggressive();
    void setBeingAttacked();

    void populatePathStack(Stack<Point> path);
    void pushToPathStack(Point p);
    Point nextPathPoint();
    boolean pathStackEmpty();
    void move(int dx, int dy, boolean possible, boolean slide);
    void turn(int dx, int dy);

    void interpolate(double interpolation);
    void animate(double interpolation);
    void draw(SpriteBatch batch, int tileSize, double interpolation, Entity entity);
}