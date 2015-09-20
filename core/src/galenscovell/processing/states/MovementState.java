package galenscovell.processing.states;

import galenscovell.processing.actions.*;

public class MovementState implements State {
    private Action moveAction;

    public MovementState() {
        this.moveAction = new MoveAction();
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
