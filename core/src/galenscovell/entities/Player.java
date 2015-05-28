
/**
 * PLAYER CLASS
 * Only one Player instance exists for persistence across levels.
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.graphics.SpriteSheet;
import galenscovell.logic.Point;


public class Player extends Creature {
    private int atkX, atkY;
    private String type;


    public Player(String type) {
        super();
        setSprites(type);
        setStats();
    }

    @Override
    public String toString() {
        return "Player";
    }

    private void setStats() {
        stats.put("vision", 5);
    }

    private void setSprites(String type) {
        int spriteLocation = 0;
        if (type.equals("explorer")) {
            spriteLocation = 0;
        } else if (type.equals("knight")) {
            spriteLocation = 4;
        } else if (type.equals("archer")) {
            spriteLocation = 8;
        } else if (type.equals("mage")) {
            spriteLocation = 12;
        } else {
            System.err.println("Player type not found.");
        }
        SpriteSheet sheet = SpriteSheet.charsheet;
        leftSprites = new Sprite[2];
        rightSprites = new Sprite[2];

        // Populate sprite animation sets
        for (int i = 0; i < 2; i++) {
            leftSprites[i] = new Sprite(sheet.getSprite(i + spriteLocation));
            rightSprites[i] = new Sprite(sheet.getSprite(i + (spriteLocation + 2)));
        }

        currentSet = leftSprites;
        sprite = currentSet[0];
    }

    public Point getFacingPoint(int tileSize) {
        int tileX = x / tileSize;
        int tileY = y / tileSize;
        if (currentSet == leftSprites) {
            return new Point(tileX - 1, tileY);
        } else {
            return new Point(tileX + 1, tileY);
        }
    }

    @Override
    public void move(int dx, int dy, boolean possible) {
        turn(dx, dy);
        if (possible) {
            x += dx;
            y += dy;
        }
    }

    public void setAttackingCoords(int atkX, int atkY) {
        this.atkX = atkX;
        this.atkY = atkY;
    }

    @Override
    protected void attack(double interpolation, Entity entity) {
        sprite = currentSet[2];
        int diffX = x - atkX;
        int diffY = y - atkY;
        currentX = (int) (prevX - (diffX * interpolation));
        currentY = (int) (prevY - (diffY * interpolation));

        // Attack animation only covers small portion of target's tile
        if (interpolation > 0.3) {
            attacking = false;
        }
    }

    @Override
    public void interpolate(double interpolation) {
        currentX = (int) (prevX + ((x - prevX) * interpolation));
        currentY = (int) (prevY + ((y - prevY) * interpolation));

        if (currentX == x && currentY == y) {
            prevX = x;
            prevY = y;
        }

        if (interpolation >= 0.9) {
            beingAttacked = false;
        }
    }
}