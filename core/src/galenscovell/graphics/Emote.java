
/**
 * EMOTE CLASS
 *
 */

package galenscovell.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.entities.Entity;
import galenscovell.graphics.SpriteSheet;


public class Emote {
    private int x, y;
    private Animation current;
    private Animation confused, surprised, indifferent;
    private float stateTime = 0.0f;


    public Emote() {
        SpriteSheet sheet = SpriteSheet.fxsheet;
        Sprite[] confusedSprites = new Sprite[2];
        confusedSprites[0] = new Sprite(sheet.getSprite(96));
        confusedSprites[1] = new Sprite(sheet.getSprite(97));
        this.confused = new Animation(0.05f, confusedSprites);

        Sprite[] surprisedSprites = new Sprite[2];
        surprisedSprites[0] = new Sprite(sheet.getSprite(98));
        surprisedSprites[1] = new Sprite(sheet.getSprite(99));
        this.surprised = new Animation(0.05f, surprisedSprites);

        Sprite[] indifferentSprites = new Sprite[2];
        indifferentSprites[0] = new Sprite(sheet.getSprite(100));
        indifferentSprites[1] = new Sprite(sheet.getSprite(101));
        this.indifferent = new Animation(0.05f, indifferentSprites);
    }

    public void emote(String type) {
        if (type.equals("confused")) {
            current = confused;
        } else if (type.equals("surprised")) {
            current = surprised;
        } else if (type.equals("indifferent")) {
            current = indifferent;
        }
        stateTime = 0.0f;
    }

    public void draw(SpriteBatch spriteBatch, Entity entity, int tileSize) {
        x = entity.getCurrentX();
        y = entity.getCurrentY() - tileSize;
        spriteBatch.draw(current.getKeyFrame(stateTime, true), x, y, tileSize, tileSize);
        stateTime += Gdx.graphics.getDeltaTime();
    }
}
