package galenscovell.world;

import galenscovell.graphics.Lighting;
import galenscovell.processing.Point;
import galenscovell.things.entities.*;
import galenscovell.things.inanimates.*;
import galenscovell.util.*;
import galenscovell.world.generation.*;

import java.util.*;

public class Level {
    private DungeonBuilder builder;
    private Map<Integer, Tile> tiles;
    private List<Entity> entities;
    private List<Inanimate> inanimates;

    public Level() {
        this.builder = new DungeonBuilder();
        this.tiles = builder.getTiles();
        optimize();
        placeWater();
        setFloorNeighbors();
        skin();
    }

    public Map<Integer, Tile> getTiles() {
        return tiles;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Inanimate> getInanimates() {
        return inanimates;
    }

    public void optimize() {
        // Prune unused Tiles and ensure Walls are set to blocking
        List<Integer> pruned = new ArrayList<Integer>();
        for (Map.Entry<Integer, Tile> entry : tiles.entrySet()) {
            if (entry.getValue().isEmpty()) {
                pruned.add(entry.getKey());
            } else if (entry.getValue().isWall()) {
                int wallNeighbors = 0;
                // If wall has no wall neighbors, make it floor
                // If all of its neighbors are also walls, remove it
                for (Point p : entry.getValue().getNeighbors()) {
                    Tile neighbor = getTile(p.x, p.y);
                    if (neighbor.isWall()) {
                        wallNeighbors++;
                    }
                }
                if (wallNeighbors <= 1) {
                    entry.getValue().becomeFloor();
                } else if (wallNeighbors == 8) {
                    pruned.add(entry.getKey());
                } else {
                    entry.getValue().toggleBlocking();
                }
            }
        }
        for (int key : pruned) {
            tiles.remove(key);
        }
    }

    private void placeWater() {
        Random generator = new Random();
        int waterPoints = generator.nextInt(5);
        List<Tile> waterTiles = new ArrayList<Tile>();
        // Place water spawn points randomly
        for (int i = 0; i < waterPoints; i++) {
            Tile waterTile = findRandomTile();
            waterTile.becomeWater();
            waterTiles.add(waterTile);
            // Expand each point out one layer initially
            expandWater(waterTile, waterTiles);
        }
        // Randomly expand water tiles
        List<Tile> addedTiles = new ArrayList<Tile>();
        for (Tile tile : waterTiles) {
            if (generator.nextInt(20) < 10) {
                expandWater(tile, addedTiles);
            }
        }
        // Add expanded water tiles to water tile list
        for (Tile tile : addedTiles) {
            waterTiles.add(tile);
        }
    }

    private void expandWater(Tile tile, List<Tile> waterTiles) {
        List<Point> neighbors = tile.getNeighbors();
        for (Point point : neighbors) {
            Tile neighborTile = tiles.get(point.x * Constants.MAPSIZE + point.y);
            if (neighborTile != null && neighborTile.isFloor()) {
                neighborTile.becomeWater();
                waterTiles.add(neighborTile);
            }
        }
    }

    private void skin() {
        Bitmasker bitmasker = new Bitmasker();
        for (Tile tile : tiles.values()) {
            tile.setBitmask(bitmasker.findBitmask(tile, tiles));
            tile.findSprite();
        }
    }

    private void setFloorNeighbors() {
        for (Tile tile : tiles.values()) {
            int floorNeighbors = 0;
            for (Point neighborPoint : tile.getNeighbors()) {
                Tile neighbor = tiles.get(neighborPoint.x * Constants.MAPSIZE + neighborPoint.y);
                if (neighbor != null && neighbor.isFloor()) {
                    floorNeighbors++;
                }
            }
            tile.setFloorNeighbors(floorNeighbors);
        }
    }

    private Tile findRandomTile() {
        Random random = new Random();
        while (true) {
            int choiceY = random.nextInt(Constants.MAPSIZE);
            int choiceX = random.nextInt(Constants.MAPSIZE);
            Tile tile = getTile(choiceX, choiceY);
            if (tile != null && tile.isFloor() && !tile.isOccupied() && !tile.hasDoor()) {
                return tile;
            }
        }
    }

    private Tile getTile(int x, int y) {
        return tiles.get(x * Constants.MAPSIZE + y);
    }

    public void testPrint() {
        builder.print();
    }

    public void placeEntities(Hero hero) {
        this.entities = new ArrayList<Entity>();
        placePlayer(hero);
        MonsterParser monsterParser = new MonsterParser();
        // TODO: Modify monster placement
        for (int i = 0; i < 3; i++) {
            placeMonsters(monsterParser);
        }
    }

    public void placeInanimates(Lighting lighting) {
        // Place doors on floors with hallway bitmask, no adjacent doors, and more than 2 adjacent floor neighbors
        this.inanimates = new ArrayList<Inanimate>();
        for (Tile tile : tiles.values()) {
            if (tile.isFloor() && tile.getFloorNeighbors() > 2) {
                if (tile.getBitmask() == 5 && suitableForDoor(tile)) {
                    inanimates.add(new Door(tile.x, tile.y, "h", lighting));
                    tile.toggleBlocking();
                    tile.toggleOccupied();
                    tile.toggleDoor();
                } else if (tile.getBitmask() == 10 && suitableForDoor(tile)) {
                    inanimates.add(new Door(tile.x, tile.y, "v", lighting));
                    tile.toggleBlocking();
                    tile.toggleOccupied();
                    tile.toggleDoor();
                }
            }
        }
    }

    private boolean suitableForDoor(Tile tile) {
        for (Point p : tile.getNeighbors()) {
            Tile n = tiles.get(p.x * Constants.MAPSIZE + p.y);
            if (n != null && (n.isWater() || n.hasDoor())) {
                return false;
            }
        }
        return true;
    }

    private void placePlayer(Hero hero) {
        Tile randomTile = findRandomTile();
        hero.setPosition(randomTile.x * Constants.TILESIZE, randomTile.y * Constants.TILESIZE);
        randomTile.toggleOccupied();
    }

    private void placeMonsters(MonsterParser parser) {
        Tile randomTile = findRandomTile();
        Entity monster = parser.spawn(2);
        monster.setPosition(randomTile.x * Constants.TILESIZE, randomTile.y * Constants.TILESIZE);
        entities.add(monster);
        randomTile.toggleOccupied();
    }
}