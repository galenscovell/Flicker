package galenscovell.processing.states;

import galenscovell.processing.*;
import galenscovell.processing.actions.Move;
import galenscovell.things.entities.*;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

public class MovementState implements State {
    private Hero hero;
    private Repository repo;
    private Tile destination;
    private Move mover;

    public MovementState(Hero hero, Repository repo) {
        this.hero = hero;
        this.repo = repo;
        this.mover = new Move(repo);
    }

    public void enter() {
        destination = null;
    }

    public void exit() {

    }

    public void update(float delta) {
        if (destination == null || !mover.findPath(hero, destination) || hero.getPathStack() == null || hero.getPathStack().isEmpty()) {
            return;
        } else {
            Point nextMove = hero.getPathStack().pop();
            if (!mover.move(hero, nextMove.x, nextMove.y)) {
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
                    mover.findPath(entity, heroTile);
                } else {
                    Tile heroTile = repo.findTile(hero.getX(), hero.getY());
                    mover.findPath(entity, heroTile);
                    // TODO: Passive behavior, destination depends on entity
                }
                if (entity.getPathStack() == null || entity.getPathStack().isEmpty()) {
                    continue;
                } else {
                    Point nextMove = entity.getPathStack().pop();
                    if (!mover.move(entity, nextMove.x, nextMove.y)) {
                        entity.setPathStack(null);
                    }
                }
            }
        }
    }
}
