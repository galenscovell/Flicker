package galenscovell.things.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import galenscovell.util.ResourceManager;

import java.util.Random;

public class Monster extends Creature {
    private String type, desc, spriteLocation;
    public int level;
    private int vit, intel, vision, speed, evade, defense, attacks, damage, poison;

    public Monster() {
    }

    public void setup() {
        this.setSprites();
        this.setFlags();
        this.setStats();
    }

    private void setStats() {
        this.title = this.type;
        this.description = this.desc;
        this.stats.put("level", this.level);
        this.stats.put("vit", this.vit);
        this.stats.put("int", this.intel);
        this.stats.put("vision", this.vision);
        this.stats.put("speed", this.speed);
        this.stats.put("evade", this.evade);
        this.stats.put("defense", this.defense);
        this.stats.put("attacks", this.attacks);
        this.stats.put("damage", this.damage);
        this.stats.put("poison", this.poison);
    }

    private void setFlags() {
        Random random = new Random();
        int flagChance = random.nextInt(100);
        if (flagChance > 90) {
            int flag = random.nextInt(5);
            if (flag == 0) {
                // TOUGH
                this.vit += 4;
                this.defense += 4;
                this.desc += " Looks like it can take a beating.";
            } else if (flag == 1) {
                // AGILE
                this.evade += 2;
                this.attacks++;
                this.damage--;
                this.desc += " Appears agile.";
            } else if (flag == 2) {
                // BRUISER
                this.damage += 4;
                this.desc += " Wouldn't want to be hit by this one.";
            } else if (flag == 3) {
                // WOUNDED
                this.vit -= 2;
                this.vision++;
                this.speed -= 1.0f;
                this.desc += " Has some cuts and bruises.";
            } else if (flag == 4) {
                // CAPABLE
                this.vit += 2;
                this.evade += 2;
                this.defense += 2;
                this.damage++;
                this.desc += " Seems confident.";
            }
        }
    }

    private void setSprites() {
        this.rightSprites = new Sprite[2];
        this.leftSprites = new Sprite[2];
        for (int i = 0; i < 2; i++) {
            this.rightSprites[i] = new Sprite(ResourceManager.organicAtlas.createSprite(this.spriteLocation + i));
            this.rightSprites[i].flip(false, true);
            this.leftSprites[i] = new Sprite(ResourceManager.organicAtlas.createSprite(this.spriteLocation + i));
            this.leftSprites[i].flip(true, true);
        }
        this.currentSet = this.leftSprites;
    }
}
