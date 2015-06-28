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
    }

    private void setSprites() {
        leftSprites = new Sprite[6];
        rightSprites = new Sprite[6];

        // Populate sprite sets
        for (int i = 0; i < 6; i++) {
            leftSprites[i] = new Sprite(ResourceManager.playerAtlas.findRegion("robo" + i + "_l"));
            leftSprites[i].flip(false, true);
            rightSprites[i] = new Sprite(ResourceManager.playerAtlas.findRegion("robo" + i + "_r"));
            rightSprites[i].flip(false, true);
        }
        currentSet = rightSprites;
    }

    @Override
    public void move(int dx, int dy, boolean possible) {
        turn(dx, dy);
        if (possible) {
            moving = true;
            x += dx;
            y += dy;
        }
    }
}