package galenscovell.flicker.processing.states;

import com.badlogic.gdx.scenes.scene2d.Actor;
import galenscovell.flicker.processing.Repository;
import galenscovell.flicker.processing.actions.Action;
import galenscovell.flicker.processing.actions.Skills.*;
import galenscovell.flicker.things.entities.Hero;
import galenscovell.flicker.ui.components.*;
import galenscovell.flicker.ui.screens.GameScreen;
import galenscovell.flicker.util.Constants;
import galenscovell.flicker.world.Tile;

public class MenuState implements State {
    private final GameScreen gameScreen;
    private final Hero hero;
    private final Repository repo;

    public MenuState(GameScreen gameScreen, Hero hero, Repository repo) {
        this.gameScreen = gameScreen;
        this.hero = hero;
        this.repo = repo;
    }

    @Override
    public StateType getStateType() {
        return StateType.MENU;
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {
        clearStageSkillMenu();
        clearSkillInfo();
        if (!repo.actionsEmpty()) {
            Action skillAction = repo.getFirstAction();
            if (skillAction.getTarget() == null) {
                repo.getFirstAction().exit();
                repo.clearActions();
            }
        }
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
            if (heroSkillAction.setTarget(target)) {
                gameScreen.changeState(StateType.ACTION);
            }
        }
    }

    @Override
    public void handleInterfaceEvent(int moveType) {
        if (!repo.actionsEmpty()) {
            clearSkillInfo();
            repo.getFirstAction().exit();
            repo.clearActions();
        }
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
                clearSkillInfo();
                repo.getFirstAction().exit();
                repo.clearActions();
            }
            displaySkillInfo(newAction.getInfo());
            repo.addAction(newAction);
        }
    }

    private void displaySkillInfo(String[] info) {
        SkillInfo infoBox = new SkillInfo(info);
        gameScreen.getStage().addActor(infoBox);
    }

    private void clearSkillInfo() {
        Actor skillInfo = gameScreen.getStage().getRoot().findActor("skillInfo");
        if (skillInfo != null) {
            skillInfo.remove();
        }
    }

    private void clearStageSkillMenu() {
        SkillMenu skillMenu = gameScreen.getStage().getRoot().findActor("skillMenu");
        if (skillMenu != null) {
            skillMenu.remove();
            skillMenu.clearSkills(-1);
        }
    }
}
