package galenscovell.things.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import galenscovell.util.ResourceManager;

import java.util.Random;

public class Monster extends Creature {
    private String type, desc, spriteLocation;
    public int tier, level;
    private int STR, CON, AGI, INT, WIS, LCK;
    private int HP, vision;

    public Monster() {

    }

    public void setup() {
        setSprites();
        setFlags();
        setStats();
    }

    private void setStats() {
        title = type;
        description = desc;

        stats.put(Stats.STR, STR);
        stats.put(Stats.CON, CON);
        stats.put(Stats.AGI, AGI);
        stats.put(Stats.INT, INT);
        stats.put(Stats.WIS, WIS);
        stats.put(Stats.LCK, LCK);

        stats.put(Stats.VISION, vision);

        stats.put(Stats.LEVEL, level);
        stats.put(Stats.EXP, 0);

        stats.put(Stats.HP, level * CON);
        stats.put(Stats.MP, level * INT);
    }

    private void setFlags() {
        Random random = new Random();
        int flagChance = random.nextInt(100);
        if (flagChance > 90) {
            int flag = random.nextInt(4);
            if (flag == 0) {
                // TOUGH
                CON++;
                desc += "Looks like it can take a beating.";
            } else if (flag == 1) {
                // AGILE
                AGI++;
                desc += "Appears agile.";
            } else if (flag == 2) {
                // BRUISER
                STR++;
                desc += "Wouldn't want to be hit by this one.";
            } else if (flag == 3) {
                // BRIGHT
                INT++;
                WIS++;
                vision++;
                desc += "Has a sharp look in its eyes.";
            }
        }
    }

    private void setSprites() {
        rightSprites = new Sprite[2];
        leftSprites = new Sprite[2];
        for (int i = 0; i < 2; i++) {
            rightSprites[i] = new Sprite(ResourceManager.organicAtlas.createSprite(spriteLocation + i));
            rightSprites[i].flip(false, true);
            leftSprites[i] = new Sprite(ResourceManager.organicAtlas.createSprite(spriteLocation + i));
            leftSprites[i].flip(true, true);
        }
        currentSet = leftSprites;
    }
}
