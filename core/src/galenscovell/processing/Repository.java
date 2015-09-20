package galenscovell.processing;

import galenscovell.things.entities.Entity;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Repository {
    public static Map<Integer, Tile> tiles;
    public static List<Entity> entities;
    public static List<Inanimate> inanimates;

    public static void fill(Map<Integer, Tile> tiles, List<Entity> entities, List<Inanimate> inanimates) {
        Repository.tiles = tiles;
        Repository.entities = entities;
        Repository.inanimates = inanimates;
    }

    public static Entity findEntity(int x, int y) {
        Entity target = null;
        for (Entity entity : entities) {
            if ((entity.getX() / Constants.MAPSIZE) == x && (entity.getY() / Constants.MAPSIZE) == y) {
                target = entity;
            }
        }
        return target;
    }

    public static Inanimate findInanimate(int x, int y) {
        Inanimate inanimate = null;
        for (Inanimate object : inanimates) {
            if (object.getX() == x && object.getY() == y) {
                inanimate = object;
            }
        }
        return inanimate;
    }

    public static Tile findTile(int x, int y) {
        return tiles.get(x * Constants.MAPSIZE + y);
    }

    public static Tile findRandomTile() {
        Random random = new Random();
        while (true) {
            int choiceY = random.nextInt(Constants.MAPSIZE);
            int choiceX = random.nextInt(Constants.MAPSIZE);
            Tile tile = tiles.get(choiceX * Constants.MAPSIZE + choiceY);
            if (tile != null && tile.isFloor()) {
                if (tile.isOccupied()) {
                    continue;
                }
                return tile;
            }
        }
    }
}
