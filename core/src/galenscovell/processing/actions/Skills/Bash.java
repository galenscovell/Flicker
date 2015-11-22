package galenscovell.processing.actions.Skills;

import galenscovell.processing.*;
import galenscovell.processing.actions.*;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Bash implements Action {
    private final Repository repo;
    private final Entity user;
    private List<Tile> range;
    private Tile targettedTile;
    private Entity targettedEntity;
    private int dx, dy;

    public Bash(Entity user, Repository repo) {
        this.user = user;
        this.repo = repo;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Bash", "Hit adjacent target and knock it back."};
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
        disableRangeDisplay();
        return bash();
    }

    private void setRange() {
        List<Tile> pattern = new ArrayList<Tile>();
        int centerX = user.getX() / Constants.TILESIZE;
        int centerY = user.getY() / Constants.TILESIZE;
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

    private boolean bash() {
        if (targettedTile == null) {
            return false;
        }
        Entity targetEntity = repo.findEntity(targettedTile.x, targettedTile.y);
        if (!range.contains(targettedTile) || targetEntity == null) {
            return false;
        }
        this.targettedEntity = targetEntity;
        int entityX = user.getX() / Constants.TILESIZE;
        int entityY = user.getY() / Constants.TILESIZE;
        int targetEntityX = targetEntity.getX() / Constants.TILESIZE;
        int targetEntityY = targetEntity.getY() / Constants.TILESIZE;
        this.dx = targetEntityX - entityX;
        this.dy = targetEntityY - entityY;
        int newX = entityX + dx;
        int newY = entityY + dy;
        return finalizeBash(newX, newY);
    }

    private boolean finalizeBash(int newX, int newY) {
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
            int newX = (targettedEntity.getX() / Constants.TILESIZE) + dx;
            int newY = (targettedEntity.getY() / Constants.TILESIZE) + dy;
            Action slide = new Slide(targettedEntity, repo);
            slide.setTarget(repo.findTile(newX, newY));
            slide.initialize();
            slide.act();
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

