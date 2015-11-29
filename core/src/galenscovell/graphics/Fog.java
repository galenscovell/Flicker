package galenscovell.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

public class Fog {
    private final Sprite sprite;
    private float x, y;
    private int frame;

    public Fog() {
        this.sprite = new Sprite(new Texture(Gdx.files.internal("textures/fogAlpha.png")));
        this.x = -64;
        this.y = -64;
        this.frame = 4;
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.1f);
        spriteBatch.draw(sprite, x, y, 768, 768);
        spriteBatch.setColor(1, 1, 1, 1);
        animate();
    }

    private void animate() {
        if (frame == 0) {
            x -= 0.1f;
            y -= 0.1f;
            if (x < -128) {
                x = -64;
                y = -64;
            }
            frame = 4;
        } else {
            frame--;
        }
    }
}