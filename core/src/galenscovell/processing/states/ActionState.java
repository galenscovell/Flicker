package galenscovell.processing.states;

import galenscovell.processing.*;
import galenscovell.processing.actions.*;
import galenscovell.things.entities.*;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.Stack;

public class ActionState implements State {
    private Hero hero;
    private Repository repo;

    public ActionState(Hero hero, Repository repo) {
        this.hero = hero;
        this.repo = repo;
    }

    public void enter() {
        System.out.println("Entering movement state.");
    }

    public void exit() {
        System.out.println("Leaving movement state.");
    }

    public void update(float delta) {
        if (repo.eventsEmpty()) {
            return;
        } else {
            Stack<Event> finishedEvents = new Stack<Event>();
            Event heroEvent = repo.nextEvent();
            if (heroEvent.act()) {
                npcTurn();
                for (Event event : repo.getEvents()) {
                    if (!event.act()) {
                        event.resolve();
                        finishedEvents.push(event);
                    }
                }
                while (!finishedEvents.isEmpty()) {
                    repo.removeEvent(finishedEvents.pop());
                }
            } else {
                heroEvent.resolve();
                repo.removeEvent(heroEvent);
            }
        }
    }

    public void handleInput(float x, float y) {
        int convertX = (int) (x / Constants.TILESIZE);
        int convertY = (int) (y / Constants.TILESIZE);
        Event newEvent = new Event(hero, repo.findTile(convertX, convertY), new Move(repo));
        if (newEvent.initialized()) {
            repo.addEvent(newEvent);
        }
    }

    public void handleInterfaceEvent(String event) {

    }

    private void npcTurn() {
        for (Entity entity : repo.entities) {
            Event npcEvent = null;
            if (entity.isAggressive()) {
                Tile targetTile = repo.findTile(hero.getX() / Constants.TILESIZE, hero.getY() / Constants.TILESIZE);
                npcEvent = new Event(entity, targetTile, new Move(repo));
            } else {
                Tile targetTile = repo.findTile(hero.getX() / Constants.TILESIZE, hero.getY() / Constants.TILESIZE);
                npcEvent = new Event(entity, targetTile, new Move(repo));
                // TODO: Passive behavior, destination depends on entity
            }
            if (npcEvent != null && npcEvent.initialized()) {
                repo.addEvent(npcEvent);
            }
        }
    }
}
