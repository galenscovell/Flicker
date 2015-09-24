package galenscovell.processing.states;

import galenscovell.processing.*;
import galenscovell.things.entities.*;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

public class MovementState implements State {
    private Hero hero;
    private Repository repo;
    private Pathfinder pathfinder;
    private Tile destination;

    public MovementState(Hero hero, Repository repo) {
        this.hero = hero;
        this.repo = repo;
        this.pathfinder = new Pathfinder();
        this.destination = null;
    }

    public void enter() {
        destination = null;
    }

    public void exit() {

    }

    public void update(float delta) {
        if (destination == null || !findPath(hero, destination) || hero.getPathStack() == null || hero.getPathStack().isEmpty()) {
            return;
        } else {
            Point nextMove = hero.getPathStack().pop();
            if (!move(hero, nextMove.x, nextMove.y)) {
                hero.setPathStack(null);
                return;
            }
            npcTurn();
        }
    }

    public void handleInput(float x, float y) {
        int convertX = (int) (x / Constants.TILESIZE);
        int convertY = (int) (y / Constants.TILESIZE);
        destination = repo.findTile(convertX, convertY);
    }

    public void handleInterfaceEvent(String event) {

    }

    private void npcTurn() {
        for (Entity entity : repo.entities) {
            if (entity.movementTimer()) {
                if (entity.isAggressive()) {
                    Tile heroTile = repo.findTile(hero.getX(), hero.getY());
                    findPath(entity, heroTile);
                } else {
                    Tile heroTile = repo.findTile(hero.getX(), hero.getY());
                    findPath(entity, heroTile);
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

    private boolean findPath(Entity entity, Tile endTile) {
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

    private boolean move(Entity entity, int x, int y) {
        int entityX = entity.getX() / Constants.TILESIZE;
        int entityY = entity.getY() / Constants.TILESIZE;
        int diffX = x - entityX;
        int diffY = y - entityY;
        Tile nextTile = repo.findTile(x, y);
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
}
