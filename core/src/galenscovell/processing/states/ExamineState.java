package galenscovell.processing.states;

import galenscovell.processing.Repository;
import galenscovell.things.entities.Hero;
import galenscovell.ui.screens.GameScreen;

public class ExamineState implements State {
    private final GameScreen root;
    private final Hero hero;
    private final Repository repo;

    public ExamineState(GameScreen root, Hero hero, Repository repo) {
        this.root = root;
        this.hero = hero;
        this.repo = repo;
    }

    @Override
    public StateType getStateType() {
        return StateType.EXAMINE;
    }

    @Override
    public void enter() {
        System.out.println("\tEntering EXAMINE state.");
    }

    @Override
    public void exit() {
        System.out.println("\tLeaving EXAMINE state.");
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void handleInput(float x, float y) {

    }

    @Override
    public void handleInterfaceEvent(int moveType) {

    }
}
