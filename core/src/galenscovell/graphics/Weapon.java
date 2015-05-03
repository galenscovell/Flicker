
/**
 * WEAPON CLASS
 * Handles loading of weapon sprites for attack animations.
 * TODO: Make this Animation based rather than sprite-based.
 */

package galenscovell.graphics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.graphics.SpriteSheet;


public class Weapon {
    private int x, y, playerX, playerY;
    private Sprite sprite;
    private Sprite[] currentSet;
    private Sprite[] upSprites, downSprites, leftSprites, rightSprites;
    private int frame;


    public Weapon() {
        SpriteSheet sheet = SpriteSheet.fxsheet;
        this.upSprites = new Sprite[3];
        this.downSprites = new Sprite[3];
        this.leftSprites = new Sprite[3];
        this.rightSprites = new Sprite[3];

        upSprites[0] = new Sprite(sheet.getSprite(5));
        upSprites[1] = new Sprite(sheet.getSprite(4));
        upSprites[2] = new Sprite(sheet.getSprite(3));

        downSprites[0] = new Sprite(sheet.getSprite(35));
        downSprites[1] = new Sprite(sheet.getSprite(36));
        downSprites[2] = new Sprite(sheet.getSprite(37));

        leftSprites[0] = new Sprite(sheet.getSprite(3));
        leftSprites[1] = new Sprite(sheet.getSprite(19));
        leftSprites[2] = new Sprite(sheet.getSprite(35));

        rightSprites[0] = new Sprite(sheet.getSprite(37));
        rightSprites[1] = new Sprite(sheet.getSprite(21));
        rightSprites[2] = new Sprite(sheet.getSprite(5));
    }

    public int getFrame() {
        return frame;
    }

    public void incrementFrame() {
        frame++;
    }

    public void resetFrame() {
        frame = 0;
    }

    public void setDirection(String dir) {
        if (dir.equals("up")) {
            currentSet = upSprites;
        } else if (dir.equals("down")) {
            currentSet = downSprites;
        } else if (dir.equals("left")) {
            currentSet = leftSprites;
        } else if (dir.equals("right")) {
            currentSet = rightSprites;
        }
    }

    public void setPosition(int playerX, int playerY) {
        this.playerX = playerX;
        this.playerY = playerY;
    }

    public void draw(SpriteBatch spriteBatch, int tileSize, int frame) {
        findCoords(frame, tileSize);
        spriteBatch.draw(currentSet[frame], x, y, tileSize, tileSize);
    }

    private void findCoords(int frame, int tileSize) {
        int halfTile = tileSize / 2;
        int quarterTile = tileSize / 4;
        this.x = playerX;
        this.y = playerY;

        if (currentSet == upSprites) {
            if (frame == 0) {
                x += halfTile;
                y -= quarterTile;
            } else if (frame == 1) {
                y -= halfTile;
            } else {
                x -= halfTile;
                y -= quarterTile;
            }
        } else if (currentSet == downSprites) {
            if (frame == 0) {
                x -= halfTile;
                y += quarterTile;
            } else if (frame == 1) {
                y += halfTile;
            } else {
                x += halfTile;
                y += quarterTile;
            }
        } else if (currentSet == leftSprites) {
            if (frame == 0) {
                x -= quarterTile;
                y -= halfTile;
            } else if (frame == 1) {
                x -= halfTile;
            } else {
                x -= quarterTile;
                y += halfTile;
            }
        } else if (currentSet == rightSprites) {
            if (frame == 0) {
                x += quarterTile;
                y += halfTile;
            } else if (frame == 1) {
                x += halfTile;
            } else {
                x += quarterTile;
                y -= halfTile;
            }
        }
    }
}

