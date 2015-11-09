package galenscovell.things.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import galenscovell.util.ResourceManager;

public class Hero extends Creature {

    public Hero() {
        this.setSprites();
        this.title = "Hero";
        this.description = "Hero";
    }

    private void setSprites() {
        this.rightSprites = new Sprite[2];
        this.leftSprites = new Sprite[2];
        for (int i = 0; i < 2; i++) {
            this.rightSprites[i] = new Sprite(ResourceManager.organicAtlas.createSprite("player" + i));
            this.rightSprites[i].flip(false, true);
            this.leftSprites[i] = new Sprite(ResourceManager.organicAtlas.createSprite("player" + i));
            this.leftSprites[i].flip(true, true);
        }
        this.currentSet = this.rightSprites;
    }
}