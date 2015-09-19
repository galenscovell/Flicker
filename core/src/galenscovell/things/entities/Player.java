package galenscovell.things.entities;

import galenscovell.util.ResourceManager;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * PLAYER CREATURE
 * Only one Player instance exists for persistence across levels.
 *
 * @author Galen Scovell
 */

public class Player extends Creature {

    public Player() {
        super();
        setSprites();
        title = "Player";
        description = "Player";
        // Temporary for testing
        stats.put("damage", 5);
    }

    private void setSprites() {
        rightSprites = new Sprite[2];
        leftSprites = new Sprite[2];

        // Populate sprite sets
        for (int i = 0; i < 2; i++) {
            rightSprites[i] = new Sprite(ResourceManager.organicAtlas.createSprite("player" + i));
            rightSprites[i].flip(false, true);
            leftSprites[i] = new Sprite(ResourceManager.organicAtlas.createSprite("player" + i));
            leftSprites[i].flip(true, true);
        }
        currentSet = rightSprites;
    }
}