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
    private Entity user;
    private Tile targettedTile;
    private Entity targettedEntity;

    public Lunge(Entity user, Repository repo) {
        this.user = user;
        this.repo = repo;
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
    public boolean initialize() {
        setRange();
        enableRangeDisplay();
        return true;
    }

    @Override
    public boolean act() {
        return lunge();
    }

    private void setRange() {
        List<Tile> pattern = new ArrayList<Tile>();
        int centerX = user.getX() / Constants.TILESIZE;
        int centerY = user.getY() / Constants.TILESIZE;
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
        this.range = repo.rayCaster.instantiate(user, pattern, 5);
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

    private boolean lunge() {
        if (targettedTile == null) {
            return false;
        }
        Entity targetEntity = repo.findEntity(targettedTile.x, targettedTile.y);
        if (!range.contains(targettedTile) || targetEntity == null) {
            return false;
        }
        disableRangeDisplay();
        this.targettedEntity = targetEntity;
        int entityX = user.getX() / Constants.TILESIZE;
        int entityY = user.getY() / Constants.TILESIZE;
        int targetEntityX = targetEntity.getX() / Constants.TILESIZE;
        int targetEntityY = targetEntity.getY() / Constants.TILESIZE;
        int newX = entityX + ((targetEntityX - entityX) / 2);
        int newY = entityY + ((targetEntityY - entityY) / 2);
        return finalizeLunge(newX, newY);
    }

    private boolean finalizeLunge(int newX, int newY) {
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
}
