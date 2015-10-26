package galenscovell.processing.states;

import galenscovell.processing.Repository;
import galenscovell.processing.actions.*;
import galenscovell.things.entities.Hero;

public class MenuState implements State {
    private Hero hero;
    private Repository repo;

    public MenuState(Hero hero, Repository repo) {
        this.hero = hero;
        this.repo = repo;
    }

    public void enter() {
        System.out.println("\tEntering MENU state.");
    }

    public void exit() {
        repo.clearEvents();
        System.out.println("\tLeaving MENU state.");
    }

    public void update(float delta) {

    }

    public void handleInput(float x, float y) {

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
