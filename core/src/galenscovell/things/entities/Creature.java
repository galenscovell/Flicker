package galenscovell.things.entities;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.processing.Point;

import java.util.*;

public class Creature implements Entity {
    private int x, y, prevX, prevY;
    private float currentX, currentY;
    private Stack<Point> pathStack;
    private int moveTimer, animateFrames, frame;
    private boolean aggressive, attacking, beingAttacked;

    protected Sprite[] currentSet, leftSprites, rightSprites;
    protected String title, description;
    protected Map<String, Integer> stats = new HashMap<String, Integer>();

    public String toString() {
        return title;
    }

    public String examine() {
        return description;
    }

    public Sprite getSprite() {
        return currentSet[0];
    }

    public int getStat(String key) {
        return stats.get(key);
    }

    public void setPosition(int newX, int newY) {
        prevX = newX;
        prevY = newY;
        x = newX;
        y = newY;
        currentX = (float) newX;
        currentY = (float) newY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getCurrentX() {
        return currentX;
    }

    public float getCurrentY() {
        return currentY;
    }

    public void toggleAggressive() {
        aggressive = !aggressive;
    }

    public boolean isAggressive() {
        return aggressive;
    }

    public boolean movementTimer() {
        if (moveTimer == stats.get("speed")) {
            moveTimer = 0;
            return true;
        }
        moveTimer++;
        return false;
    }

    public void setBeingAttacked() {
        beingAttacked = true;
    }

    public void setAttacking() {
        attacking = true;
    }

    public void setPathStack(Stack<Point> path) {
        this.pathStack = path;
    }

    public Stack<Point> getPathStack() {
        return pathStack;
    }

    public void move(int dx, int dy, boolean possible) {
        turn(dx, dy);
        if (possible) {
            x += dx;
            y += dy;
        }
    }

    public void turn(int dx, int dy) {
        if (dx < 0 && currentSet != leftSprites) {
            currentSet = leftSprites;
        } else if (dx > 0 && currentSet != rightSprites) {
            currentSet = rightSprites;
        }
    }

    public void attack(double interpolation, Entity entity) {

    }

    public void interpolate(double interpolation) {
        animate(interpolation);
        currentX = (float) (prevX + ((x - prevX) * interpolation));
        currentY = (float) (prevY + ((y - prevY) * interpolation));
        if (currentX == (float) x && currentY == (float) y) {
            prevX = x;
            prevY = y;
        }
        if (interpolation == 1) {
            beingAttacked = false;
        }
    }

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

    public void draw(SpriteBatch batch, int tileSize, double interpolation, Entity entity) {
        float offsetTileHeight = currentY - (tileSize / 3);
        interpolate(interpolation);
        if (attacking) {
            attack(interpolation, entity);
        }
        if (beingAttacked) {
            batch.setColor(1, (float) interpolation, (float) interpolation, 1);
            batch.draw(currentSet[frame], currentX, offsetTileHeight, tileSize, tileSize);
            batch.setColor(1, 1, 1, 1);
        } else {
            batch.draw(currentSet[frame], currentX, offsetTileHeight, tileSize, tileSize);
        }
    }
}