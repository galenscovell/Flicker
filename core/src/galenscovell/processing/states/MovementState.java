package galenscovell.processing.states;

import galenscovell.processing.actions.*;
import galenscovell.things.entities.Entity;
import galenscovell.world.Tile;

import java.util.*;

public class MovementState implements State {
    private Action moveAction;
    private Map<Integer, Tile> tiles;
    private List<Entity> entities;

    public MovementState(Map<Integer, Tile> tiles) {
        this.moveAction = new MoveAction();
        this.tiles = tiles;
    }

    public void enter() {

    }

    public void exit() {

    }

    public void update(float delta) {

    }

    public void handleInput(float x, float y) {
        // moveAction.updateMovement(x, y);
    }
}
