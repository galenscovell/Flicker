package galenscovell.logic;

import galenscovell.entities.Entity;
import galenscovell.entities.Player;
import galenscovell.inanimates.Inanimate;
import galenscovell.screens.HudStage;
import galenscovell.util.Constants;

import java.util.List;
import java.util.Map;

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
    private List<Entity> entities;
    private List<Inanimate> inanimates;
    private Player player;
    private EntityPathfinder pathfinder;
    private CombatKit combatKit;

    public Updater(Player player, Map<Integer, Tile> tiles) {
        this.player = player;
        this.tiles = tiles;
        this.tileSize = Constants.TILESIZE;
        this.pathfinder = new EntityPathfinder();
        this.combatKit = new CombatKit(tiles);
    }

    public void setHud(HudStage hud) {
        this.hud = hud;
    }

    public void setLists(List<Entity> entities, List<Inanimate> inanimates) {
        this.entities = entities;
        this.inanimates = inanimates;
    }

    public boolean update(int[] destination) {
        if (hud.restrictMovement() || !hud.clearMenus()) {
            return false;
        }
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
            // End player's turn, move all other entities
            npcTurn();
        }
        return true;
    }

    public void examine(float x, float y) {
        if (!hud.clearMenus()) {
            return;
        }
        // Pull info about entity or object at selection, if found turn off examine mode
        int tileX = (int) x / tileSize;
        int tileY = (int) y / tileSize;
        Entity entity = findEntity(tileX, tileY);
        if (entity != null) {
            // Display examine info on HUD
            hud.displayExamineInfo(entity.examine(), entity.getSprite());
            hud.modeChange(1);
        } else {
            Inanimate inanimate = findInanimate(tileX, tileY);
            if (inanimate != null) {
                // Display examine info on HUD
                hud.displayExamineInfo(inanimate.examine(), inanimate.getSprite());
                hud.modeChange(1);
            }
        }
    }

    public void interact(float x, float y) {
        if (!hud.clearMenus()) {
            return;
        }
        // Interact with selected object
        int tileX = (int) x / tileSize;
        int tileY = (int) y / tileSize;
        Inanimate inanimate = findInanimate(tileX, tileY);
        if (inanimate != null) {

        }
    }

    public void attackMode() {
        combatKit.setRange(player, "lunge");
        combatKit.toggleDisplay();
    }

    public void attack(float x, float y) {
        if (!hud.clearMenus()) {
            return;
        }
        // Attack a selected entity, if found turn off attack mode
        int tileX = (int) x / tileSize;
        int tileY = (int) y / tileSize;
        Entity target = findEntity(tileX, tileY);
        if (target != null) {
            hit(player, target);
            npcTurn();
            hud.modeChange(0);
        } else {
            combatKit.toggleDisplay();
            hud.modeChange(0);
        }
    }

    public Tile getTile(float x, float y) {
        int tileX = (int) (x / tileSize);
        int tileY = (int) (y / tileSize);
        return findTile(tileX, tileY);
    }

    public boolean descend() {
        return false;
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

    private void hit(Entity entity, Entity target) {
        entity.setAttacking();
        target.setBeingAttacked();
    }

    private Inanimate findInanimate(int x, int y) {
        Inanimate inanimate = null;
        for (Inanimate object : inanimates) {
            if (object.getX() == x && object.getY() == y) {
                inanimate = object;
            }
        }
        return inanimate;
    }

    private Entity findEntity(int x, int y) {
        Entity target = null;
        for (Entity entity : entities) {
            if ((entity.getX() / tileSize) == x && (entity.getY() / tileSize) == y) {
                target = entity;
            }
        }
        return target;
    }

    private Tile findTile(int x, int y) {
        return tiles.get(x * Constants.MAPSIZE + y);
    }
}