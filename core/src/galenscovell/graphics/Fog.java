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
    private float x, y;
    private int frame;

    public Fog() {
        this.sprite = new Sprite(new Texture(Gdx.files.internal("textures/fogAlpha.png")));
        this.x = -64;
        this.y = -64;
        this.frame = 3;
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.05f);
        spriteBatch.draw(sprite, x, y, 512, 512);
        spriteBatch.setColor(1, 1, 1, 1);

        if (frame == 0) {
            animate();
            frame = 3;
        } else {
            frame--;
        }
    }

    private void animate() {
        x -= 0.1f;
        y -= 0.1f;
        if ((x < -128)) {
            x = -64;
            y = -64;
        }
    }
}