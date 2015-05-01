
/**
 * UPDATER CLASS
 * Handles game logic: interactions, movements, behaviors and HUD updates.
 */

package galenscovell.logic;

import galenscovell.entities.Dead;
import galenscovell.entities.Entity;
import galenscovell.entities.Player;

import galenscovell.inanimates.Inanimate;

import galenscovell.util.Constants;

import java.util.List;
import java.util.Map;
import java.util.Random;


public class Updater {
    private int tileSize;
    private Map<Integer, Tile> tiles;
    private Player player;
    private Inanimate stairs;


    public Updater(Map<Integer, Tile> tiles) {
        this.tiles = tiles;
        this.tileSize = Constants.TILESIZE;
    }

    public void update(int[] input, boolean movePressed, boolean attacking, boolean interacting, List<Entity> entities, List<Dead> deadList, List<Inanimate> inanimates) {
        if (attacking && !player.isAttacking()) {
            player.toggleAttack();
        }

        if (playerMove(input[0], input[1]) || movePressed || attacking) {
            if (player.isAttacking()) {
                playerAttack(entities, deadList);
            }

            for (Entity entity : entities) {
                entityMove(entity);
            }
        }

        if (interacting && !playerInteract(inanimates)) {
            System.out.println("There doesn't appear to be anything here.");
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

    public void deconstruct() {
        tiles = null;
    }

    private boolean playerInteract(List<Inanimate> inanimates) {
        Point facingPoint = player.getFacingPoint(tileSize);
        Tile facingTile = findTile(facingPoint.x, facingPoint.y);
        for (Inanimate inanimate : inanimates) {
            if (inanimate.getX() == facingTile.x && inanimate.getY() == facingTile.y) {
                inanimate.interact(facingTile);
                return true;
            }
        }
        return false;
    }

    private void playerAttack(List<Entity> entities, List<Dead> deadList) {
        Point attackedTile = player.getFacingPoint(tileSize);
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
            deadList.add(new Dead(attackedTile.x, attackedTile.y));
        }
    }

    private boolean playerMove(int dx, int dy) {
        // Prevent movement during player attack animation
        if (player.isAttacking() || (dx == 0 && dy == 0)) {
            return false;
        }

        int playerX = (player.getX() / tileSize);
        int playerY = (player.getY() / tileSize);

        Tile nextTile = findTile(playerX + dx, playerY + dy);
        if (nextTile.isFloor() && !nextTile.isOccupied()) {
            Tile currentTile = findTile(playerX, playerY);
            currentTile.toggleOccupied();
            player.move(dx * tileSize, dy * tileSize);
            nextTile.toggleOccupied();
            return true;
        } else {
            player.turn(dx, dy);
            return false;
        }
    }

    private void entityMove(Entity entity) {
        if (entity.isMoveTime()) {
            if (entity.isInView()) {
                entityAggressiveMove(entity);
            } else {
                entityPassiveMove(entity);
            }
            entity.resetMoveTime();
        } else {
            entity.incrementMoveTime();
        }

    }

    private void entityPassiveMove(Entity entity) {
        Random generator = new Random();
        int entityX = (entity.getX() / tileSize);
        int entityY = (entity.getY() / tileSize);
        int dx = 0;
        int dy = 0;

        int choice = generator.nextInt(2);
        if (choice == 0) {
            dx += generator.nextInt(3) - 1;
        } else if (choice == 1) {
            dy += generator.nextInt(3) - 1;
        }

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

        // If entity is horizontally or vertically aligned with and adjacent to Player, attack
        if ((diffX == 0 && (diffY == 1 || diffY == -1)) || (diffY == 0 && (diffX == 1 || diffX == -1))) {
            entityAttack(entity);
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

        Tile currentTile = findTile(entityX, entityY);
        if (choice == 0 && up) {
            currentTile.toggleOccupied();
            dy--;
            upTile.toggleOccupied();
        } else if (choice == 0 && down) {
            currentTile.toggleOccupied();
            dy++;
            downTile.toggleOccupied();
        } else if (choice == 1 && left) {
            currentTile.toggleOccupied();
            dx--;
            leftTile.toggleOccupied();
        } else if (choice == 1 && right) {
            currentTile.toggleOccupied();
            dx++;
            rightTile.toggleOccupied();
        }
        entity.move(dx * tileSize, dy * tileSize, true);
    }

    private void entityAttack(Entity entity) {
        entity.toggleAttacking();
    }

    private Tile findTile(int x, int y) {
        int key = x * Constants.TILE_COLUMNS + y;
        if (tiles.containsKey(key)) {
            return tiles.get(key);
        } else {
            return null;
        }
    }
}