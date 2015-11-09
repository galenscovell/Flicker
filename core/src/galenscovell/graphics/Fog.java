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
        this.frame = 3;
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.05f);
        spriteBatch.draw(this.sprite, this.x, this.y, 512, 512);
        spriteBatch.setColor(1, 1, 1, 1);

        if (this.frame == 0) {
            this.animate();
            this.frame = 3;
        } else {
            this.frame--;
        }
    }

    private void animate() {
        this.x -= 0.1f;
        this.y -= 0.1f;
        if (this.x < -128) {
            this.x = -64;
            this.y = -64;
        }
    }
}