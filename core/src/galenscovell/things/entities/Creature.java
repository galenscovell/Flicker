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
        return this.title;
    }

    @Override
    public String examine() {
        return this.description;
    }

    @Override
    public Sprite getSprite() {
        return this.currentSet[0];
    }

    @Override
    public int getStat(String key) {
        return this.stats.get(key);
    }

    @Override
    public void setPosition(int newX, int newY) {
        this.prevX = newX;
        this.prevY = newY;
        this.x = newX;
        this.y = newY;
        this.currentX = (float) newX;
        this.currentY = (float) newY;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public float getCurrentX() {
        return this.currentX;
    }

    @Override
    public float getCurrentY() {
        return this.currentY;
    }

    @Override
    public void toggleAggressive() {
        this.aggressive = !this.aggressive;
    }

    @Override
    public boolean isAggressive() {
        return this.aggressive;
    }

    @Override
    public boolean movementTimer() {
        if (this.moveTimer == this.stats.get("speed")) {
            this.moveTimer = 0;
            return true;
        }
        this.moveTimer++;
        return false;
    }

    @Override
    public void setBeingAttacked() {
        this.beingAttacked = true;
    }

    @Override
    public void setAttacking() {
        this.attacking = true;
    }

    @Override
    public void populatePathStack(Stack<Point> path) {
        this.pathStack = path;
    }

    @Override
    public void pushToPathStack(Point p) {
        this.pathStack.push(p);
    }

    @Override
    public Point nextPathPoint() {
        return this.pathStack.pop();
    }

    @Override
    public boolean pathStackEmpty() {
        return this.pathStack == null || this.pathStack.isEmpty();
    }

    @Override
    public void move(int dx, int dy, boolean possible) {
        this.turn(dx, dy);
        if (possible) {
            this.x += dx;
            this.y += dy;
        }
    }

    @Override
    public void turn(int dx, int dy) {
        if (dx < 0 && this.currentSet != this.leftSprites) {
            this.currentSet = this.leftSprites;
        } else if (dx > 0 && this.currentSet != this.rightSprites) {
            this.currentSet = this.rightSprites;
        }
    }

    @Override
    public void attack(double interpolation, Entity entity) {

    }

    @Override
    public void interpolate(double interpolation) {
        this.animate(interpolation);
        this.currentX = (float) (this.prevX + (this.x - this.prevX) * interpolation);
        this.currentY = (float) (this.prevY + (this.y - this.prevY) * interpolation);
        if (this.currentX == (float) this.x && this.currentY == (float) this.y) {
            this.prevX = this.x;
            this.prevY = this.y;
        }
        if (interpolation == 1) {
            this.beingAttacked = false;
        }
    }

    @Override
    public void animate(double interpolation) {
        if (this.animateFrames == 30) {
            if (this.frame == 0) {
                this.frame = 1;
            } else {
                this.frame = 0;
            }
            this.animateFrames = 0;
        } else {
            this.animateFrames++;
        }
    }

    @Override
    public void draw(SpriteBatch batch, int tileSize, double interpolation, Entity entity) {
        float offsetTileHeight = this.currentY - tileSize / 3;
        this.interpolate(interpolation);
        if (this.attacking) {
            this.attack(interpolation, entity);
        }
        if (this.beingAttacked) {
            batch.setColor(1, (float) interpolation, (float) interpolation, 1);
            batch.draw(this.currentSet[this.frame], this.currentX, offsetTileHeight, tileSize, tileSize);
            batch.setColor(1, 1, 1, 1);
        } else {
            batch.draw(this.currentSet[this.frame], this.currentX, offsetTileHeight, tileSize, tileSize);
        }
    }
}