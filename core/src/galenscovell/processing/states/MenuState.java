package galenscovell.processing.states;

import galenscovell.processing.Repository;
import galenscovell.processing.actions.*;
import galenscovell.processing.actions.Skills.*;
import galenscovell.things.entities.Hero;
import galenscovell.ui.screens.GameScreen;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

public class MenuState implements State {
    private final GameScreen root;
    private final Hero hero;
    private final Repository repo;

    public MenuState(GameScreen root, Hero hero, Repository repo) {
        this.root = root;
        this.hero = hero;
        this.repo = repo;
    }

    @Override
    public StateType getStateType() {
        return StateType.MENU;
    }

    @Override
    public void enter() {
        // System.out.println("\tEntering MENU state");
    }

    @Override
    public void exit() {
        root.clearStageSkillMenu();
        // System.out.println("\tLeaving MENU state\n");
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void handleInput(float x, float y) {
        if (!repo.actionsEmpty()) {
            Action heroSkillAction = repo.getFirstAction();  // Hero action is always first in action list
            int convertX = (int) (x / Constants.TILESIZE);
            int convertY = (int) (y / Constants.TILESIZE);
            Tile target = repo.findTile(convertX, convertY);
            heroSkillAction.setTarget(target);
            root.changeState(StateType.ACTION);
        }
    }

    @Override
    public void handleInterfaceEvent(int moveType) {
        Action newAction;
        if (moveType == Constants.LUNGE_TYPE) {
            newAction = new Lunge(hero, repo);
        } else if (moveType == Constants.ROLL_TYPE) {
            newAction = new Roll(hero, repo);
        } else if (moveType == Constants.BASH_TYPE) {
            newAction = new Bash(hero, repo);
        } else {
            newAction = new Leap(hero, repo);
        }
        if (newAction.initialize()) {
            // If action currently being processed, replace old user action with new
            if (!repo.actionsEmpty()) {
                repo.clearActions();
            }
            repo.addAction(newAction);
        }
    }
}
