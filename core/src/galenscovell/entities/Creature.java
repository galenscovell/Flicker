
/**
 * CREATURE SUPERCLASS
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.graphics.SpriteSheet;


public class Creature implements Entity {
    protected int x, y, prevX, prevY, currentX, currentY;
    protected boolean inView, moving, attacking;

    protected Sprite sprite;
    protected Sprite[] currentSet;
    protected Sprite[] leftSprites, rightSprites;

    protected int sightRange;


    public Sprite getSprite() {
        return sprite;
    }

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

    public void toggleMovement() {
        if (moving) {
            moving = false;
        } else {
            moving = true;
        }
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

    public void interpolate(double interpolation) {
        currentX = (int) (prevX + ((x - prevX) * interpolation));
        currentY = (int) (prevY + ((y - prevY) * interpolation));

        if (currentX == x && currentY == y) {
            prevX = x;
            prevY = y;
            moving = false;
        }

        if (moving) {
            animate(interpolation);
        } else {
            sprite = currentSet[0];
            return;
        }
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void toggleAttack() {
        if (attacking) {
            attacking = false;
        } else {
            attacking = true;
        }
    }

    public void attack(double interpolation, Entity entity) {
        int diffX = entity.getX() - x;
        int diffY = entity.getY() - y;
        if (diffX > 0) {
            currentSet = rightSprites;
        } else if (diffX < 0) {
            currentSet = leftSprites;
        }
        currentX = (int) (prevX + (diffX * interpolation));
        currentY = (int) (prevY + (diffY * interpolation));

        // Attack animation only covers half of player's tile
        if (interpolation >= 0.6) {
            toggleAttack();
        }
    }

    public void animate(double interpolation) {
        if (interpolation == 0.1) {
            sprite = currentSet[1];
        } else if (interpolation == 0.9) {
            sprite = currentSet[0];
        } else {
            return;
        }
    }
}