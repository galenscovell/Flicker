package galenscovell.processing.actions.Skills;

import galenscovell.processing.*;
import galenscovell.processing.actions.*;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Bash implements Action {
    private final Repository repo;
    private List<Tile> range;
    private Entity targettedEntity;
    private int dx, dy;

    public Bash(Repository repo) {
        this.repo = repo;
    }

    @Override
    public boolean initialized(Entity entity, Tile target) {
        setRange(entity);
        toggleRangeDisplay();
        return true;
    }

    @Override
    public boolean act(Entity entity, Tile target) {
        return bash(entity, target);
    }

    private void setRange(Entity entity) {
        List<Tile> pattern = new ArrayList<Tile>();
        int centerX = entity.getX() / Constants.TILESIZE;
        int centerY = entity.getY() / Constants.TILESIZE;
        Tile center = repo.findTile(centerX, centerY);

        // pattern: 1 tile all
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                Tile tile = repo.findTile(centerX + dx, centerY + dy);
                if (tile != null && tile != center && tile.isFloor()) {
                    pattern.add(tile);
                }
            }
        }
        this.range = repo.rayCaster.instantiate(entity, pattern, 5);
    }

    private void toggleRangeDisplay() {
        for (Tile tile : range) {
            tile.toggleHighlighted();
        }
    }

    private boolean bash(Entity entity, Tile target) {
        if (target == null) {
            return false;
        }
        Entity targetEntity = repo.findEntity(target.x, target.y);
        if (!range.contains(target) || targetEntity == null) {
            return false;
        }
        this.targettedEntity = targetEntity;
        int entityX = entity.getX() / Constants.TILESIZE;
        int entityY = entity.getY() / Constants.TILESIZE;
        int targetEntityX = targetEntity.getX() / Constants.TILESIZE;
        int targetEntityY = targetEntity.getY() / Constants.TILESIZE;
        this.dx = targetEntityX - entityX;
        this.dy = targetEntityY - entityY;
        int newX = entityX + dx;
        int newY = entityY + dy;
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

    @Override
    public void resolve(Entity entity) {
        toggleRangeDisplay();
        if (targettedEntity != null) {
            targettedEntity.setBeingAttacked();
            targettedEntity.takePhysicalDamage(entity.doPhysicalDamage());
            if (targettedEntity.isDead()) {
                repo.placeRemains(targettedEntity);
            }
        }
    }
}

