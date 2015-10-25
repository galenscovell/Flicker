package galenscovell.processing.states;

import galenscovell.processing.Repository;
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
        System.out.println("\tEntering ACTION state.");
    }

    public void exit() {
        System.out.println("\tLeaving ACTION state.");
    }

    public void update(float delta) {
        if (repo.eventsEmpty()) {
            return;
        } else {
            Stack<Event> finishedEvents = new Stack<Event>();
            Event heroEvent = repo.nextEvent();  // Hero event is always first in event list
            if (heroEvent.step()) {
                npcTurn();
                // Iterate through each event and step one act forward
                for (Event event : repo.getEvents()) {
                    // Skip first event (Hero event has already acted)
                    if (event == heroEvent) {
                        continue;
                    }
                    // If an event is unable to act, resolve it and add it to finished events
                    if (!event.step()) {
                        event.finish();
                        finishedEvents.push(event);
                    }
                }
                // Remove all finished events from event list
                while (!finishedEvents.isEmpty()) {
                    repo.removeEvent(finishedEvents.pop());
                }
            } else {
                // Once heroEvent is unable to act, resolve it and remove it from event list
                heroEvent.finish();
                repo.removeEvent(heroEvent);
            }
        }
    }

    public void handleInput(float x, float y) {
        int convertX = (int) (x / Constants.TILESIZE);
        int convertY = (int) (y / Constants.TILESIZE);
        Event newEvent = new Event(hero, repo.findTile(convertX, convertY), new Move(repo));
        if (newEvent.start()) {
            repo.addEvent(newEvent);
        }
    }

    public void handleInterfaceEvent(String event) {

    }

    private void npcTurn() {
        for (Entity entity : repo.entities) {
            Event npcEvent = null;
            if (entity.isAggressive()) {
                // TODO: Aggressive behavior depending on entity
                Tile targetTile = repo.findTile(hero.getX() / Constants.TILESIZE, hero.getY() / Constants.TILESIZE);
                npcEvent = new Event(entity, targetTile, new Move(repo));
            } else {
                // TODO: Passive behavior depending on entity
                Tile targetTile = repo.findTile(hero.getX() / Constants.TILESIZE, hero.getY() / Constants.TILESIZE);
                npcEvent = new Event(entity, targetTile, new Move(repo));
            }
            if (npcEvent != null && npcEvent.start()) {
                repo.addEvent(npcEvent);
            }
        }
    }
}
