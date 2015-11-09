package galenscovell.processing.actions;

import galenscovell.processing.*;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Skill implements Action {
    private final Repository repo;
    private final Pathfinder pathfinder;
    private List<Tile> range;
    private int type;

    public Skill(Repository repo) {
        this.repo = repo;
        this.pathfinder = new Pathfinder();
    }

    @Override
    public void define(int type) {
        this.type = type;
    }

    @Override
    public boolean initialized(Entity entity, Tile target) {
        this.setRange(entity);
        this.toggleRangeDisplay();
        return true;
    }

    @Override
    public boolean act(Entity entity, Tile target) {
        if (this.type == Constants.LUNGE_TYPE) {
            return this.lunge(entity, target);
        } else if (this.type == Constants.ROLL_TYPE) {
            return this.roll(entity, target);
        } else if (this.type == Constants.BASH_TYPE) {
            return this.bash(entity, target);
        } else if (this.type == Constants.LEAP_TYPE) {
            return this.leap(entity, target);
        }
        return false;
    }

    @Override
    public void resolve(Entity entity) {
        this.toggleRangeDisplay();
    }

    private void toggleRangeDisplay() {
        for (Tile tile : this.range) {
            tile.toggleHighlighted();
        }
    }

    private void setRange(Entity entity) {
        List<Tile> pattern = new ArrayList<Tile>();
        int centerX = entity.getX() / Constants.TILESIZE;
        int centerY = entity.getY() / Constants.TILESIZE;
        Tile center = this.repo.findTile(centerX, centerY);

        if (this.type == Constants.LUNGE_TYPE) {
            // pattern: 2 tiles cardinal
            for (int dx = -2; dx <= 2; dx++) {
                Tile tile = this.repo.findTile(centerX + dx, centerY);
                if (tile != null && tile != center && tile.isFloor()) {
                    pattern.add(tile);
                }
            }
            for (int dy = -2; dy <= 2; dy++) {
                Tile tile = this.repo.findTile(centerX, centerY + dy);
                if (tile != null && tile != center && tile.isFloor()) {
                    pattern.add(tile);
                }
            }
        } else if (this.type == Constants.ROLL_TYPE) {
            // pattern: 2 tiles diagonal
            for (int dx = -2; dx <= 2; dx++) {
                for (int dy = -2; dy <= 2; dy++) {
                    if (Math.abs(dx) != Math.abs(dy)) {
                        continue;
                    }
                    Tile tile = this.repo.findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        pattern.add(tile);
                    }
                }
            }
        } else if (this.type == Constants.BASH_TYPE) {
            // pattern: 1 tile all
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    Tile tile = this.repo.findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        pattern.add(tile);
                    }
                }
            }
        } else if (this.type == Constants.LEAP_TYPE) {
            // pattern: donut radius 3
            for (int dx = -3; dx <= 3; dx++) {
                for (int dy = -3; dy <= 3; dy++) {
                    if (Math.abs(dx) == 3 && Math.abs(dy) == 3) {
                        continue;
                    }
                    if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
                        continue;
                    }
                    Tile tile = this.repo.findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        pattern.add(tile);
                    }
                }
            }
        }
        this.range = this.repo.rayCaster.instantiate(entity, pattern, 5);
    }

    public boolean lunge(Entity entity, Tile target) {
        if (target == null) {
            return false;
        }
        Entity targetEntity = this.repo.findEntity(target.x, target.y);
        if (!this.range.contains(target) || targetEntity == null) {
            return false;
        }
        int entityX = entity.getX() / Constants.TILESIZE;
        int entityY = entity.getY() / Constants.TILESIZE;
        int targetEntityX = targetEntity.getX() / Constants.TILESIZE;
        int targetEntityY = targetEntity.getY() / Constants.TILESIZE;
        int newX = entityX + (targetEntityX - entityX) / 2;
        int newY = entityY + (targetEntityY - entityY) / 2;
        return this.finalizeLunge(entity, newX, newY);
    }

    private boolean finalizeLunge(Entity entity, int newX, int newY) {
        Move skillMovement = new Move(this.repo);
        Tile skillTarget = this.repo.findTile(newX, newY);
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
        if (target == null || !this.range.contains(target) || target.isOccupied()) {
            return false;
        }
        return this.finalizeRoll(entity, target.x, target.y);
    }

    private boolean finalizeRoll(Entity entity, int newX, int newY) {
        Move skillMovement = new Move(this.repo);
        Tile skillTarget = this.repo.findTile(newX, newY);
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
        Entity targetEntity = this.repo.findEntity(target.x, target.y);
        if (!this.range.contains(target) || targetEntity == null) {
            return false;
        }
        int entityX = entity.getX() / Constants.TILESIZE;
        int entityY = entity.getY() / Constants.TILESIZE;
        int targetEntityX = targetEntity.getX() / Constants.TILESIZE;
        int targetEntityY = targetEntity.getY() / Constants.TILESIZE;
        int newX = entityX + (targetEntityX - entityX) / 2;
        int newY = entityY + (targetEntityY - entityY) / 2;
        return this.finalizeBash(entity, newX, newY);
    }

    private boolean finalizeBash(Entity entity, int newX, int newY) {
        Move skillMovement = new Move(this.repo);
        Tile skillTarget = this.repo.findTile(newX, newY);
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
        if (target == null || !this.range.contains(target) || target.isOccupied()) {
            return false;
        }
        return this.finalizeLeap(entity, target.x, target.y);
    }

    private boolean finalizeLeap(Entity entity, int newX, int newY) {
        Move skillMovement = new Move(this.repo);
        Tile skillTarget = this.repo.findTile(newX, newY);
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
