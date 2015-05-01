
/**
 * DEAD CLASS
 * Enemy death sprite loading and location.
 */

package galenscovell.entities;

import galenscovell.graphics.SpriteSheet;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;


public class Dead {
    public int x, y, tileSize;
    public Sprite sprite;


    public Dead(int x, int y) {
        this.x = x;
        this.y = y;
        sprite = setSprite();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private Sprite setSprite() {
        SpriteSheet sheet = SpriteSheet.charsheet;
        Random random = new Random();

        int choice = random.nextInt(4);
        if (choice == 0) {
            return new Sprite(sheet.getSprite(144));
        } else if (choice == 1) {
            return new Sprite(sheet.getSprite(145));
        } else if (choice == 2) {
            return new Sprite(sheet.getSprite(146));
        } else {
            return new Sprite(sheet.getSprite(147));
        }
    }
}