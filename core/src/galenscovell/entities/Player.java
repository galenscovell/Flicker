
/**
 * PLAYER CLASS
 * Displays player sprite and handles player coordinates.
 * Only one Player instance exists for persistence across levels.
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import galenscovell.graphics.SpriteSheet;


public class Player {
    private int x, y, prevX, prevY, currentX, currentY;
    public int size;
    public Sprite sprite;


    public Player(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
        this.currentX = x;
        this.currentY = y;
        this.size = size;

        SpriteSheet sheet = SpriteSheet.charsheet;
        this.sprite = new Sprite(sheet.getSprite(0));
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

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void draw(double interpolation) {
        // When interpolation is 1, movement animation is complete
        if (interpolation == 1.0) {
            prevX = x;
            prevY = y;
            return;
        }
        currentX = (int) (prevX + ((x - prevX) * interpolation));
        currentY = (int) (prevY + ((y - prevY) * interpolation));
    }
}