package galenscovell.processing.actions;

import galenscovell.processing.Updater;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.ArrayList;
import java.util.Map;

public class AttackAction {
    private Updater updater;
    private Map<Integer, Tile> tiles;
    private ArrayList<Tile> range;
    private String currentMove;
    private int tileSize;

    public AttackAction(Updater updater, Map<Integer, Tile> tiles) {
        this.updater = updater;
        this.tiles = tiles;
        this.tileSize = Constants.TILESIZE;
        this.currentMove = "";
    }

    public void setRange(Entity entity, String move) {
        range = new ArrayList<Tile>();
        currentMove = move;
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
        for (Tile tile : range) {
            tile.toggleHighlighted();
        }
    }

    public void removeRange() {
        for (Tile tile : range) {
            tile.toggleHighlighted();
        }
        range.clear();
        currentMove = null;
    }

    public Tile getTileInRange(int x, int y) {
        return findTileInRange(x, y);
    }

    public void finalizeMove(Entity entity, Entity targetEntity, Tile targetTile) {
        if (currentMove.equals("lunge")) {
            lunge(entity, targetEntity, targetTile);
        } else if (currentMove.equals("roll")) {
            roll(entity, targetEntity, targetTile);
        } else if (currentMove.equals("bash")) {
            bash(entity, targetEntity, targetTile);
        } else if (currentMove.equals("leap")) {
            leap(entity, targetEntity, targetTile);
        }
        removeRange();
    }

    public void lunge(Entity entity, Entity targetEntity, Tile targetTile) {
        if (!range.contains(targetTile) || targetEntity == null) {
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
        if (!range.contains(targetTile) || targetEntity != null) {
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
