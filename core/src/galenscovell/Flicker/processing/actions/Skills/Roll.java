package galenscovell.flicker.processing.actions.Skills;

import galenscovell.flicker.processing.*;
import galenscovell.flicker.processing.actions.*;
import galenscovell.flicker.things.entities.Entity;
import galenscovell.flicker.util.Constants;
import galenscovell.flicker.world.Tile;

import java.util.*;

public class Roll implements Action {
    private final Repository repo;
    private final Entity user;
    private final List<Tile> range;
    private Tile targettedTile;
    private Entity targettedEntity;

    public Roll(Entity user, Repository repo) {
        this.user = user;
        this.repo = repo;
        this.range = new ArrayList<Tile>();
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Roll", "Roll two spaces diagonally."};
    }

    @Override
    public boolean setTarget(Tile tile) {
        if (tile == null || !range.contains(tile) || tile.isOccupied()|| !tile.isHighlightedOrange()) {
            return false;
        } else {
            this.targettedTile = tile;
            return true;
        }
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
        return true;
    }

    @Override
    public boolean act() {
        disableRangeDisplay();
        return roll();
    }

    private void setRange() {
        int centerX = user.getX() / Constants.TILESIZE;
        int centerY = user.getY() / Constants.TILESIZE;

        // pattern: two tiles diagonal
        for (int i = 0; i <= 3; i++) {
            for (int delta = 1; delta <= 2; delta++) {
                Tile tile;
                if (i == 0) {
                    tile = repo.findTile(centerX + delta, centerY + delta);  // up-right
                } else if (i == 1) {
                    tile = repo.findTile(centerX + delta, centerY - delta);  // down-right
                } else if (i == 2) {
                    tile = repo.findTile(centerX - delta, centerY - delta);  // down-left
                } else {
                    tile = repo.findTile(centerX - delta, centerY + delta);  // up-left
                }

                if (tile != null && tile.isFloor() && !tile.isBlocking()) {
                    if (delta == 1) {
                        if (tile.isOccupied()) {
                            break;
                        } else {
                            tile.highlightBlue();
                            range.add(tile);
                        }
                    } else {
                        tile.highlightOrange();
                        range.add(tile);
                    }
                } else {
                    break;
                }
            }
        }
    }

    private void disableRangeDisplay() {
        for (Tile tile : range) {
            tile.disableHighlight();
        }
    }

    private boolean roll() {
        Move skillMovement = new Move(user, repo);
        Tile skillTarget = repo.findTile(targettedTile.x, targettedTile.y);
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

