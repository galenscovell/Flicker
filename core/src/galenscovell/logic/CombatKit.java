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
    private Updater updater;
    private Map<Integer, Tile> tiles;
    private ArrayList<Tile> range;
    private String lastMove;
    private int tileSize;

    public CombatKit(Updater updater, Map<Integer, Tile> tiles) {
        this.updater = updater;
        this.tiles = tiles;
        this.tileSize = Constants.TILESIZE;
    }

    public void setRange(Entity entity, String move) {
        range = new ArrayList<Tile>();
        lastMove = move;
        int centerX = entity.getX() / Constants.TILESIZE;
        int centerY = entity.getY() / Constants.TILESIZE;
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

    public Tile getTileInRange(int x, int y) {
        return findTileInRange(x, y);
    }

    public void finalizeMove(Entity entity, Entity targetEntity, Tile targetTile) {
        if (lastMove.equals("lunge")) {
            lunge(entity, targetEntity, targetTile);
        } else if (lastMove.equals("roll")) {
            roll(entity, targetEntity, targetTile);
        } else if (lastMove.equals("bash")) {
            bash(entity, targetEntity, targetTile);
        } else if (lastMove.equals("leap")) {
            leap(entity, targetEntity, targetTile);
        }
    }

    public void lunge(Entity entity, Entity targetEntity, Tile targetTile) {
        if (targetEntity == null) {
            return;
        }
        int entityX = (entity.getX() / tileSize);
        int entityY = (entity.getY() / tileSize);
        int targetEntityX = (targetEntity.getX() / tileSize);
        int targetEntityY = (targetEntity.getY() / tileSize);
        int newX = entityX + ((targetEntityX - entityX) / 2);
        int newY = entityY + ((targetEntityY - entityY) / 2);
        updater.move(entity, newX, newY);
    }

    public void roll(Entity entity, Entity targetEntity, Tile targetTile) {
        if (targetEntity != null) {
            return;
        }
        updater.move(entity, targetTile.x, targetTile.y);
    }

    public void bash(Entity entity, Entity targetEntity, Tile targetTile) {

    }

    public void leap(Entity entity, Entity targetEntity, Tile targetTile) {

    }

    private Tile findTile(int x, int y) {
        return tiles.get(x * Constants.MAPSIZE + y);
    }

    private Tile findTileInRange(int x, int y) {
        Tile tile = null;
        for (Tile t : range) {
            if (t.x == x && t.y == y) {
                tile = t;
            }
        }
        return tile;
    }
}
