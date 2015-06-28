package galenscovell.logic;

import galenscovell.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * LEVEL
 * Level is composed of a HashMap with Tile object values and (x * level_columns + y) keys.
 * Optimization is an intensive procedure which should only occur when creating levels.
 *
 * @author Galen Scovell
 */

public class Level {
    private int columns, rows;
    private DungeonBuilder builder;
    private Map<Integer, Tile> tiles;

    public Level() {
        this.rows = Constants.ROWS;
        this.columns = Constants.COLUMNS;
        this.builder = new DungeonBuilder(columns, rows);
        this.tiles = builder.getTiles();
    }

    public void update() {
        checkAdjacent();
        for (Tile tile : tiles.values()) {
            builder.smooth(tile);
        }
    }

    public Map<Integer, Tile> getTiles() {
        return tiles;
    }

    public void optimize() {
        // Remove walls without floor neighbors, also switch over any remaining corridor Tiles
        for (Tile tile : tiles.values()) {
            if (tile.isWall()) {
                if (tile.getFloorNeighbors() == 0) {
                    tile.setUnused();
                }
            } else if (tile.isCorridor()) {
                tile.state = 1;
            }
        }
        // If wall has one or zero connecting walls, make it floor
        // If floor is on level boundary, make it wall
        int wallNeighbors;
        for (Tile tile : tiles.values()) {
            wallNeighbors = 0;
            if (tile.isWall()) {
                for (Point neighbor : tile.getNeighbors()) {
                    if (tiles.get(neighbor.x * columns + neighbor.y).isWall()) {
                        wallNeighbors++;
                    }
                }
                if (wallNeighbors == 0) {
                    tile.state = 1;
                }
             } else if (tile.isFloor()) {
                if (tile.getNeighbors().size() < 8) {
                    tile.state = 0;
                }
            }
        }
        // Recheck floor neighbors, if floor exists with exactly one floor neighbor, make it wall
        checkAdjacent();
        for (Tile tile : tiles.values()) {
            if (tile.isFloor()) {
                if (tile.getFloorNeighbors() == 1) {
                    tile.state = 0;
                }
            } else if (tile.isWall()) {
                tile.toggleBlocking();
            }
        }
        prune();
        skin();
        placeWater();
    }

    private void skin() {
        Bitmasker bitmasker = new Bitmasker();
        for (Tile tile : tiles.values()) {
            tile.setBitmask(bitmasker.findBitmask(tile, tiles, columns));
            tile.findSprite();
        }
    }

    private void prune() {
        // Remove unused Tiles
        List<Integer> pruned = new ArrayList<Integer>();
        for (Map.Entry<Integer, Tile> entry : tiles.entrySet()) {
            if (entry.getValue().isEmpty()) {
                pruned.add(entry.getKey());
            }
        }
        for (int key : pruned) {
            tiles.remove(key);
        }
    }

    private void placeWater() {
        Random generator = new Random();
        int waterPoints = generator.nextInt(3);
        List<Tile> waterTiles = new ArrayList<Tile>();
        // Place initial water spawn points randomly
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
        for (Tile tile : addedTiles) {
            waterTiles.add(tile);
        }
        // Find water tile bitmasks and apply sprites
        Bitmasker bitmasker = new Bitmasker();
        for (Tile tile : waterTiles) {
            tile.setBitmask(bitmasker.findBitmask(tile, tiles, columns));
            tile.findSprite();
        }
    }

    private void expandWater(Tile tile, List<Tile> waterTiles) {
        List<Point> neighbors = tile.getNeighbors();
        for (Point point : neighbors) {
            Tile neighborTile = tiles.get(point.x * columns + point.y);
            if (neighborTile != null && neighborTile.isFloor()) {
                neighborTile.state = 3;
                waterTiles.add(neighborTile);
            }
        }
    }

    private void checkAdjacent() {
        for (Tile tile : tiles.values()) {
            int value = 0;
            List<Point> neighborPoints = tile.getNeighbors();
            for (Point point : neighborPoints) {
                if (tiles.get(point.x * columns + point.y).isFloor()) {
                    value++;
                }
            }
            tile.setFloorNeighbors(value);
        }
    }

    private Tile findRandomTile() {
        Random random = new Random();
        while (true) {
            int choiceY = random.nextInt(rows);
            int choiceX = random.nextInt(columns);
            Tile tile = tiles.get(choiceX * columns + choiceY);
            if (tile != null && tile.isFloor() && !tile.isOccupied()) {
                return tile;
            }
        }
    }
}