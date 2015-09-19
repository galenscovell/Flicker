package galenscovell.processing;

import galenscovell.things.entities.Entity;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Repository {
    private Map<Integer, Tile> tiles;
    private List<Entity> entities;
    private List<Inanimate> inanimates;

    public Repository(Map<Integer, Tile> ti, List<Entity> en, List<Inanimate> in) {
        this.tiles = ti;
        this.entities = en;
        this.inanimates = in;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Inanimate> getInanimates() {
        return inanimates;
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

    public Entity findEntity(int x, int y) {
        Entity target = null;
        for (Entity entity : entities) {
            if ((entity.getX() / tileSize) == x && (entity.getY() / tileSize) == y) {
                target = entity;
            }
        }
        return target;
    }

    public Tile findTile(int x, int y) {
        return tiles.get(x * Constants.MAPSIZE + y);
    }
}
