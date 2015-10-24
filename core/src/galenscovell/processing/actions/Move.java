package galenscovell.processing.actions;

import galenscovell.processing.*;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.Stack;

public class Move implements Action {
    private Repository repo;
    private Pathfinder pathfinder;

    public Move(Repository repo) {
        this.repo = repo;
        this.pathfinder = new Pathfinder();
    }

    public boolean initialized(Entity entity, Tile endTile) {
        int convertX = entity.getX() / Constants.TILESIZE;
        int convertY = entity.getY() / Constants.TILESIZE;
        Tile startTile = repo.findTile(convertX, convertY);
        if (endTile == null || startTile == endTile) {
            return false;
        } else {
            entity.setPathStack(pathfinder.findPath(startTile, endTile, repo));
            return true;
        }
    }

    public boolean act(Entity entity, Tile tile) {
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

    public void resolve(Entity entity) {
        entity.setPathStack(null);
    }
}
