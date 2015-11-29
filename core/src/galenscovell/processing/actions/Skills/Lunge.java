package galenscovell.processing.actions.Skills;

import galenscovell.processing.*;
import galenscovell.processing.actions.*;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Lunge implements Action {
    private final Repository repo;
    private final Entity user;
    private final List<Tile> range;
    private Tile targettedTile;
    private Entity targettedEntity;

    public Lunge(Entity user, Repository repo) {
        this.user = user;
        this.repo = repo;
        this.range = new ArrayList<Tile>();
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Lunge", "Hit target two spaces away in a cardinal direction."};
    }

    @Override
    public boolean setTarget(Tile tile) {
        if (tile == null || !range.contains(tile) || !tile.isHighlightedOrange()) {
            return false;
        } else {
            Entity targetEntity = repo.findEntity(tile.x, tile.y);
            if (targetEntity == null) {
                return false;
            } else {
                this.targettedTile = tile;
                this.targettedEntity = targetEntity;
                return true;
            }
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

    private void setRange() {
        int centerX = user.getX() / Constants.TILESIZE;
        int centerY = user.getY() / Constants.TILESIZE;

        // pattern: two tiles cardinal
        for (int i = 0; i <= 3; i++) {
            for (int delta = 1; delta <= 2; delta++) {
                Tile tile;
                if (i == 0) {
                    tile = repo.findTile(centerX + delta, centerY);  // pos-horizontal
                } else if (i == 1) {
                    tile = repo.findTile(centerX - delta, centerY);  // neg-horizontal
                } else if (i == 2) {
                    tile = repo.findTile(centerX, centerY + delta);  // pos-vertical
                } else {
                    tile = repo.findTile(centerX, centerY - delta);  // neg-vertical
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

    @Override
    public boolean act() {
        disableRangeDisplay();
        return lunge();
    }

    private void disableRangeDisplay() {
        for (Tile tile : range) {
            tile.disableHighlight();
        }
    }

    private boolean lunge() {
        int entityX = user.getX() / Constants.TILESIZE;
        int entityY = user.getY() / Constants.TILESIZE;
        int targetEntityX = targettedEntity.getX() / Constants.TILESIZE;
        int targetEntityY = targettedEntity.getY() / Constants.TILESIZE;
        int newX = entityX + ((targetEntityX - entityX) / 2);
        int newY = entityY + ((targetEntityY - entityY) / 2);

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
        if (targettedEntity != null) {
            targettedEntity.setBeingAttacked();
            targettedEntity.takePhysicalDamage(user.doPhysicalDamage());
            if (targettedEntity.isDead()) {
                repo.placeRemains(targettedEntity);
            }
        }
    }

    @Override
    public void exit() {
        disableRangeDisplay();
    }
}
