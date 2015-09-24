package galenscovell.processing.states;

import galenscovell.processing.*;
import galenscovell.things.entities.*;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class CombatState implements State {
    private Hero hero;
    private Repository repo;
    private Pathfinder pathfinder;
    private Tile target;
    private List<Tile> range;
    private String currentMove;

    public CombatState(Hero hero, Repository repo) {
        this.hero = hero;
        this.repo = repo;
        this.pathfinder = new Pathfinder();
        this.target = null;
        this.range = new ArrayList<Tile>();
        this.currentMove = "";
    }

    public void enter() {
        target = null;
    }

    public void exit() {

    }

    public void update(float delta) {

    }

    public void handleInput(float x, float y) {
        int convertX = (int) (x / Constants.TILESIZE);
        int convertY = (int) (y / Constants.TILESIZE);
        target = repo.findTile(convertX, convertY);
    }

    public void handleInterfaceEvent(String event) {
        initMove(hero, event);
    }

    private void initMove(Entity entity, String move) {
        for (Tile tile : range) {
            tile.toggleHighlighted();
        }
        range.clear();
        if (move.equals("clear") || currentMove.equals(move)) {
            currentMove = "";
        } else {
            setRange(entity, move);
        }
    }

    private void setRange(Entity entity, String move) {
        currentMove = move;
        int centerX = entity.getX() / Constants.TILESIZE;
        int centerY = entity.getY() / Constants.TILESIZE;
        Tile center = repo.findTile(centerX, centerY);

        if (move.equals("lunge")) {
            // Range: 2 tiles cardinal
            for (int dx = -2; dx <= 2; dx++) {
                Tile tile = repo.findTile(centerX + dx, centerY);
                if (tile != null && tile != center && tile.isFloor()) {
                    range.add(tile);
                }
            }
            for (int dy = -2; dy <= 2; dy++) {
                Tile tile = repo.findTile(centerX, centerY + dy);
                if (tile != null && tile != center && tile.isFloor()) {
                    range.add(tile);
                }
            }
        } else if (move.equals("roll")) {
            // Range: 2 tiles diagonal
            for (int dx = -2; dx <= 2; dx++) {
                for (int dy = -2; dy <= 2; dy++) {
                    if (Math.abs(dx) != Math.abs(dy)) {
                        continue;
                    }
                    Tile tile = repo.findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        range.add(tile);
                    }
                }
            }
        } else if (move.equals("bash")) {
            // Range: 1 tile all
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    Tile tile = repo.findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        range.add(tile);
                    }
                }
            }
        } else if (move.equals("leap")) {
            // Range: Circle radius 3
            for (int dx = -3; dx <= 3; dx++) {
                for (int dy = -3; dy <= 3; dy++) {
                    if (Math.abs(dx) == 3 && Math.abs(dy) == 3) {
                        continue;
                    }
                    Tile tile = repo.findTile(centerX + dx, centerY + dy);
                    if (tile != null && tile != center && tile.isFloor()) {
                        range.add(tile);
                    }
                }
            }
        }
        for (Tile tile : range) {
            tile.toggleHighlighted();
        }
    }
//
//    public void finalizeMove(Entity entity, Entity targetEntity, Tile targetTile) {
//        if (currentMove.equals("lunge")) {
//            lunge(entity, targetEntity, targetTile);
//        } else if (currentMove.equals("roll")) {
//            roll(entity, targetEntity, targetTile);
//        } else if (currentMove.equals("bash")) {
//            bash(entity, targetEntity, targetTile);
//        } else if (currentMove.equals("leap")) {
//            leap(entity, targetEntity, targetTile);
//        }
//        removeRange();
//    }
//
//    public void lunge(Entity entity, Entity targetEntity, Tile targetTile) {
//        if (!range.contains(targetTile) || targetEntity == null) {
//            return;
//        }
//        int entityX = (entity.getX() / tileSize);
//        int entityY = (entity.getY() / tileSize);
//        int targetEntityX = (targetEntity.getX() / tileSize);
//        int targetEntityY = (targetEntity.getY() / tileSize);
//        int newX = entityX + ((targetEntityX - entityX) / 2);
//        int newY = entityY + ((targetEntityY - entityY) / 2);
//        updater.move(entity, newX, newY);
//    }
//
//    public void roll(Entity entity, Entity targetEntity, Tile targetTile) {
//        if (!range.contains(targetTile) || targetEntity != null) {
//            return;
//        }
//        updater.move(entity, targetTile.x, targetTile.y);
//    }
//
//    public void bash(Entity entity, Entity targetEntity, Tile targetTile) {
//
//    }
//
//    public void leap(Entity entity, Entity targetEntity, Tile targetTile) {
//
//    }
//
//    private Tile findTile(int x, int y) {
//        return tiles.get(x * Constants.MAPSIZE + y);
//    }
//
//    public Tile getTileInRange(int x, int y) {
//        return findTileInRange(x, y);
//    }
//
//    private Tile findTileInRange(int x, int y) {
//        Tile tile = null;
//        for (Tile t : range) {
//            if (t.x == x && t.y == y) {
//                tile = t;
//            }
//        }
//        return tile;
//    }

}
