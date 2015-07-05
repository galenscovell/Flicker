package galenscovell.entities;

import galenscovell.util.ResourceManager;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;

/**
 * MONSTER CREATURE
 * Utilizes MonsterParser to create Creatures based on JSON data.
 *
 * @author Galen Scovell
 */

public class Monster extends Creature {
    private String type, desc, spriteLocation;
    public int level;
    private int vit, intel, vision, speed, evade, defense, attacks, damage, poison;

    public Monster() {
        super();
    }

    public void setup() {
        setSprites();
        setFlags();
        setStats();
    }

    private void setStats() {
        title = type;
        description = desc;
        stats.put("level", level);
        stats.put("vit", vit);
        stats.put("int", intel);
        stats.put("vision", vision);
        stats.put("speed", speed);
        stats.put("evade", evade);
        stats.put("defense", defense);
        stats.put("attacks", attacks);
        stats.put("damage", damage);
        stats.put("poison", poison);
    }

    private void setFlags() {
        Random random = new Random();
        int flagChance = random.nextInt(100);
        if (flagChance > 90) {
            int flag = random.nextInt(5);
            if (flag == 0) {
                // TOUGH
                vit += 4;
                defense += 4;
                desc += " Looks like it can take a beating.";
            } else if (flag == 1) {
                // AGILE
                evade += 2;
                attacks++;
                damage--;
                desc += " Appears agile.";
            } else if (flag == 2) {
                // BRUISER
                damage += 4;
                desc += " Wouldn't want to be hit by this one.";
            } else if (flag == 3) {
                // WOUNDED
                vit -= 2;
                vision++;
                speed -= 1.0f;
                desc += " Has some cuts and bruises.";
            } else if (flag == 4) {
                // CAPABLE
                vit += 2;
                evade += 2;
                defense += 2;
                damage++;
                desc += " Seems confident.";
            }
        }
    }

    private void setSprites() {
        rightSprites = new Sprite[2];
        leftSprites = new Sprite[2];

        // Populate sprite animation sets
        for (int i = 0; i < 2; i++) {
            rightSprites[i] = new Sprite(ResourceManager.organicAtlas.findRegion(spriteLocation + i));
            rightSprites[i].flip(false, true);
            leftSprites[i] = new Sprite(ResourceManager.organicAtlas.findRegion(spriteLocation + i));
            leftSprites[i].flip(true, true);
        }
        currentSet = leftSprites;
    }
}
