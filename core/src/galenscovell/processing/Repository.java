package galenscovell.processing;

import galenscovell.processing.actions.Action;
import galenscovell.things.entities.Entity;
import galenscovell.things.inanimates.*;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Repository {
    public final Map<Integer, Tile> tiles;
    public final RayCaster rayCaster;
    public List<Action> actions;
    public List<Entity> entities;
    public List<Inanimate> inanimates;

    public Repository(Map<Integer, Tile> tiles) {
        this.tiles = tiles;
        this.actions = new ArrayList<Action>();
        this.rayCaster = new RayCaster(tiles);
    }

    public void updateResistanceMap() {
        rayCaster.updateResistanceMap();
    }

    public void addActors(List<Entity> entities, List<Inanimate> inanimates) {
        this.entities = entities;
        this.inanimates = inanimates;
    }

    public boolean actionsEmpty() {
        return actions.isEmpty();
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void removeAction(Action action) {
        actions.remove(action);
    }

    public void clearActions() {
        resolveActions();
        this.actions = new ArrayList<Action>();
    }

    public void resolveActions() {
        for (Action action : actions) {
            action.resolve();
        }
    }

    public List<Action> getActions() {
        return this.actions;
    }

    public Action getFirstAction() {
        return actions.get(0);
    }

    public void placeRemains(Entity entity) {
        int entityX = entity.getX() / Constants.TILESIZE;
        int entityY = entity.getY() / Constants.TILESIZE;
        Tile entityTile = findTile(entityX, entityY);
        entityTile.toggleOccupied();
        Action removedAction = null;
        for (Action action : getActions()) {
            if (action.getUser() == entity) {
                removedAction = action;
            }
        }
        if (removedAction != null) {
            actions.remove(removedAction);
        }
        entities.remove(entity);
        Dead remains = new Dead(entityX, entityY);
        inanimates.add(remains);
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
