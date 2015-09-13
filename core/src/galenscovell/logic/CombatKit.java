package galenscovell.logic;

import galenscovell.entities.Entity;
import galenscovell.util.Constants;

import java.util.ArrayList;
import java.util.Map;

/**
 * COMBAT KIT
 * Combat component handler
 *
 * @author Galen Scovell
 */

public class CombatKit {
    private Map<Integer, Tile> tiles;
    private ArrayList<Tile> range;

    public CombatKit(Map<Integer, Tile> tiles) {
        this.tiles = tiles;
    }

    public void setRange(Entity entity, String move) {
        this.range = new ArrayList<Tile>();
        float centerX = entity.getX() / Constants.TILESIZE;
        float centerY = entity.getY() / Constants.TILESIZE;
        Tile center = findTile(centerX, centerY);

        if (move.equals("lunge")) {
            // Range: 2 tiles cardinal
            for (int dx = -2; dx <= 2; dx += 2) {
                Tile tile = findTile(centerX + dx, centerY);
                if (tile != null && tile != center && tile.isFloor()) {
                    range.add(tile);
                }
            }
            for (int dy = -2; dy <= 2; dy += 2) {
                Tile tile = findTile(centerX, centerY + dy);
                if (tile != null && tile != center && tile.isFloor()) {
                    range.add(tile);
                }
            }
        } else if (move.equals("roll")) {
            // Range: 2 tiles diagonal
            for (int dx = -2; dx <= 2; dx++) {
                for (int dy = -2; dy <= 2; dy++) {
                    if (Math.abs(dx) != Math.abs(dy)) {
                        continue;
                    }
                    Tile tile = findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        range.add(tile);
                    }
                }
            }
        } else if (move.equals("bash")) {
            // Range: 1 tile all
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    Tile tile = findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        range.add(tile);
                    }
                }
            }
        } else if (move.equals("leap")) {
            // Range: Circle radius 3
            for (int dx = -3; dx <= 3; dx++) {
                for (int dy = -3; dy <= 3; dy++) {
                    if (Math.abs(dx) == 3 && Math.abs(dy) == 3) {
                        continue;
                    }
                    Tile tile = findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        range.add(tile);
                    }
                }
            }
        }
    }

    public void toggleDisplay() {
        for (Tile tile : range) {
            tile.toggleHighlighted();
        }
    }

    public void lunge(Entity entity, Tile target) {

    }

    public void roll(Entity entity, Tile target) {

    }

    public void bash(Entity entity, Tile target) {

    }

    public void leap(Entity entity, Tile target) {

    }

    private Tile findTile(float x, float y) {
        int tileX = (int) x;
        int tileY = (int) y;
        return tiles.get(tileX * Constants.MAPSIZE + tileY);
    }
}
