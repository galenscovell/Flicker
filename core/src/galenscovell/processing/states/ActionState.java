package galenscovell.processing.states;

import galenscovell.processing.Repository;
import galenscovell.processing.actions.*;
import galenscovell.things.entities.*;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.ui.screens.GameScreen;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class ActionState implements State {
    private final GameScreen root;
    private final Hero hero;
    private final Repository repo;
    private List<Inanimate> adjacentThings;

    public ActionState(GameScreen root, Hero hero, Repository repo) {
        this.root = root;
        this.hero = hero;
        this.repo = repo;
        this.adjacentThings = new ArrayList<Inanimate>();
    }

    @Override
    public StateType getStateType() {
        return StateType.ACTION;
    }

    @Override
    public void enter() {
        System.out.println("\tEntering ACTION state.");
    }

    @Override
    public void exit() {
        repo.clearEvents();
        System.out.println("\tLeaving ACTION state.");
    }

    @Override
    public void update(float delta) {
        if (repo.eventsEmpty()) {
            return;
        } else {
            if (!adjacentThings.isEmpty()) {
                adjacentThings.clear();
                root.clearInanimateBoxes();
            }
            Stack<Event> finishedEvents = new Stack<Event>();
            Event heroEvent = repo.getFirstEvent();  // Hero event is always first in event list
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
                // Once heroEvent is unable to act, resolve it and clear event list
                heroEvent.finish();
                repo.clearEvents();
                heroAdjacentCheck();
            }
        }
    }

    private void heroAdjacentCheck() {
        int heroTileX = hero.getX() / Constants.TILESIZE;
        int heroTileY = hero.getY() / Constants.TILESIZE;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 || dy == 0) {
                    Inanimate thing = repo.findInanimate(heroTileX + dx, heroTileY + dy);
                    if (thing != null) {
                        adjacentThings.add(thing);
                        Tile thingTile = repo.findTile(heroTileX + dx, heroTileY + dy);
                        root.displayInanimateBox(thing, thingTile);
                    }
                }
            }
        }
    }

    @Override
    public void handleInput(float x, float y) {
        int convertX = (int) (x / Constants.TILESIZE);
        int convertY = (int) (y / Constants.TILESIZE);
        Event newEvent = new Event(hero, repo.findTile(convertX, convertY), new Move(repo));
        if (newEvent.start()) {
            // If event currently being processed, replace old user event with new event
            if (!repo.eventsEmpty()) {
                repo.clearEvents();
            }
            repo.addEvent(newEvent);
        }
    }

    @Override
    public void handleInterfaceEvent(int moveType) {

    }

    private void npcTurn() {
        for (Entity entity : repo.entities) {
            if (entity.movementTimer()) {
                // Check if entity has another event in event list and remove it
                Event previousEvent = null;
                for (Event event : repo.getEvents()) {
                    if (event.getEntity() == entity) {
                        previousEvent = event;
                    }
                }
                if (previousEvent != null) {
                    repo.removeEvent(previousEvent);
                }
                // Create new entity event with entity behaviors
                Event npcEvent;
                if (entity.isAggressive()) {
                    // TODO: Aggressive behavior depending on entity
                    Tile targetTile = repo.findTile(hero.getX() / Constants.TILESIZE, hero.getY() / Constants.TILESIZE);
                    npcEvent = new Event(entity, targetTile, new Move(repo));
                } else {
                    // TODO: Passive behavior depending on entity
                    Tile targetTile = repo.findTile(hero.getX() / Constants.TILESIZE, hero.getY() / Constants.TILESIZE);
                    npcEvent = new Event(entity, targetTile, new Move(repo));
                }
                if (npcEvent.start()) {
                    repo.addEvent(npcEvent);
                }
            }
        }
    }
}
