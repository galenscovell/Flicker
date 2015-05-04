
/**
 * CREATURE SUPERCLASS
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import galenscovell.graphics.SpriteSheet;

import com.badlogic.gdx.graphics.g2d.Sprite;


public class Creature implements Entity {
    private int x, y, prevX, prevY, currentX, currentY;
    private int spriteNumber, waitFrames;
    private boolean inView;
    private boolean isAttacking;

    public Sprite sprite;
    protected Sprite[] currentSet;
    protected Sprite[] leftSprites, rightSprites;

    protected int speed, strength, sightRange;
    private int moveTime;


    public Creature(int x, int y) {
        this.prevX = x;
        this.prevY = y;
        this.x = x;
        this.y = y;
        this.currentX = x;
        this.currentY = y;

        this.spriteNumber = 0;
        this.waitFrames = 15;
        this.moveTime = 0;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void move(int dx, int dy, boolean possible) {
        if (dx < 0) {
            currentSet = leftSprites;
        } else if (dx > 0) {
            currentSet = rightSprites;
        }

        if (possible) {
            animate();
            x += dx;
            y += dy;
        }
    }

    public void interpolate(double interpolation) {
        animate();
        currentX = (int) (prevX + ((x - prevX) * interpolation));
        currentY = (int) (prevY + ((y - prevY) * interpolation));

        if (currentX == x && currentY == y) {
            prevX = x;
            prevY = y;
        }
    }

    public void attack(double interpolation, Player player) {
        int diffX = player.getX() - x;
        int diffY = player.getY() - y;
        if (diffX > 0) {
            currentSet = rightSprites;
        } else if (diffX < 0) {
            currentSet = leftSprites;
        }
        animate();
        currentX = (int) (prevX + (diffX * interpolation));
        currentY = (int) (prevY + (diffY * interpolation));

        // Attack animation only covers half of player's tile
        if (interpolation >= 0.6) {
            toggleAttacking();
        }
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

    public boolean isAttacking() {
        return isAttacking;
    }

    public void toggleAttacking() {
        if (isAttacking) {
            isAttacking = false;
        } else {
            isAttacking = true;
        }
    }

    public boolean isMoveTime() {
        return moveTime == speed;
    }

    public void resetMoveTime() {
        moveTime = 0;
    }

    public void incrementMoveTime() {
        moveTime++;
    }

    private void animate() {
        if (waitFrames == 0) {
            spriteNumber++;
            waitFrames = 20;
            if (spriteNumber > 1) {
                spriteNumber = 0;
            }
        } else {
            waitFrames--;
        }
        sprite = currentSet[spriteNumber];
    }
}