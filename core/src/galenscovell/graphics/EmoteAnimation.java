
/**
 * EMOTEANIMATION CLASS
 *
 */

package galenscovell.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.entities.Entity;
import galenscovell.graphics.SpriteSheet;


public class EmoteAnimation {
    private Sprite[] emote;
    private Entity entity;
    private int x, y;
    private int turns = 0;
    private int skipFrames = 60;
    private int frame = 0;


    public EmoteAnimation(String type, Entity entity) {
        if (type.equals("confused")) {
            this.emote = createAnimation(96);
        } else if (type.equals("surprised")) {
            this.emote = createAnimation(98);
        } else if (type.equals("indifferent")) {
            this.emote = createAnimation(100);
        }
        this.entity = entity;
    }

    public void incrementTurn() {
        turns++;
    }

    public int getTurns() {
        return turns;
    }

    public Sprite[] createAnimation(int startIndex) {
        SpriteSheet sheet = SpriteSheet.fxsheet;
        Sprite[] sprites = new Sprite[2];
        sprites[0] = new Sprite(sheet.getSprite(startIndex));
        sprites[1] = new Sprite(sheet.getSprite(startIndex + 1));
        return sprites;
    }

    public void draw(SpriteBatch spriteBatch, int tileSize) {
        x = entity.getCurrentX();
        y = entity.getCurrentY() - tileSize;
        spriteBatch.draw(emote[frame], x, y, tileSize, tileSize);
        if (skipFrames == 0) {
            if (frame == 0) {
                frame++;
            } else {
                frame--;
            }
            skipFrames = 60;
        } else {
            skipFrames--;
        }
    }
}
