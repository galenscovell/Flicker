package galenscovell.processing.states;

import galenscovell.processing.Repository;
import galenscovell.things.entities.*;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.ui.screens.GameScreen;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

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
        int convertX = (int) (x / Constants.TILESIZE);
        int convertY = (int) (y / Constants.TILESIZE);
        Entity entity = repo.findEntity(convertX, convertY);
        if (entity == null) {
            Inanimate inanimate = repo.findInanimate(convertX, convertY);
            if (inanimate == null) {
                Tile tile = repo.findTile(convertX, convertY);
                if (tile != null) {
                    System.out.println("Examine: " + tile);
                }
            } else {
                System.out.println("Examine: " + inanimate);
            }
        } else {
            System.out.println("Examine: " + entity);
        }
    }

    @Override
    public void handleInterfaceEvent(int moveType) {

    }
}
