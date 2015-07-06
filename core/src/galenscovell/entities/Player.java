package galenscovell.entities;

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
        description = "A lonely probe.";
        // Temporary for testing
        stats.put("damage", 5);
    }

    private void setSprites() {
        rightSprites = new Sprite[6];
        leftSprites = new Sprite[6];

        // Populate sprite sets
        for (int i = 0; i < 6; i++) {
            rightSprites[i] = new Sprite(ResourceManager.syntheticAtlas.createSprite("robo" + i));
            rightSprites[i].flip(false, true);
            leftSprites[i] = new Sprite(ResourceManager.syntheticAtlas.createSprite("robo" + i));
            leftSprites[i].flip(true, true);
        }
        currentSet = rightSprites;
    }
}