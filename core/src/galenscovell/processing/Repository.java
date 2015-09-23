package galenscovell.processing;

import galenscovell.things.entities.Entity;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Repository {
    public Map<Integer, Tile> tiles;
    public List<Entity> entities;
    public List<Inanimate> inanimates;

    public Repository(Map<Integer, Tile> tiles, List<Entity> entities, List<Inanimate> inanimates) {
        this.tiles = tiles;
        this.entities = entities;
        this.inanimates = inanimates;
    }

    public Entity findEntity(int x, int y) {
        Entity target = null;
        for (Entity entity : entities) {
            if ((entity.getX() / Constants.MAPSIZE) == x && (entity.getY() / Constants.MAPSIZE) == y) {
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
