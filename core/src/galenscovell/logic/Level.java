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
        // If Tile is a wall with neighboring floor, it becomes a perimeter Tile
        // Also switch over any remaining corridor Tiles
        for (Tile tile : tiles.values()) {
            if (tile.isWall()) {
                if (tile.getFloorNeighbors() > 0) {
                    tile.state = 3;
                }
            } else if (tile.isCorridor()) {
                tile.state = 1;
            }
        }
        // Set perimeter Tiles not actually on perimeter to be floor Tiles
        int wallNeighbors;
        for (Tile tile : tiles.values()) {
            if (tile.isPerimeter()) {
                wallNeighbors = 0;
                for (Point neighbor : tile.getNeighbors()) {
                    if (tiles.get(neighbor.x * columns + neighbor.y).isWall()) {
                        wallNeighbors++;
                    }
                }
                if (wallNeighbors == 0) {
                    tile.state = 1;
                }
            }
        }
        // Set floor Tiles on world boundary or with neighboring walls as perimeter
        for (Tile tile : tiles.values()) {
            if (tile.isFloor()) {
                wallNeighbors = 0;
                for (Point neighbor : tile.getNeighbors()) {
                    if (tiles.get(neighbor.x * columns + neighbor.y).isWall()) {
                        wallNeighbors++;
                    }
                }
                if (wallNeighbors > 0 || tile.getNeighbors().size() < 8) {
                    tile.state = 3;
                }
            }
        }
        // Recheck Tiles for floor neighbors, if floor exists without any adjacent
        // floor Tiles remove it. If Tile has only one adjacent floor make it perimeter.
        checkAdjacent();
        for (Tile tile : tiles.values()) {
            if (tile.getFloorNeighbors() == 1) {
                tile.state = 3;
            }
        }
        skin();
        prune();
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
            waterTile.state = 4;
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
            if (neighborTile.isFloor()) {
                neighborTile.state = 4;
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