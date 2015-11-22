package galenscovell.things.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import galenscovell.util.ResourceManager;

public class Hero extends Creature {

    public Hero() {
        setSprites();
        setStats();
    }

    private void setStats() {
        title = "Hero";
        description = "Hero";

        stats.put(Stats.STR, 1);
        stats.put(Stats.CON, 1);
        stats.put(Stats.AGI, 1);
        stats.put(Stats.INT, 1);
        stats.put(Stats.WIS, 1);
        stats.put(Stats.LCK, 1);

        stats.put(Stats.LEVEL, 1);
        stats.put(Stats.EXP, 0);

        stats.put(Stats.HP, 4);
        stats.put(Stats.MP, 4);
    }

    private void setSprites() {
        rightSprites = new Sprite[2];
        leftSprites = new Sprite[2];
        for (int i = 0; i < 2; i++) {
            rightSprites[i] = new Sprite(ResourceManager.organicAtlas.createSprite("player" + i));
            rightSprites[i].flip(false, true);
            leftSprites[i] = new Sprite(ResourceManager.organicAtlas.createSprite("player" + i));
            leftSprites[i].flip(true, true);
        }
        currentSet = rightSprites;
    }
}