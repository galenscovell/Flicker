package galenscovell.processing.states;

import galenscovell.processing.*;
import galenscovell.processing.actions.Move;
import galenscovell.things.entities.*;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class CombatState implements State {
    private Hero hero;
    private Repository repo;
    private Tile tileTarget;
    private Entity targetEntity;
    private List<Tile> range;
    private String currentMove;
    private boolean moveDisplayed;
    private Move mover;

    public CombatState(Hero hero, Repository repo) {
        this.hero = hero;
        this.repo = repo;
        this.range = new ArrayList<Tile>();
        this.mover = new Move(repo);
        this.currentMove = "";
    }

    public void enter() {
        tileTarget = null;
        targetEntity = null;
    }

    public void exit() {

    }

    public void update(float delta) {

    }

    public void handleInput(float x, float y) {
        int convertX = (int) (x / Constants.TILESIZE);
        int convertY = (int) (y / Constants.TILESIZE);
        tileTarget = repo.findTile(convertX, convertY);
        targetEntity = repo.findEntity(convertX, convertY);
        finalizeMove(hero, targetEntity, tileTarget);
    }

    public void handleInterfaceEvent(String event) {
        if (!moveDisplayed) {
            initMove(hero, event);
            moveDisplayed = true;
        }
    }

    private void initMove(Entity entity, String move) {
        for (Tile tile : range) {
            tile.toggleHighlighted();
        }
        range.clear();
        tileTarget = null;
        targetEntity = null;
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

    public void removeRangeDisplay() {
        for (Tile tile : range) {
            tile.toggleHighlighted();
        }
        moveDisplayed = false;
    }

    public void finalizeMove(Entity entity, Entity targetEntity, Tile targetTile) {
        if (currentMove.equals("lunge")) {
            lunge(entity, targetEntity, targetTile);
        } else if (currentMove.equals("roll")) {
            roll(entity, targetEntity, targetTile);
        } else if (currentMove.equals("bash")) {
            bash(entity, targetEntity, targetTile);
        } else if (currentMove.equals("leap")) {
            leap(entity, targetEntity, targetTile);
        }
        removeRangeDisplay();
    }

    public void lunge(Entity entity, Entity targetEntity, Tile targetTile) {
        if (!range.contains(targetTile) || targetEntity == null) {
            return;
        }
        int entityX = (entity.getX() / Constants.TILESIZE);
        int entityY = (entity.getY() / Constants.TILESIZE);
        int targetEntityX = (targetEntity.getX() / Constants.TILESIZE);
        int targetEntityY = (targetEntity.getY() / Constants.TILESIZE);
        int newX = entityX + ((targetEntityX - entityX) / 2);
        int newY = entityY + ((targetEntityY - entityY) / 2);
        mover.move(hero, newX, newY);
    }

    public void roll(Entity entity, Entity targetEntity, Tile targetTile) {

    }

    public void bash(Entity entity, Entity targetEntity, Tile targetTile) {

    }

    public void leap(Entity entity, Entity targetEntity, Tile targetTile) {

    }

    private Tile findTileInRange(int x, int y) {
        Tile tile = null;
        for (Tile t : range) {
            if (t.x == x && t.y == y) {
                tile = t;
            }
        }
        return tile;
    }
}
