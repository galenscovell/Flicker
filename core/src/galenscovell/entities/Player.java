
/**
 * PLAYER CLASS
 * Displays player sprite and handles player coordinates.
 * Only one Player instance exists for persistence across levels.
 */

package galenscovell.entities;

import galenscovell.graphics.SpriteSheet;

import com.badlogic.gdx.graphics.g2d.Sprite;


public class Player {
    private int x, y, prevX, prevY, currentX, currentY;
    private int spriteFrame, waitFrames;
    public Sprite sprite;
    private Sprite[] currentSet;
    private Sprite[] upSprites, downSprites, leftSprites, rightSprites;


    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
        this.currentX = x;
        this.currentY = y;

        SpriteSheet sheet = SpriteSheet.charsheet;

        this.upSprites = new Sprite[4];
        this.downSprites = new Sprite[4];
        this.leftSprites = new Sprite[4];
        this.rightSprites = new Sprite[4];

        // Populate sprite animation sets
        for (int i = 0; i < 4; i++) {
            upSprites[i] = new Sprite(sheet.getSprite(i + 48));
            downSprites[i] = new Sprite(sheet.getSprite(i));
            leftSprites[i] = new Sprite(sheet.getSprite(i + 16));
            rightSprites[i] = new Sprite(sheet.getSprite(i + 32));
        }

        this.currentSet = downSprites;
        this.sprite = currentSet[0];
        this.spriteFrame = 0;
        this.waitFrames = 20;
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

    public void turn(int dx, int dy) {
        if (dy < 0) {
            currentSet = upSprites;
        } else if (dy > 0) {
            currentSet = downSprites;
        } else if (dx < 0) {
            currentSet = leftSprites;
        } else if (dx > 0) {
            currentSet = rightSprites;
        }
    }

    public void move(int dx, int dy) {
        turn(dx, dy);
        animate(currentSet);
        x += dx;
        y += dy;
    }

    public void draw(double interpolation) {
        animate(currentSet);
        // When interpolation is 1, movement animation is complete
        if (interpolation == 1.0) {
            prevX = x;
            prevY = y;
            return;
        }
        currentX = (int) (prevX + ((x - prevX) * interpolation));
        currentY = (int) (prevY + ((y - prevY) * interpolation));
    }

    private void animate(Sprite[] currentSet) {
        if (waitFrames == 0) {
            spriteFrame++;
            waitFrames = 20;
            if (spriteFrame > 3) {
                spriteFrame = 0;
            }
        } else {
            waitFrames--;
        }
        sprite = currentSet[spriteFrame];
    }
}