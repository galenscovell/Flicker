package galenscovell.processing.actions.Skills;

import galenscovell.processing.*;
import galenscovell.processing.actions.*;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Lunge implements Action {
    private final Repository repo;
    private List<Tile> range;
    private Entity targettedEntity;

    public Lunge(Repository repo) {
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
        return lunge(entity, target);
    }

    private void setRange(Entity entity) {
        List<Tile> pattern = new ArrayList<Tile>();
        int centerX = entity.getX() / Constants.TILESIZE;
        int centerY = entity.getY() / Constants.TILESIZE;
        Tile center = repo.findTile(centerX, centerY);

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
        this.range = repo.rayCaster.instantiate(entity, pattern, 5);
    }

    private void toggleRangeDisplay() {
        for (Tile tile : range) {
            tile.toggleHighlighted();
        }
    }

    private boolean lunge(Entity entity, Tile target) {
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
