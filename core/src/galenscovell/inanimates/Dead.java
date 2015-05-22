
/**
 * DEAD CLASS
 * Enemy death sprite loading and location.
 */

package galenscovell.inanimates;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.graphics.SpriteSheet;
import galenscovell.graphics.Torchlight;
import galenscovell.logic.Tile;

import java.util.Random;


public class Dead implements Inanimate {
    private int x, y;
    private Sprite sprite;
    private boolean blocking;


    public Dead(int x, int y) {
        this.x = x;
        this.y = y;
        SpriteSheet sheet = SpriteSheet.charsheet;
        Random random = new Random();
        int choice = random.nextInt(4);
        if (choice == 0) {
            this.sprite = new Sprite(sheet.getSprite(64));
        } else if (choice == 1) {
            this.sprite = new Sprite(sheet.getSprite(65));
        } else if (choice == 2) {
            this.sprite = new Sprite(sheet.getSprite(66));
        } else {
            this.sprite = new Sprite(sheet.getSprite(67));
        }

        this.blocking = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return "Dead";
    }

    public String interact(Tile tile) {
        return "A corpse lies here.";
    }

    public void draw(SpriteBatch batch, int tileSize, Torchlight torchlight) {
        batch.draw(sprite, x * tileSize, y * tileSize, tileSize, tileSize);
    }
}