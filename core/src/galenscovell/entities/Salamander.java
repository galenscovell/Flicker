
/**
 * SALAMANDER CLASS
 * Handles Salamander creature sprites and stats.
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;

import galenscovell.graphics.SpriteSheet;


public class Salamander extends Creature {

    public Salamander() {
        super();
        setSprites();
        setStats();
    }

    private void setStats() {
        speed = 2;
        strength = 2;
        sightRange = 5;
    }

    private void setSprites() {
        SpriteSheet sheet = SpriteSheet.charsheet;
        leftSprites = new Sprite[2];
        rightSprites = new Sprite[2];

        // Populate sprite animation sets
        for (int i = 0; i < 2; i++) {
            leftSprites[i] = new Sprite(sheet.getSprite(i + 80));
            rightSprites[i] = new Sprite(sheet.getSprite(i + 82));
        }

        currentSet = leftSprites;
        sprite = currentSet[0];
    }
}
