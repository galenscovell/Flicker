package galenscovell.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
    private int x, y, size;
    private int frameSkip;

    public Fog() {
        Texture texture = new Texture(Gdx.files.internal("textures/fogAlpha.png"));
        this.sprite = new Sprite(texture);

        this.x = -1024;
        this.y = -1024;
        this.size = 8192;
        this.frameSkip = 4;
    }

    public void render(SpriteBatch spriteBatch) {
        // Save original batch color
        Color c = new Color(spriteBatch.getColor());
        // Set new color for rendering fog sprite
        spriteBatch.setColor(0.7f, 0.7f, 0.7f, 0.08f);
        spriteBatch.draw(sprite, x, y, size, size);
        // Return color to original
        spriteBatch.setColor(c);

        if (frameSkip == 0) {
            animate();
            frameSkip = 4;
        } else {
            frameSkip--;
        }
    }

    private void animate() {
        x--;
        y--;
        if ((x < -size / 4)) {
            x = -1024;
            y = -1024;
        }
    }
}