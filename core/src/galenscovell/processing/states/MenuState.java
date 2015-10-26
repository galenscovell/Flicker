package galenscovell.processing.states;

import galenscovell.processing.Repository;
import galenscovell.processing.actions.*;
import galenscovell.things.entities.Hero;
import galenscovell.ui.screens.GameScreen;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

public class MenuState implements State {
    private GameScreen root;
    private Hero hero;
    private Repository repo;

    public MenuState(GameScreen root, Hero hero, Repository repo) {
        this.root = root;
        this.hero = hero;
        this.repo = repo;
    }

    public void enter() {
        System.out.println("\tEntering MENU state.");
    }

    public void exit() {
        System.out.println("\tLeaving MENU state.");
    }

    public void update(float delta) {

    }

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

    public void handleInterfaceEvent(String definition) {
        Skill skill = new Skill(repo);
        skill.define(definition);
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
