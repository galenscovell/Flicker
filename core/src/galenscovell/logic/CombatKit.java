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
    private String lastMove;

    public CombatKit(Map<Integer, Tile> tiles) {
        this.tiles = tiles;
    }

    public void setRange(Entity entity, String move) {
        if (lastMove != null && lastMove.equals(move)) {
            removeRange();
            return;
        }
        if (range != null && range.size() > 0) {
            removeRange();
        }
        range = new ArrayList<Tile>();
        lastMove = move;
        float centerX = entity.getX() / Constants.TILESIZE;
        float centerY = entity.getY() / Constants.TILESIZE;
        Tile center = findTile(centerX, centerY);

        if (move.equals("lunge")) {
            // Range: 2 tiles cardinal
            for (int dx = -2; dx <= 2; dx++) {
                Tile tile = findTile(centerX + dx, centerY);
                if (tile != null && tile != center && tile.isFloor()) {
                    range.add(tile);
                }
            }
            for (int dy = -2; dy <= 2; dy++) {
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
        toggleDisplay();
    }

    public void removeRange() {
        toggleDisplay();
        range.clear();
        lastMove = null;
    }

    public void toggleDisplay() {
        for (Tile tile : range) {
            tile.toggleHighlighted();
        }
    }

    public String getLastMove() {
        return lastMove;
    }

    public Tile getTileInRange(float x, float y) {
        return findTileInRange(x, y);
    }

    public void finalizeMove(Entity entity, Tile target) {
        if (lastMove.equals("lunge")) {
            lunge(entity, target);
        } else if (lastMove.equals("roll")) {
            roll(entity, target);
        } else if (lastMove.equals("bash")) {
            bash(entity, target);
        } else if (lastMove.equals("leap")) {
            leap(entity, target);
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
        return tiles.get((int) x * Constants.MAPSIZE + (int) y);
    }

    private Tile findTileInRange(float x, float y) {
        Tile tile = null;
        for (Tile t : range) {
            if (t.x == x && t.y == y) {
                tile = t;
            }
        }
        return tile;
    }
}
