
/**
 * UPDATER CLASS
 * Handles game logic: interactions, movements, behaviors and HUD updates.
 */

package galenscovell.logic;

import galenscovell.entities.Entity;
import galenscovell.entities.Player;

import galenscovell.inanimates.Dead;
import galenscovell.inanimates.Inanimate;

import galenscovell.screens.HudDisplay;

import galenscovell.util.Constants;

import java.util.List;
import java.util.Map;
import java.util.Random;


public class Updater {
    private int tileSize;
    private HudDisplay hud;
    private Map<Integer, Tile> tiles;
    private Player player;
    private Inanimate stairs;


    public Updater(Map<Integer, Tile> tiles) {
        this.tiles = tiles;
        this.tileSize = Constants.TILESIZE;
    }

    public void setHud(HudDisplay hud) {
        this.hud = hud;
    }

    public void update(int[] input, boolean moving, boolean acting, List<Entity> entities, List<Inanimate> inanimates) {
        if (acting) {
            if (input[2] == 1) {
                playerInteract(inanimates);
            } else if (input[2] == -1 && !player.isAttacking()) {
                playerAttack(entities, inanimates);
            }
        }

        if (moving) {
            if (!player.isAttacking()) {
                playerMove(input[0], input[1]);
            }
        }

        if (moving || acting) {
            for (Entity entity : entities) {
                if (entity.movementTimer()) {
                    entityMove(entity);
                }
            }
        }
    }

    public void setPlayer(Player playerInstance) {
        this.player = playerInstance;
    }

    public void setStairs(List<Inanimate> inanimates) {
        for (Inanimate inanimate : inanimates) {
            if (inanimate.getType().equals("Stairs")) {
                this.stairs = inanimate;
            }
        }
    }

    public boolean playerDescends() {
        return ((player.getCurrentX() / tileSize) == stairs.getX() && (player.getCurrentY() / tileSize) == stairs.getY());
    }

    private void playerInteract(List<Inanimate> inanimates) {
        player.toggleInteracting();
        Point facingPoint = player.getFacingPoint(tileSize);
        Tile facingTile = findTile(facingPoint.x, facingPoint.y);
        for (Inanimate inanimate : inanimates) {
            if (inanimate.getX() == facingTile.x && inanimate.getY() == facingTile.y) {
                inanimate.interact(facingTile);
                return;
            }
        }
        System.out.println("There doesn't appear to be anything here.");
    }

    private void playerAttack(List<Entity> entities, List<Inanimate> inanimates) {
        player.toggleAttack();
        Point attackedTile = player.getFacingPoint(tileSize);
        player.setAttackingCoords(attackedTile.x * tileSize, attackedTile.y * tileSize);
        Entity hitEntity = null;

        for (Entity entity : entities) {
            if (entity.getX() == attackedTile.x * tileSize && entity.getY() == attackedTile.y * tileSize) {
                Tile tile = findTile(attackedTile.x, attackedTile.y);
                tile.toggleOccupied();
                hitEntity = entity;
            }
        }

        if (hitEntity != null) {
            entities.remove(hitEntity);
            inanimates.add(new Dead(attackedTile.x, attackedTile.y));
        }
    }

    private void playerMove(int dx, int dy) {
        int playerX = (player.getX() / tileSize);
        int playerY = (player.getY() / tileSize);
        Tile nextTile = findTile(playerX + dx, playerY + dy);
        if (nextTile.isFloor() && !nextTile.isOccupied()) {
            Tile currentTile = findTile(playerX, playerY);
            currentTile.toggleOccupied();
            player.move(dx * tileSize, dy * tileSize, true);
            nextTile.toggleOccupied();
        } else {
            player.move(dx, dy, false);
        }
    }

    private void entityMove(Entity entity) {
        if (entity.isInView()) {
            entityAggressiveMove(entity);
        } else {
            entityPassiveMove(entity);
        }
    }

    private void entityPassiveMove(Entity entity) {
        Random generator = new Random();
        int dx = 0;
        int dy = 0;

        int choice = generator.nextInt(2);
        if (choice == 0) {
            dx += generator.nextInt(3) - 1;
        } else if (choice == 1) {
            dy += generator.nextInt(3) - 1;
        }

        int entityX = (entity.getX() / tileSize);
        int entityY = (entity.getY() / tileSize);
        Tile nextTile = findTile(entityX + dx, entityY + dy);
        if (nextTile.isFloor() && !nextTile.isOccupied()) {
            // If possible, move to new Tile and set old Tile as unoccupied
            Tile currentTile = findTile(entityX, entityY);
            currentTile.toggleOccupied();
            entity.move(dx * tileSize, dy * tileSize, true);
            nextTile.toggleOccupied();
        } else {
            // Otherwise just turn in that direction
            entity.move(dx * tileSize, dy * tileSize, false);
        }
    }

    private void entityAggressiveMove(Entity entity) {
        int entityX = (entity.getX() / tileSize);
        int entityY = (entity.getY() / tileSize);
        int diffX = (entityX - (player.getX() / tileSize));
        int diffY = (entityY - (player.getY() / tileSize));

        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;

        // Attack if horizontally or vertically aligned with and adjacent to Player
        if ((diffX == 0 && Math.abs(diffY) == 1) || (diffY == 0 && Math.abs(diffX) == 1)) {
            entityAttack(entity);
            return;
        }

        Tile upTile = findTile(entityX, entityY - 1);
        Tile downTile = findTile(entityX, entityY + 1);
        if (diffY >= 1 && upTile.isFloor() && !upTile.isOccupied()) {
            up = true;
        } else if (diffY <= -1 && downTile.isFloor() && !downTile.isOccupied()) {
            down = true;
        }

        Tile leftTile = findTile(entityX - 1, entityY);
        Tile rightTile = findTile(entityX + 1, entityY);
        if (diffX >= 1 && leftTile.isFloor() && !leftTile.isOccupied()) {
            left = true;
        } else if (diffX <= -1 && rightTile.isFloor() && !rightTile.isOccupied()) {
            right = true;
        }

        int dx = 0;
        int dy = 0;
        Random generator = new Random();
        int choice = generator.nextInt(2);

        Tile movedTile = null;
        if (choice == 0 && up) {
            dy--;
            movedTile = upTile;
        } else if (choice == 0 && down) {
            dy++;
            movedTile = downTile;
        } else if (choice == 1 && left) {
            dx--;
            movedTile = leftTile;
        } else if (choice == 1 && right) {
            dx++;
            movedTile = rightTile;
        }

        if (movedTile != null) {
            Tile currentTile = findTile(entityX, entityY);
            currentTile.toggleOccupied();
            entity.move(dx * tileSize, dy * tileSize, true);
            movedTile.toggleOccupied();
        } else {
            return;
        }
    }

    private void entityAttack(Entity entity) {
        entity.toggleAttack();
    }

    private Tile findTile(int x, int y) {
        return tiles.get(x * Constants.TILE_COLUMNS + y);
    }
}