
/**
 * MONSTER CLASS
 * Utilizes MonsterParser to create entities based on JSON data.
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;

import galenscovell.graphics.SpriteSheet;

import java.util.Random;


public class Monster extends Creature {
    private String type;
    private int hp;
    private int sprite_location;
    private int vision;
    private int evade;
    private int defense;
    private int attacks;
    private int damage;
    private int poison;
    private int curse;
    private int petrify;
    private int silence;
    private String desc;


    public Monster() {
        super();
        setSprites();
        setFlags();
        setStats();
    }

    @Override
    public String toString() {
        return "Type: " + type + "\n\tSprite: " + sprite_location + "\n\tHP: " + hp + "\n\tSight range: " + vision + "\n\tEvade: " + evade + "\n\tDefense: " + defense + "\n\tAttacks: " + attacks + "\n\tDamage: " + damage + "\n\tPoison: " + poison + "\n\tCurse: " + curse + "\n\tPetrify: " + petrify + "\n\tSilence: " + silence + "\n\tDesc: " + desc;
    }

    private void setStats() {
        sightRange = vision;
    }

    private void setFlags() {
        Random random = new Random();
        int flagChance = random.nextInt(100);
        if (flagChance > 80) {
            int flag = random.nextInt(5);
            switch (flag) {
                case 0:
                    // TOUGH
                    hp += 4;
                    defense += 4;
                    desc += " Looks tough.";
                    break;
                case 1:
                    // AGILE
                    evade += 2;
                    attacks++;
                    damage--;
                    desc += " Looks agile.";
                    break;
                case 2:
                    // HEAVYHITTER
                    damage += 4;
                    desc += " Wouldn't want to be hit by this one.";
                    break;
                case 3:
                    // SCOUT
                    vision++;
                    evade++;
                    desc += " Seems aware of its surroundings.";
                    break;
                case 4:
                    // CAPABLE
                    hp += 2;
                    defense++;
                    evade++;
                    damage++;
                    desc += " Seems confident.";
                    break;
            }
        }
    }

    private void setSprites() {
        // TODO: JSON numbers all == 0?
        System.out.println(sprite_location);
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
