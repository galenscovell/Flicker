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

        stats.put("STR", 1);
        stats.put("CON", 1);
        stats.put("AGI", 1);
        stats.put("INT", 1);
        stats.put("WIS", 1);
        stats.put("LCK", 1);
        stats.put("vision", 4);

        stats.put("level", 1);
        stats.put("HP", 4);
        stats.put("EXP", 0);
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