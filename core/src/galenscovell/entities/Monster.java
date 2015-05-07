
/**
 * MONSTER CLASS
 * Utilizes MonsterParser to create Creatures based on JSON data.
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;

import galenscovell.graphics.SpriteSheet;

import java.util.Random;


public class Monster extends Creature {
    private String type;
    private String desc;
    private int level, spriteLocation, hp, vision, speed, evade, defense, attacks, damage, poison;


    public Monster() {
        super();
    }

    public int getLevel() {
        return level;
    }

    public void setup() {
        setSprites();
        setFlags();
        setStats();
    }

    @Override
    public String toString() {
        return "Type: " + type +
                "\n\tDesc: " + desc +
                "\n\tSprite: " + spriteLocation +
                "\n\tHP: " + hp +
                "\n\tVision: " + vision +
                "\n\tSpeed (waited turns before action): " + speed +
                "\n\tEvade: " + evade +
                "\n\tDefense: " + defense +
                "\n\tAttacks: " + attacks +
                "\n\tDamage: " + damage +
                "\n\tPoison: " + poison;
    }

    private void setStats() {
        sightRange = vision;
        movementRequirement = speed;
    }

    private void setFlags() {
        Random random = new Random();
        int flagChance = random.nextInt(100);
        if (flagChance > 80) {
            int flag = random.nextInt(5);
            if (flag == 0) {
                // TOUGH
                hp += 4;
                defense += 4;
                desc += " Looks like it can take a beating.";
            } else if (flag == 1) {
                // AGILE
                evade += 2;
                attacks++;
                damage--;
                desc += " Looks agile.";
            } else if (flag == 2) {
                // BRUISER
                damage += 4;
                desc += " Wouldn't want to be hit by this one.";
            } else if (flag == 3) {
                // WOUNDED
                hp -= 2;
                vision++;
                speed -= 1.0f;
                desc += " Has some cuts and bruises.";
            } else {
                // CAPABLE
                hp += 2;
                evade += 2;
                defense += 2;
                damage++;
                desc += " Seems confident.";
            }
        }
    }

    private void setSprites() {
        SpriteSheet sheet = SpriteSheet.charsheet;
        leftSprites = new Sprite[2];
        rightSprites = new Sprite[2];

        // Populate sprite animation sets
        for (int i = 0; i < 2; i++) {
            leftSprites[i] = new Sprite(sheet.getSprite(i + spriteLocation));
            rightSprites[i] = new Sprite(sheet.getSprite(i + (spriteLocation + 2)));
        }

        currentSet = leftSprites;
        sprite = currentSet[0];
    }
}
