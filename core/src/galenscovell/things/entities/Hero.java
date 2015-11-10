package galenscovell.things.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import galenscovell.util.ResourceManager;

public class Hero extends Creature {

    public Hero() {
        setSprites();
        title = "Hero";
        description = "Hero";
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