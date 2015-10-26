package galenscovell.processing.actions;

import galenscovell.processing.*;
import galenscovell.things.entities.*;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Skill implements Action {
    private Repository repo;
    private Pathfinder pathfinder;
    private List<Tile> range;
    private String definition;

    public Skill(Repository repo) {
        this.repo = repo;
        this.pathfinder = new Pathfinder();
        this.range = new ArrayList<Tile>();
    }

    public void define(String definition) {
        this.definition = definition;
    }

    public boolean initialized(Entity entity, Tile target) {
        setRange(entity);
        toggleRangeDisplay();
        return true;
    }

    public boolean act(Entity entity, Tile target) {
        return false;
    }

    public void resolve(Entity entity) {
        toggleRangeDisplay();
    }

    private void toggleRangeDisplay() {
        for (Tile tile : range) {
            tile.toggleHighlighted();
        }
    }

    private void setRange(Entity entity) {
        int centerX = entity.getX() / Constants.TILESIZE;
        int centerY = entity.getY() / Constants.TILESIZE;
        Tile center = repo.findTile(centerX, centerY);

        if (definition.equals("lunge")) {
            // Range: 2 tiles cardinal
            for (int dx = -2; dx <= 2; dx++) {
                Tile tile = repo.findTile(centerX + dx, centerY);
                if (tile != null && tile != center && tile.isFloor()) {
                    range.add(tile);
                }
            }
            for (int dy = -2; dy <= 2; dy++) {
                Tile tile = repo.findTile(centerX, centerY + dy);
                if (tile != null && tile != center && tile.isFloor()) {
                    range.add(tile);
                }
            }
        } else if (definition.equals("roll")) {
            // Range: 2 tiles diagonal
            for (int dx = -2; dx <= 2; dx++) {
                for (int dy = -2; dy <= 2; dy++) {
                    if (Math.abs(dx) != Math.abs(dy)) {
                        continue;
                    }
                    Tile tile = repo.findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        range.add(tile);
                    }
                }
            }
        } else if (definition.equals("bash")) {
            // Range: 1 tile all
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    Tile tile = repo.findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        range.add(tile);
                    }
                }
            }
        } else if (definition.equals("leap")) {
            // Range: Donut radius 3
            for (int dx = -3; dx <= 3; dx++) {
                for (int dy = -3; dy <= 3; dy++) {
                    if (Math.abs(dx) == 3 && Math.abs(dy) == 3) {
                        continue;
                    }
                    if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
                        continue;
                    }
                    Tile tile = repo.findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        range.add(tile);
                    }
                }
            }
        }
    }

    public void finalizeMove(Entity entity, Entity targetEntity, Tile targetTile) {
//        if (currentMove.equals("lunge")) {
//            lunge(entity, targetEntity, targetTile);
//        } else if (currentMove.equals("roll")) {
//            roll(entity, targetEntity, targetTile);
//        } else if (currentMove.equals("bash")) {
//            bash(entity, targetEntity, targetTile);
//        } else if (currentMove.equals("leap")) {
//            leap(entity, targetEntity, targetTile);
//        }
//        removeRangeDisplay();
    }

    public void lunge(Entity entity, Entity targetEntity, Tile targetTile) {
//        if (!range.contains(targetTile) || targetEntity == null) {
//            return;
//        }
//        int entityX = (entity.getX() / Constants.TILESIZE);
//        int entityY = (entity.getY() / Constants.TILESIZE);
//        int targetEntityX = (targetEntity.getX() / Constants.TILESIZE);
//        int targetEntityY = (targetEntity.getY() / Constants.TILESIZE);
//        int newX = entityX + ((targetEntityX - entityX) / 2);
//        int newY = entityY + ((targetEntityY - entityY) / 2);
//        mover.move(hero, newX, newY);
    }

    public void roll(Entity entity, Entity targetEntity, Tile targetTile) {

    }

    public void bash(Entity entity, Entity targetEntity, Tile targetTile) {

    }

    public void leap(Entity entity, Entity targetEntity, Tile targetTile) {

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
