package galenscovell.processing;

import galenscovell.processing.actions.Action;
import galenscovell.things.entities.Entity;
import galenscovell.things.inanimates.*;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Repository {
    private final Map<Integer, Tile> tiles;
    private final RayCaster rayCaster;
    private final List<Action> actions;
    private List<Entity> entities;
    private List<Inanimate> inanimates;

    public Repository(Map<Integer, Tile> tiles) {
        this.tiles = tiles;
        this.actions = new ArrayList<Action>();
        this.rayCaster = new RayCaster(tiles);
    }


    /***************************************************
     * Entities and Inanimates
     */
    public void setActors(List<Entity> entities, List<Inanimate> inanimates) {
        this.entities = entities;
        this.inanimates = inanimates;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Inanimate> getInanimates() {
        return inanimates;
    }

    public Entity findEntity(int x, int y) {
        Entity found = null;
        for (Entity entity : entities) {
            if ((entity.getX() / Constants.TILESIZE) == x && (entity.getY() / Constants.TILESIZE) == y) {
                found = entity;
            }
        }
        return found;
    }

    public Inanimate findInanimate(int x, int y) {
        Inanimate found = null;
        for (Inanimate inanimate : inanimates) {
            if (inanimate.getX() == x && inanimate.getY() == y) {
                found = inanimate;
            }
        }
        return found;
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


    /***************************************************
     * Actions
     */
    public List<Action> getActions() {
        return actions;
    }

    public Action getFirstAction() {
        return actions.get(0);
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
        actions.clear();
    }

    public void resolveActions() {
        for (Action action : actions) {
            action.resolve();
        }
    }


    /***************************************************
     * Tiles
     */
    public Map<Integer, Tile> getTiles() {
        return tiles;
    }

    public Tile findTile(int x, int y) {
        return tiles.get(x * Constants.MAPSIZE + y);
    }


    /***************************************************
     * Raycaster
     */
    public void updateResistanceMap() {
        rayCaster.updateResistanceMap();
    }

    public RayCaster getRayCaster() {
        return rayCaster;
    }
}
