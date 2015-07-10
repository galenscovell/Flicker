package galenscovell.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * FOG
 * Displays and animates transparent fog sprite.
 *
 * @author Galen Scovell
 */

public class Fog {
    private Sprite sprite;
    private int x, y;
    private int frame;

    public Fog() {
        this.sprite = new Sprite(new Texture(Gdx.files.internal("textures/fogAlpha.png")));
        this.x = -256;
        this.y = -256;
        this.frame = 3;
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.1f);
        spriteBatch.draw(sprite, x, y, 2048, 2048);
        spriteBatch.setColor(1, 1, 1, 1);

        if (frame == 0) {
            animate();
            frame = 3;
        } else {
            frame--;
        }
    }

    private void animate() {
        x--;
        y--;
        if ((x < -512)) {
            x = -256;
            y = -256;
        }
    }
}