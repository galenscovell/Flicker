package galenscovell.things.entities;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.processing.Point;

import java.util.*;

public class Creature implements Entity {
    private int x, y, prevX, prevY;
    private int moveTimer, animateFrames, frame;
    private float currentX, currentY;
    private Stack<Point> pathStack;
    private boolean aggressive, beingAttacked;

    protected Sprite[] currentSet, leftSprites, rightSprites;
    protected String title, description;
    protected Map<String, Integer> stats = new HashMap<String, Integer>();


    /***************************************************
     * Stats and Description
     */
    @Override
    public String toString() {
        return title;
    }

    @Override
    public Sprite getSprite() {
        return currentSet[0];
    }

    @Override
    public String examine() {
        return description;
    }

    @Override
    public int getStat(String key) {
        return stats.get(key);
    }

    @Override
    public int doPhysicalDamage() {
        return getStat("STR");
    }

    @Override
    public void takePhysicalDamage(int damage) {
        stats.put("HP", getStat("HP") - damage);
    }

    @Override
    public boolean isDead() {
        return getStat("HP") <= 0;
    }


    /***************************************************
     * Location
     */
    @Override
    public void setPosition(int newX, int newY) {
        prevX = newX;
        prevY = newY;
        x = newX;
        y = newY;
        currentX = newX;
        currentY = newY;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public float getCurrentX() {
        return currentX;
    }

    @Override
    public float getCurrentY() {
        return currentY;
    }


    /***************************************************
     * State
     */
    public void toggleAggressive() {
        aggressive = !aggressive;
    }

    @Override
    public boolean isAggressive() {
        return aggressive;
    }

    @Override
    public void setBeingAttacked() {
        beingAttacked = true;
    }

    @Override
    public void attack(Entity entity) {
        entity.setBeingAttacked();
    }


    /***************************************************
     * Movement
     */
    @Override
    public boolean movementTimer() {
        if (moveTimer == 2) {
            moveTimer = 0;
            return true;
        }
        moveTimer++;
        return false;
    }

    @Override
    public void populatePathStack(Stack<Point> path) {
        this.pathStack = path;
    }

    @Override
    public void pushToPathStack(Point p) {
        pathStack.push(p);
    }

    @Override
    public Point nextPathPoint() {
        return pathStack.pop();
    }

    @Override
    public boolean pathStackEmpty() {
        return pathStack == null || pathStack.isEmpty();
    }

    @Override
    public void move(int dx, int dy, boolean possible, boolean slide) {
        if (!slide) {
            turn(dx, dy);
        }
        if (possible) {
            x += dx;
            y += dy;
        }
    }

    @Override
    public void turn(int dx, int dy) {
        if (dx < 0 && currentSet != leftSprites) {
            currentSet = leftSprites;
        } else if (dx > 0 && currentSet != rightSprites) {
            currentSet = rightSprites;
        }
    }


    /***************************************************
     * Rendering
     */
    @Override
    public void interpolate(double interpolation) {
        animate(interpolation);
        currentX = (float) (prevX + ((x - prevX) * interpolation));
        currentY = (float) (prevY + ((y - prevY) * interpolation));
        if (currentX == x && currentY == y) {
            prevX = x;
            prevY = y;
        }
        if (interpolation == 1) {
            beingAttacked = false;
        }
    }

    @Override
    public void animate(double interpolation) {
        if (animateFrames == 30) {
            if (frame == 0) {
                frame = 1;
            } else {
                frame = 0;
            }
            animateFrames = 0;
        } else {
            animateFrames++;
        }
    }

    @Override
    public void draw(SpriteBatch batch, int tileSize, double interpolation, Entity entity) {
        float offsetTileHeight = currentY - (tileSize / 3);
        interpolate(interpolation);
        if (beingAttacked) {
            batch.setColor(1, (float) interpolation, (float) interpolation, 1);
            batch.draw(currentSet[frame], currentX, offsetTileHeight, tileSize, tileSize);
            batch.setColor(1, 1, 1, 1);
        } else {
            batch.draw(currentSet[frame], currentX, offsetTileHeight, tileSize, tileSize);
        }
    }
}