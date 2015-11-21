package galenscovell.processing.actions.Skills;

import galenscovell.processing.*;
import galenscovell.processing.actions.*;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Leap implements Action {
    private final Repository repo;
    private List<Tile> range;

    public Leap(Repository repo) {
        this.repo = repo;
    }

    @Override
    public boolean initialized(Entity entity, Tile target) {
        setRange(entity);
        enableRangeDisplay();
        return true;
    }

    @Override
    public boolean act(Entity entity, Tile target) {
        return leap(entity, target);
    }

    private void setRange(Entity entity) {
        List<Tile> pattern = new ArrayList<Tile>();
        int centerX = entity.getX() / Constants.TILESIZE;
        int centerY = entity.getY() / Constants.TILESIZE;
        Tile center = repo.findTile(centerX, centerY);

        // pattern: donut radius 3
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
                    pattern.add(tile);
                }
            }
        }
        this.range = repo.rayCaster.instantiate(entity, pattern, 5);
    }

    private void enableRangeDisplay() {
        for (Tile tile : range) {
            tile.enableHighlight();
        }
    }

    private void disableRangeDisplay() {
        for (Tile tile : range) {
            tile.disableHighlight();
        }
    }

    private boolean leap(Entity entity, Tile target) {
        if (target == null || !range.contains(target) || target.isOccupied()) {
            return false;
        }
        disableRangeDisplay();
        return finalizeLeap(entity, target.x, target.y);
    }

    private boolean finalizeLeap(Entity entity, int newX, int newY) {
        Move skillMovement = new Move(repo);
        Tile skillTarget = repo.findTile(newX, newY);
        if (skillMovement.initialized(entity, skillTarget)) {
            Point finalPoint = null;
            while (!entity.pathStackEmpty()) {
                finalPoint = entity.nextPathPoint();
            }
            entity.pushToPathStack(finalPoint);
            return skillMovement.act(entity, skillTarget);
        } else {
            return false;
        }
    }

    @Override
    public void resolve(Entity entity) {

    }
}

