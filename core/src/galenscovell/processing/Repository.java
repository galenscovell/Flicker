package galenscovell.processing;

import galenscovell.processing.actions.Event;
import galenscovell.things.entities.Entity;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Repository {
    public Map<Integer, Tile> tiles;
    public List<Entity> entities;
    public List<Inanimate> inanimates;
    public List<Event> events;

    public Repository(Map<Integer, Tile> tiles) {
        this.tiles = tiles;
        this.events = new ArrayList<Event>();
    }

    public void addActors(List<Entity> entities, List<Inanimate> inanimates) {
        this.entities = entities;
        this.inanimates = inanimates;
    }

    public boolean eventsEmpty() {
        return events.isEmpty();
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }

    public void clearEvents() {
        resolveEvents();
        this.events = new ArrayList<Event>();
    }

    public void resolveEvents() {
        for (Event event : events) {
            event.finish();
        }
    }

    public List<Event> getEvents() {
        return this.events;
    }

    public Event getFirstEvent() {
        return events.get(0);
    }

    public Entity findEntity(int x, int y) {
        Entity target = null;
        for (Entity entity : entities) {
            if ((entity.getX() / Constants.TILESIZE) == x && (entity.getY() / Constants.TILESIZE) == y) {
                target = entity;
            }
        }
        return target;
    }

    public Inanimate findInanimate(int x, int y) {
        Inanimate inanimate = null;
        for (Inanimate object : inanimates) {
            if (object.getX() == x && object.getY() == y) {
                inanimate = object;
            }
        }
        return inanimate;
    }

    public Tile findTile(int x, int y) {
        return tiles.get(x * Constants.MAPSIZE + y);
    }

    public Tile findRandomTile() {
        Random random = new Random();
        while (true) {
            int choiceY = random.nextInt(Constants.MAPSIZE);
            int choiceX = random.nextInt(Constants.MAPSIZE);
            Tile tile = findTile(choiceX, choiceY);
            if (tile != null && tile.isFloor()) {
                if (tile.isOccupied()) {
                    continue;
                }
                return tile;
            }
        }
    }
}
