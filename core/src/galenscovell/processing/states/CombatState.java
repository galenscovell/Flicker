package galenscovell.processing.states;

import galenscovell.processing.actions.AttackAction;
import galenscovell.world.Tile;

import java.util.Map;

public class CombatState implements State {
    private AttackAction attackAction;
    private Map<Integer, Tile> tiles;

    public CombatState(Map<Integer, Tile> tiles) {
        this.attackAction = new AttackAction();
        this.tiles = tiles;
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
