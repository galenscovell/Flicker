
/**
 * WEAPON CLASS
 * Handles loading of weapon sprites for attack animations.
 */

package galenscovell.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.graphics.SpriteSheet;


public class Weapon {
    private int x, y;
    private Sprite sprite;
    private Sprite[] sprites;
    private float stateTime;
    private boolean drawn;


    public Weapon(String type) {
        SpriteSheet sheet = SpriteSheet.fxsheet;

        int i = 0;
        if (type.equals("dagger")) {
            i = 0;
        } else if (type.equals("TODO")) {
            i = 3;
        }

        this.sprites = new Sprite[4];
        sprites[0] = new Sprite(sheet.getSprite(i + 0));
        sprites[1] = new Sprite(sheet.getSprite(i + 1));
        sprites[2] = new Sprite(sheet.getSprite(i + 2));
        sprites[3] = new Sprite(sheet.getSprite(i + 3));

        this.stateTime = 0.0f;

    }

    public boolean isDrawn() {
        return drawn;
    }

    public void setPosition(String dir, int tileSize, int playerX, int playerY) {
        this.x = playerX;
        this.y = playerY;

        if (dir.equals("up")) {
            this.sprite = sprites[0];
            y -= tileSize / 3;
        } else if (dir.equals("down")) {
            this.sprite = sprites[1];
            y += tileSize / 3;
        } else if (dir.equals("left")) {
            this.sprite = sprites[2];
            x -= tileSize / 4;
        } else if (dir.equals("right")) {
            this.sprite = sprites[3];
            x += tileSize / 4;
        }
        drawn = true;
    }

    public void draw(SpriteBatch spriteBatch, int tileSize) {
        spriteBatch.draw(sprite, x, y, tileSize, tileSize);
        stateTime += Gdx.graphics.getDeltaTime();

        if (stateTime >= 0.5) {
            drawn = false;
            stateTime = 0.0f;
        }
    }
}

