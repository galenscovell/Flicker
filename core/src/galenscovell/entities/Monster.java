
/**
 * MONSTER CLASS
 * Utilizes MonsterParser to create entities based on JSON data.
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;

import galenscovell.graphics.SpriteSheet;


public class Monster extends Creature {
    private String type;
    private int sprite_location;
    private int vision;
    private String desc;


    public Monster() {
        super();
        setSprites();
        setStats();
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "Type: " + type + ", Desc: " + desc;
    }

    private void setStats() {
        sightRange = vision;
    }

    private void setSprites() {
        SpriteSheet sheet = SpriteSheet.charsheet;
        leftSprites = new Sprite[2];
        rightSprites = new Sprite[2];

        // Populate sprite animation sets
        for (int i = 0; i < 2; i++) {
            leftSprites[i] = new Sprite(sheet.getSprite(i + sprite_location));
            rightSprites[i] = new Sprite(sheet.getSprite(i + sprite_location));
        }

        currentSet = leftSprites;
        sprite = currentSet[0];
    }
}
