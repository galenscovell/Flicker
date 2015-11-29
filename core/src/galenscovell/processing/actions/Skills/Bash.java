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
    private final List<Tile> range;
    private Tile targettedTile;
    private Entity targettedEntity;
    private int slideX, slideY;

    public Bash(Entity user, Repository repo) {
        this.user = user;
        this.repo = repo;
        this.range = new ArrayList<Tile>();
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Bash", "Hit adjacent target and knock it back."};
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
        Tile center = repo.findTile(centerX, centerY);

        // pattern: 1 tile all
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                Tile tile = repo.findTile(centerX + dx, centerY + dy);
                if (tile != null && tile != center && tile.isFloor() && !tile.isBlocking()) {
                    tile.highlightOrange();
                    range.add(tile);
                }
            }
        }
    }

    @Override
    public boolean act() {
        disableRangeDisplay();
        return bash();
    }

    private void disableRangeDisplay() {
        for (Tile tile : range) {
            tile.disableHighlight();
        }
    }

    private boolean bash() {
        int entityX = user.getX() / Constants.TILESIZE;
        int entityY = user.getY() / Constants.TILESIZE;
        int targetEntityX = targettedEntity.getX() / Constants.TILESIZE;
        int targetEntityY = targettedEntity.getY() / Constants.TILESIZE;
        this.slideX = targetEntityX - entityX;
        this.slideY = targetEntityY - entityY;
        int newX = entityX + slideX;
        int newY = entityY + slideY;

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
            int dmg = user.doPhysicalDamage();
            targettedEntity.takePhysicalDamage(dmg);
            repo.updateCombatText(targettedEntity, dmg);
            int newX = (targettedEntity.getX() / Constants.TILESIZE) + slideX;
            int newY = (targettedEntity.getY() / Constants.TILESIZE) + slideY;
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

