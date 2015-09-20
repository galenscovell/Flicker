package galenscovell.processing.states;

import galenscovell.processing.actions.*;

public class CombatState implements State {
    private Action attackAction;

    public CombatState() {
        this.attackAction = new AttackAction();
    }

    public void enter() {

    }

    public void exit() {

    }

    public void update(float delta) {

    }

    public void handleInput(float x, float y) {

    }
}
