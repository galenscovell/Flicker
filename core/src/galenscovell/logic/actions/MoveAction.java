package galenscovell.logic.actions;

import galenscovell.logic.Pathfinder;
import galenscovell.logic.Point;
import galenscovell.logic.Updater;
import galenscovell.logic.world.Tile;
import galenscovell.things.entities.Entity;
import galenscovell.things.entities.Player;

import java.util.List;
import java.util.Map;

/**
 * MOVEMENT KIT
 * Movement component handler
 *
 * @author Galen Scovell
 */

public class MoveAction {
    private Updater updater;
    private Map<Integer, Tile> tiles;
    private List<Entity> entities;
    private Player player;
    private Pathfinder pathfinder;

    public MoveAction(Updater updater, Player player, Map<Integer, Tile> tiles) {
        this.updater = updater;
        this.player = player;
        this.tiles = tiles;
        this.pathfinder = new Pathfinder();
    }

    public boolean updateMovement(int[] destination) {
        if (!findPath(player, destination[0], destination[1]) || player.getPathStack() == null || player.getPathStack().isEmpty()) {
            return false;
        } else {
            Point nextMove = player.getPathStack().pop();
            if (move(player, nextMove.x, nextMove.y)) {
                // TODO: Movement power usage and regeneration
            } else {
                player.setPathStack(null);
                return false;
            }
            npcTurn();
        }
        return true;
    }

    private void npcTurn() {
        for (Entity entity : entities) {
            if (entity.movementTimer()) {
                if (entity.isAggressive()) {
                    findPath(entity, player.getX(), player.getY());
                } else {
                    findPath(entity, player.getX(), player.getY());
                    // TODO: Passive behavior, destination depends on entity
                }
                if (entity.getPathStack() == null || entity.getPathStack().isEmpty()) {
                    continue;
                } else {
                    Point nextMove = entity.getPathStack().pop();
                    if (!move(entity, nextMove.x, nextMove.y)) {
                        entity.setPathStack(null);
                    }
                }
            }
        }
    }

    private boolean findPath(Entity entity, int destX, int destY) {
        Tile startTile = updater.getTile(entity.getX(), entity.getY());
        Tile endTile = updater.getTile(destX, destY);
        if (endTile == null || startTile == endTile) {
            return false;
        } else {
            entity.setPathStack(pathfinder.findPath(tiles, startTile, endTile));
            return true;
        }
    }

    public boolean move(Entity entity, int x, int y) {
        int entityX = (entity.getX() / tileSize);
        int entityY = (entity.getY() / tileSize);
        int diffX = x - entityX;
        int diffY = y - entityY;
        Tile nextTile = updater.findTile(x, y);
        if (nextTile.isFloor() && !nextTile.isOccupied()) {
            findTile(entityX, entityY).toggleOccupied();
            entity.move(diffX * tileSize, diffY * tileSize, true);
            nextTile.toggleOccupied();
            return true;
        } else {
            entity.move(diffX, diffY, false);
            return false;
        }
    }

}
