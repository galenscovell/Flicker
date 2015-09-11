package galenscovell.logic;

import galenscovell.entities.Entity;
import galenscovell.util.Constants;

import java.util.Map;

/**
 * COMBAT KIT
 * Combat component handler
 *
 * @author Galen Scovell
 */

public class CombatKit {
    private Map<Integer, Tile> tiles;

    public CombatKit(Map<Integer, Tile> tiles) {
        this.tiles = tiles;
    }

    public void displayRange(Entity entity, String move) {
        int centerX = entity.getX() / Constants.TILESIZE;
        int centerY = entity.getY() / Constants.TILESIZE;
        Tile center = findTile(centerX, centerY);
        if (move.equals("lunge")) {
            // Range: 2 tiles cardinal
            for (int dx = -2; dx <= 2; dx++) {
                Tile tile = findTile(centerX + dx, centerY);
                if (tile != null && tile != center && tile.isFloor()) {
                    tile.toggleHighlighted();
                }
            }
            for (int dy = -2; dy <= 2; dy++) {
                Tile tile = findTile(centerX, centerY + dy);
                if (tile != null && tile != center && tile.isFloor()) {
                    tile.toggleHighlighted();
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
                        tile.toggleHighlighted();
                    }
                }
            }
        } else if (move.equals("bash")) {
            // Range: 1 tile all
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    Tile tile = findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        tile.toggleHighlighted();
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
                        tile.toggleHighlighted();
                    }
                }
            }
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

    private Tile findTile(int x, int y) {
        return tiles.get(x * Constants.MAPSIZE + y);
    }
}
