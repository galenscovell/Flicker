package galenscovell.logic;

import galenscovell.entities.Entity;
import galenscovell.entities.Player;
import galenscovell.inanimates.Inanimate;
import galenscovell.screens.HudStage;
import galenscovell.util.Constants;

import java.util.List;
import java.util.Map;
import java.util.Random;
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
    private Inanimate stairs;
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
        if (!findPath(player, destination[0], destination[1])) {
            return false;
        }
        if (player.getPathStack() == null || player.getPathStack().isEmpty()) {
            return false;
        } else {
            Point nextMove = player.getPathStack().pop();
            move(player, nextMove.x, nextMove.y);
        }

        for (Entity entity : entities) {
            if (entity.movementTimer()) {
                if (entity.getPathStack() == null || entity.getPathStack().isEmpty()) {
                    if (entity.isInView()) {
                        findPath(entity, player.getX(), player.getY());
                    } else {
                        npcPassiveMove(entity);
                    }
                } else {
                    Point nextMove = entity.getPathStack().pop();
                    move(entity, nextMove.x, nextMove.y);
                }
            }
        }
        return true;
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

    public void interact() {

    }

    public boolean descend() {
        return false;
    }

    public Tile getTile(float x, float y) {
        int tileX = (int) (x / tileSize);
        int tileY = (int) (y / tileSize);
        return findTile(tileX, tileY);
    }

    private void move(Entity entity, int x, int y) {
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
        Tile nextTile = findTile(entityX + dx, entityY + dy);
        if (nextTile.isFloor() && !nextTile.isOccupied()) {
            Tile currentTile = findTile(entityX, entityY);
            currentTile.toggleOccupied();
            entity.move(dx * tileSize, dy * tileSize, true);
            nextTile.toggleOccupied();
        } else {
            entity.move(dx, dy, false);
        }
    }

    private void npcPassiveMove(Entity entity) {

    }

    private void npcAggressiveMove(Entity entity) {

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