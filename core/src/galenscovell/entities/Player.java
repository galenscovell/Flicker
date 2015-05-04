
/**
 * PLAYER CLASS
 * Displays player sprite and handles player coordinates.
 * Only one Player instance exists for persistence across levels.
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.graphics.SpriteSheet;

import galenscovell.logic.Point;


public class Player {
    private int x, y, prevX, prevY, currentX, currentY;
    private boolean moving, step;

    public Sprite sprite;
    private Sprite[] currentSet;
    private Sprite[] upSprites, downSprites, leftSprites, rightSprites;

    private boolean attacking;
    private int atkX, atkY;


    public Player() {
        SpriteSheet sheet = SpriteSheet.charsheet;
        this.upSprites = new Sprite[3];
        this.downSprites = new Sprite[3];
        this.leftSprites = new Sprite[3];
        this.rightSprites = new Sprite[3];

        // Populate sprite animation sets
        for (int i = 0; i < 3; i++) {
            upSprites[i] = new Sprite(sheet.getSprite(i + 48));
            downSprites[i] = new Sprite(sheet.getSprite(i));
            leftSprites[i] = new Sprite(sheet.getSprite(i + 16));
            rightSprites[i] = new Sprite(sheet.getSprite(i + 32));
        }

        this.currentSet = downSprites;
        this.sprite = currentSet[0];

        this.step = false;
        this.moving = false;
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

    public Point getFacingPoint(int tileSize) {
        int tileX = x / tileSize;
        int tileY = y / tileSize;
        if (currentSet == upSprites) {
            return new Point(tileX, tileY - 1);
        } else if (currentSet == downSprites) {
            return new Point(tileX, tileY + 1);
        } else if (currentSet == leftSprites) {
            return new Point(tileX - 1, tileY);
        } else {
            return new Point(tileX + 1, tileY);
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

    public void turn(int dx, int dy) {
        if (dy < 0 && currentSet != upSprites) {
            currentSet = upSprites;
        } else if (dy > 0 && currentSet != downSprites) {
            currentSet = downSprites;
        } else if (dx < 0 && currentSet != leftSprites) {
            currentSet = leftSprites;
        } else if (dx > 0 && currentSet != rightSprites) {
            currentSet = rightSprites;
        }
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void toggleMovement() {
        if (moving) {
            moving = false;
        } else {
            moving = true;
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

    public void setAttackingCoords(int atkX, int atkY) {
        this.atkX = atkX;
        this.atkY = atkY;
    }

    public void attack(double interpolation, int tileSize) {
        sprite = currentSet[2];
        int diffX = x - atkX;
        int diffY = y - atkY;
        currentX = (int) (prevX - (diffX * interpolation));
        currentY = (int) (prevY - (diffY * interpolation));

        if (interpolation >= 0.4) {
            toggleAttack();
        }
    }

    private void animate(double interpolation) {
        if (interpolation == 0.1) {
            if (step) {
                sprite = currentSet[2];
                step = false;
            } else {
                sprite = currentSet[1];
                step = true;
            }
        } else if (interpolation == 0.9) {
            sprite = currentSet[0];
        } else {
            return;
        }
    }
}