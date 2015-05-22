
/**
 * CREATURE SUPERCLASS
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.graphics.SpriteSheet;


public class Creature implements Entity {
    protected int x, y, prevX, prevY, currentX, currentY;
    protected boolean inView, attacking, beingAttacked;

    protected Sprite sprite;
    protected Sprite[] currentSet;
    protected Sprite[] leftSprites, rightSprites;

    protected int sightRange;
    protected int movementRequirement;
    private int moveTimer;
    private int animateFrames;


    public void setPosition(int newX, int newY) {
        prevX = newX;
        prevY = newY;
        x = newX;
        y = newY;
        currentX = newX;
        currentY = newY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public int getSightRange() {
        return sightRange;
    }

    public void toggleInView() {
        if (inView) {
            inView = false;
        } else {
            inView = true;
        }
    }

    public boolean isInView() {
        return inView;
    }

    public boolean movementTimer() {
        if (moveTimer == movementRequirement) {
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

    public void move(int dx, int dy, boolean possible) {
        turn(dx, dy);
        if (possible) {
            x += dx;
            y += dy;
            if (!inView) {
                prevX = x;
                prevY = y;
            }
        }
    }

    protected void turn(int dx, int dy) {
        if (dx < 0 && currentSet != leftSprites) {
            currentSet = leftSprites;
        } else if (dx > 0 && currentSet != rightSprites) {
            currentSet = rightSprites;
        }
    }

    protected void attack(double interpolation, Entity entity) {
        int diffX = entity.getCurrentX() - x;
        int diffY = entity.getCurrentY() - y;
        if (diffX > 0) {
            currentSet = rightSprites;
        } else if (diffX < 0) {
            currentSet = leftSprites;
        }
        currentX = (int) (prevX + (diffX * interpolation));
        currentY = (int) (prevY + (diffY * interpolation));

        // Attack animation only covers small portion of target's tile
        if (interpolation > 0.3) {
            attacking = false;
        }
    }

    protected void animate(double interpolation) {
        if (animateFrames == 30) {
            if (sprite == currentSet[0]) {
                sprite = currentSet[1];
            } else {
                sprite = currentSet[0];
            }
            animateFrames = 0;
        } else {
            animateFrames++;
        }
    }

    protected void interpolate(double interpolation) {
        animate(interpolation);
        currentX = (int) (prevX + ((x - prevX) * interpolation));
        currentY = (int) (prevY + ((y - prevY) * interpolation));

        if (currentX == x && currentY == y) {
            prevX = x;
            prevY = y;
        }

        if (interpolation >= 0.8) {
            beingAttacked = false;
        }
    }

    public void draw(SpriteBatch batch, int tileSize, double interpolation, Entity entity) {
        interpolate(interpolation);
        if (attacking) {
            attack(interpolation, entity);
        }
        if (beingAttacked) {
            batch.setColor(1, 0, 0, 1.0f - (float) interpolation);
        } else {
            batch.setColor(1, 1, 1, 1);
        }
        batch.draw(sprite, currentX, currentY, tileSize, tileSize);
    }
}