package galenscovell.flicker.processing.actions;

import galenscovell.flicker.processing.*;
import galenscovell.flicker.things.entities.Entity;
import galenscovell.flicker.util.Constants;
import galenscovell.flicker.world.Tile;

public class Knockback implements Action {
    private final Repository repo;
    private final Pathfinder pathfinder;
    private final Entity user;
    private Tile targettedTile;

    public Knockback(Entity user, Repository repo) {
        this.user = user;
        this.repo = repo;
        this.pathfinder = new Pathfinder();
    }

    @Override
    public String[] getInfo() {
        return null;
    }

    @Override
    public boolean setTarget(Tile tile) {
        this.targettedTile = tile;
        return true;
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
        int convertX = user.getX() / Constants.TILESIZE;
        int convertY = user.getY() / Constants.TILESIZE;
        Tile startTile = repo.findTile(convertX, convertY);
        if (targettedTile == null || startTile == targettedTile) {
            return false;
        } else {
            user.populatePathStack(pathfinder.findPath(startTile, targettedTile, repo));
            return true;
        }
    }

    @Override
    public boolean act() {
        if (user.pathStackEmpty()) {
            return false;
        }
        Point targetPoint = user.nextPathPoint();
        int entityX = user.getX() / Constants.TILESIZE;
        int entityY = user.getY() / Constants.TILESIZE;
        int diffX = targetPoint.x - entityX;
        int diffY = targetPoint.y - entityY;
        Tile nextTile = repo.findTile(targetPoint.x, targetPoint.y);
        if (nextTile.isFloor() && !nextTile.isOccupied()) {
            repo.findTile(entityX, entityY).toggleOccupied();
            user.move(diffX * Constants.TILESIZE, diffY * Constants.TILESIZE, true, true);
            nextTile.toggleOccupied();
            return true;
        } else {
            user.move(diffX, diffY, false, true);
            return false;
        }
    }

    @Override
    public void resolve() {

    }

    @Override
    public void exit() {

    }
}
