package galenscovell.processing.actions;

import galenscovell.processing.*;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Skill implements Action {
    private Repository repo;
    private Pathfinder pathfinder;
    private List<Tile> range;
    private int type;

    public Skill(Repository repo) {
        this.repo = repo;
        this.pathfinder = new Pathfinder();
    }

    public void define(int type) {
        this.type = type;
    }

    public boolean initialized(Entity entity, Tile target) {
        setRange(entity);
        toggleRangeDisplay();
        return true;
    }

    public boolean act(Entity entity, Tile target) {
        if (type == Constants.LUNGE_TYPE) {
            return lunge(entity, target);
        } else if (type == Constants.ROLL_TYPE) {
            return roll(entity, target);
        } else if (type == Constants.BASH_TYPE) {
            return bash(entity, target);
        } else if (type == Constants.LEAP_TYPE) {
            return leap(entity, target);
        }
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
        List<Tile> pattern = new ArrayList<Tile>();
        int centerX = entity.getX() / Constants.TILESIZE;
        int centerY = entity.getY() / Constants.TILESIZE;
        Tile center = repo.findTile(centerX, centerY);

        if (type == Constants.LUNGE_TYPE) {
            // pattern: 2 tiles cardinal
            for (int dx = -2; dx <= 2; dx++) {
                Tile tile = repo.findTile(centerX + dx, centerY);
                if (tile != null && tile != center && tile.isFloor()) {
                    pattern.add(tile);
                }
            }
            for (int dy = -2; dy <= 2; dy++) {
                Tile tile = repo.findTile(centerX, centerY + dy);
                if (tile != null && tile != center && tile.isFloor()) {
                    pattern.add(tile);
                }
            }
        } else if (type == Constants.ROLL_TYPE) {
            // pattern: 2 tiles diagonal
            for (int dx = -2; dx <= 2; dx++) {
                for (int dy = -2; dy <= 2; dy++) {
                    if (Math.abs(dx) != Math.abs(dy)) {
                        continue;
                    }
                    Tile tile = repo.findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        pattern.add(tile);
                    }
                }
            }
        } else if (type == Constants.BASH_TYPE) {
            // pattern: 1 tile all
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    Tile tile = repo.findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        pattern.add(tile);
                    }
                }
            }
        } else if (type == Constants.LEAP_TYPE) {
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
        }
        this.range = repo.rayCaster.instantiate(entity, pattern, 5);
    }

    public boolean lunge(Entity entity, Tile target) {
        if (target == null) {
            return false;
        }
        Entity targetEntity = repo.findEntity(target.x, target.y);
        if (!range.contains(target) || targetEntity == null) {
            return false;
        }
        int entityX = (entity.getX() / Constants.TILESIZE);
        int entityY = (entity.getY() / Constants.TILESIZE);
        int targetEntityX = (targetEntity.getX() / Constants.TILESIZE);
        int targetEntityY = (targetEntity.getY() / Constants.TILESIZE);
        int newX = entityX + ((targetEntityX - entityX) / 2);
        int newY = entityY + ((targetEntityY - entityY) / 2);
        return finalizeLunge(entity, newX, newY);
    }

    private boolean finalizeLunge(Entity entity, int newX, int newY) {
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

    public boolean roll(Entity entity, Tile target) {
        if (target == null || !range.contains(target) || target.isOccupied()) {
            return false;
        }
        return finalizeRoll(entity, target.x, target.y);
    }

    private boolean finalizeRoll(Entity entity, int newX, int newY) {
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

    public boolean bash(Entity entity, Tile target) {
        if (target == null) {
            return false;
        }
        Entity targetEntity = repo.findEntity(target.x, target.y);
        if (!range.contains(target) || targetEntity == null) {
            return false;
        }
        int entityX = (entity.getX() / Constants.TILESIZE);
        int entityY = (entity.getY() / Constants.TILESIZE);
        int targetEntityX = (targetEntity.getX() / Constants.TILESIZE);
        int targetEntityY = (targetEntity.getY() / Constants.TILESIZE);
        int newX = entityX + ((targetEntityX - entityX) / 2);
        int newY = entityY + ((targetEntityY - entityY) / 2);
        return finalizeBash(entity, newX, newY);
    }

    private boolean finalizeBash(Entity entity, int newX, int newY) {
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

    public boolean leap(Entity entity, Tile target) {
        if (target == null || !range.contains(target) || target.isOccupied()) {
            return false;
        }
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
}
