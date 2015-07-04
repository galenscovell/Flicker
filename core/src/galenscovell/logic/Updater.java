package galenscovell.logic;

import galenscovell.entities.Entity;
import galenscovell.entities.Player;
import galenscovell.inanimates.Inanimate;
import galenscovell.screens.HudStage;
import galenscovell.util.Constants;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * UPDATER
 * Handles game logic: interactions, movements, behaviors and HUD updates.
 *
 * @author Galen Scovell
 */

public class Updater {
    private int tileSize;
    private HudStage hud;
    private Map<Integer, Tile> tiles;
    private Player player;
    private Pathfinder pathfinder;

    public Updater(Player player, Map<Integer, Tile> tiles) {
        this.player = player;
        this.tiles = tiles;
        this.tileSize = Constants.TILESIZE;
        this.pathfinder = new Pathfinder();
    }

    public void setHud(HudStage hud) {
        this.hud = hud;
    }

    public boolean update(int[] destination, List<Entity> entities) {
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
                        nextMove = entity.getPathStack().pop();
                        if (!move(entity, nextMove.x, nextMove.y)) {
                            entity.setPathStack(null);
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean interact(float x, float y, List<Inanimate> inanimates) {
        Inanimate object = getObject((int) x / tileSize, (int) y / tileSize, inanimates);
        if (object != null) {
            hud.addToLog(object.interact(getTile(x, y)));
            return true;
        }
        return false;
    }

    public boolean descend() {
        return false;
    }

    public Tile getTile(float x, float y) {
        int tileX = (int) (x / tileSize);
        int tileY = (int) (y / tileSize);
        return findTile(tileX, tileY);
    }

    public Inanimate getObject(int x, int y, List<Inanimate> inanimates) {
        for (Inanimate object : inanimates) {
            if (object.getX() == x && object.getY() == y) {
                return object;
            }
        }
        return null;
    }

    private boolean findPath(Entity entity, int destX, int destY) {
        Tile startTile = getTile(entity.getX(), entity.getY());
        Tile endTile = getTile(destX, destY);
        if (endTile == null || startTile == endTile) {
            return false;
        } else {
            entity.setPathStack(pathfinder.findPath(tiles, startTile, endTile));
            return true;
        }
    }

    private boolean move(Entity entity, int x, int y) {
        int entityX = (entity.getX() / tileSize);
        int entityY = (entity.getY() / tileSize);
        int diffX = x - entityX;
        int diffY = y - entityY;
        int dx = 0;
        int dy = 0;
        if (diffX > 0) {
            dx++;
        } else if (diffX < 0) {
            dx--;
        }
        if (diffY > 0) {
            dy++;
        } else if (diffY < 0) {
            dy--;
        }
        Tile nextTile = findTile(x, y);
        if (nextTile.isFloor() && !nextTile.isOccupied()) {
            // Set current tile as unoccupied
            findTile(entityX, entityY).toggleOccupied();
            // Move to next tile and set it as occupied
            entity.move(dx * tileSize, dy * tileSize, true);
            nextTile.toggleOccupied();
            return true;
        } else {
            // Unable to move to tile, just turn in that direction
            entity.move(dx, dy, false);
            return false;
        }
    }

    private void npcAttack(Entity entity) {
        player.setBeingAttacked();
        entity.setAttacking();
        hud.updateChassis(entity.getStat("damage"));
        hud.addToLog(entity + " hits for " + entity.getStat("damage") + " damage.");
    }

    private Tile findTile(int x, int y) {
        return tiles.get(x * Constants.COLUMNS + y);
    }
}