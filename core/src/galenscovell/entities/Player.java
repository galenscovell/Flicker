package galenscovell.entities;

import galenscovell.graphics.SpriteSheet;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * PLAYER CREATURE
 * Only one Player instance exists for persistence across levels.
 *
 * @author Galen Scovell
 */

public class Player extends Creature {
    private int atkX, atkY;
    private String type, desc;
    private int level, spriteLocation, vit, intel, vision, speed, evade, defense, attacks, damage, poison;

    public Player() {
        super();
    }

    public void setup() {
        setSprites();
        setStats();
    }

    private void setStats() {
        title = type;
        description = desc;
        stats.put("level", level);
        stats.put("vit", vit);
        stats.put("int", intel);
        stats.put("vision", vision);
        stats.put("speed", speed);
        stats.put("evade", evade);
        stats.put("defense", defense);
        stats.put("attacks", attacks);
        stats.put("damage", damage);
        stats.put("poison", poison);
    }

    public String getClassType() {
        return type;
    }

    private void setSprites() {
        SpriteSheet sheet = SpriteSheet.charsheet;
        leftSprites = new Sprite[2];
        rightSprites = new Sprite[2];

        // Populate sprite animation sets
        for (int i = 0; i < 2; i++) {
            leftSprites[i] = new Sprite(sheet.getSprite(i + spriteLocation));
            rightSprites[i] = new Sprite(sheet.getSprite(i + (spriteLocation + 2)));
        }
        currentSet = leftSprites;
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