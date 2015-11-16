package galenscovell.processing.states;

import galenscovell.processing.Repository;
import galenscovell.processing.actions.*;
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
        System.out.println("\tEntering MENU state");
    }

    @Override
    public void exit() {
        repo.resolveEvents();
        System.out.println("\tLeaving MENU state\n");
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void handleInput(float x, float y) {
        if (!repo.eventsEmpty()) {
            Event heroEvent = repo.getFirstEvent();  // Hero event is always first in event list
            int convertX = (int) (x / Constants.TILESIZE);
            int convertY = (int) (y / Constants.TILESIZE);
            Tile target = repo.findTile(convertX, convertY);
            heroEvent.setTarget(target);
            root.changeState(StateType.ACTION);
        }
    }

    @Override
    public void handleInterfaceEvent(int moveType) {
        Skill skill = new Skill(repo);
        skill.define(moveType);
        Event newEvent = new Event(hero, null, skill);
        if (newEvent.start()) {
            // If event currently being processed, replace old user event with new event
            if (!repo.eventsEmpty()) {
                repo.clearEvents();
            }
            repo.addEvent(newEvent);
        }
    }
}
