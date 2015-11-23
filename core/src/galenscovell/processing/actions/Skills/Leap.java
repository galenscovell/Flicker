package galenscovell.processing.actions.Skills;

import galenscovell.processing.*;
import galenscovell.processing.actions.*;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Leap implements Action {
    private final Repository repo;
    private final Entity user;
    private List<Tile> range;
    private Tile targettedTile;
    private Entity targettedEntity;

    public Leap(Entity user, Repository repo) {
        this.user = user;
        this.repo = repo;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Leap", "Leap up to three spaces away."};
    }

    @Override
    public void setTarget(Tile tile) {
        this.targettedTile = tile;
    }

    @Override
    public Entity getUser() {
        return user;
    }

    @Override
    public Tile getTarget() {
        return targettedTile;
    }

    @Override
    public boolean initialize() {
        setRange();
        enableRangeDisplay();
        return true;
    }

    @Override
    public boolean act() {
        disableRangeDisplay();
        return leap();
    }

    private void setRange() {
        List<Tile> pattern = new ArrayList<Tile>();
        int centerX = user.getX() / Constants.TILESIZE;
        int centerY = user.getY() / Constants.TILESIZE;
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
        this.range = repo.getRayCaster().instantiate(user, pattern, 5);
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

    private boolean leap() {
        if (targettedTile == null || !range.contains(targettedTile) || targettedTile.isOccupied()) {
            return false;
        }
        return finalizeLeap(targettedTile.x, targettedTile.y);
    }

    private boolean finalizeLeap(int newX, int newY) {
        Move skillMovement = new Move(user, repo);
        Tile skillTarget = repo.findTile(newX, newY);
        skillMovement.setTarget(skillTarget);
        if (skillMovement.initialize()) {
            Point finalPoint = null;
            while (!user.pathStackEmpty()) {
                finalPoint = user.nextPathPoint();
            }
            user.pushToPathStack(finalPoint);
            return skillMovement.act();
        } else {
            return false;
        }
    }

    @Override
    public void resolve() {

    }

    @Override
    public void exit() {
        disableRangeDisplay();
    }
}

