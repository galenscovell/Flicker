package galenscovell.processing.actions;

import galenscovell.processing.*;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

public class Move implements Action {
    private final Repository repo;
    private final Pathfinder pathfinder;
    private final Entity user;
    private Tile targettedTile;

    public Move(Entity user, Repository repo) {
        this.user = user;
        this.repo = repo;
        this.pathfinder = new Pathfinder();
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
            user.move(diffX * Constants.TILESIZE, diffY * Constants.TILESIZE, true, false);
            nextTile.toggleOccupied();
            return true;
        } else {
            user.move(diffX, diffY, false, false);
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
