package galenscovell.processing.states;

import galenscovell.processing.*;
import galenscovell.things.entities.*;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

public class MovementState implements State {
    private Hero hero;
    private Pathfinder pathfinder;
    private Tile destination;

    public MovementState(Hero hero) {
        this.hero = hero;
        this.pathfinder = new Pathfinder();
        this.destination = null;
    }

    public void enter() {

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
        System.out.println(x + ", " + y);
        destination = Repository.findTile(x, y);
        destination.toggleHighlighted();
    }

    private void npcTurn() {
        for (Entity entity : Repository.entities) {
            if (entity.movementTimer()) {
                if (entity.isAggressive()) {
                    Tile heroTile = Repository.findTile(hero.getX(), hero.getY());
                    findPath(entity, heroTile);
                } else {
                    Tile heroTile = Repository.findTile(hero.getX(), hero.getY());
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
        Tile startTile = Repository.findTile(entity.getX(), entity.getY());
        if (endTile == null || startTile == endTile) {
            return false;
        } else {
            entity.setPathStack(pathfinder.findPath(Repository.tiles, startTile, endTile));
            return true;
        }
    }

    public boolean move(Entity entity, int x, int y) {
        int entityX = (entity.getX() / Constants.TILESIZE);
        int entityY = (entity.getY() / Constants.TILESIZE);
        int diffX = x - entityX;
        int diffY = y - entityY;
        Tile nextTile = Repository.findTile(x, y);
        if (nextTile.isFloor() && !nextTile.isOccupied()) {
            Repository.findTile(entityX, entityY).toggleOccupied();
            entity.move(diffX * Constants.TILESIZE, diffY * Constants.TILESIZE, true);
            nextTile.toggleOccupied();
            return true;
        } else {
            entity.move(diffX, diffY, false);
            return false;
        }
    }
}
