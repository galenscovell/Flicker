package galenscovell.processing.actions;

import galenscovell.processing.*;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

public class Move implements Action {
    private final Repository repo;
    private final Pathfinder pathfinder;

    public Move(Repository repo) {
        this.repo = repo;
        this.pathfinder = new Pathfinder();
    }

    @Override
    public void define(int moveType) {

    }

    @Override
    public boolean initialized(Entity entity, Tile targetTile) {
        int convertX = entity.getX() / Constants.TILESIZE;
        int convertY = entity.getY() / Constants.TILESIZE;
        Tile startTile = repo.findTile(convertX, convertY);
        if (targetTile == null || startTile == targetTile) {
            return false;
        } else {
            entity.populatePathStack(pathfinder.findPath(startTile, targetTile, repo));
            return true;
        }
    }

    @Override
    public boolean act(Entity entity, Tile target) {
        if (entity.pathStackEmpty()) {
            return false;
        }
        Point targetPoint = entity.nextPathPoint();
        int entityX = entity.getX() / Constants.TILESIZE;
        int entityY = entity.getY() / Constants.TILESIZE;
        int diffX = targetPoint.x - entityX;
        int diffY = targetPoint.y - entityY;
        Tile nextTile = repo.findTile(targetPoint.x, targetPoint.y);
        if (nextTile.isFloor() && !nextTile.isOccupied()) {
            repo.findTile(entityX, entityY).toggleOccupied();
            entity.move(diffX * Constants.TILESIZE, diffY * Constants.TILESIZE, true);
            nextTile.toggleOccupied();
            return true;
        } else {
            entity.move(diffX, diffY, false);
            return false;
        }
    }

    @Override
    public void resolve(Entity entity) {

    }
}
