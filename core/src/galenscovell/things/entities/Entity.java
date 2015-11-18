package galenscovell.things.entities;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.processing.Point;

import java.util.Stack;

public interface Entity {
    String examine();
    int getStat(String key);
    Sprite getSprite();

    void setPosition(int newX, int newY);
    int getX();
    int getY();
    float getCurrentX();
    float getCurrentY();

    void toggleAggressive();
    boolean isAggressive();
    boolean movementTimer();
    void setBeingAttacked();

    void populatePathStack(Stack<Point> path);
    void pushToPathStack(Point p);
    Point nextPathPoint();
    boolean pathStackEmpty();

    void move(int dx, int dy, boolean possible);
    void turn(int dx, int dy);
    void attack(Entity entity);
    void interpolate(double interpolation);
    void animate(double interpolation);
    void draw(SpriteBatch batch, int tileSize, double interpolation, Entity entity);
}