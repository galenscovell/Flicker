package galenscovell.logic;

import galenscovell.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * LEVEL
 * Level is composed of a HashMap with Tile object values and (x * level_columns + y) keys.
 * Optimization is an expensive procedure which should only occur when creating levels.
 *
 * @author Galen Scovell
 */

public class Level {
    private DungeonBuilder builder;
    private Map<Integer, Tile> tiles;

    public Level() {
        this.builder = new DungeonBuilder();
        this.tiles = builder.getTiles();
    }

    public Map<Integer, Tile> getTiles() {
        return tiles;
    }

    public void optimize() {
        // Make sure all final wall tiles are set as blocking
        for (Tile tile : tiles.values()) {
            if (tile.isWall()) {
                tile.toggleBlocking();
            }
        }
        System.out.println("Tiles before prune: " + tiles.size());
        prune();
        System.out.println("Tiles after prune: " + tiles.size());
        placeWater();
        setFloorNeighbors();
        skin();
    }

    private void prune() {
        // Remove unused Tiles
        List<Integer> pruned = new ArrayList<Integer>();
        for (Map.Entry<Integer, Tile> entry : tiles.entrySet()) {
            if (entry.getValue().isUnused()) {
                pruned.add(entry.getKey());
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
            waterTile.state = 3;
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
                neighborTile.state = 3;
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
            Tile tile = tiles.get(choiceX * Constants.MAPSIZE + choiceY);
            if (tile != null && tile.isFloor() && !tile.isOccupied()) {
                return tile;
            }
        }
    }

    public void print() {
        builder.print();
    }
}