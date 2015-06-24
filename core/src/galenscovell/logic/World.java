package galenscovell.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * WORLD
 * World is composed of a 2D array grid and a list of matching Tile instances.
 *
 * @author Galen Scovell
 */

public class World {
    private int columns, rows;
    private Builder builder;
    private Map<Integer, Tile> tiles;
    private Bitmasker bitmasker;

    public World() {
        this.columns = 60;
        this.rows = 60;
        this.builder = new DungeonBuilder(columns, rows);
        builder.build();
        this.tiles = builder.getTiles();
        this.bitmasker = new Bitmasker();
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

    public void optimizeLayout() {
        List<Integer> pruned = new ArrayList<Integer>();
        // If Tile is a wall connected to a floor Tile, it becomes a perimeter Tile
        for (Map.Entry<Integer, Tile> entry : tiles.entrySet()) {
            Tile tile = entry.getValue();
            if (tile.isWall()) {
                if (tile.getFloorNeighbors() > 0) {
                    tile.state = 3;
                    // Otherwise add Tile to remove list
                } else {
                    pruned.add(entry.getKey());
                }
                // Switch over lingering corridor tiles
            } else if (tile.isCorridor()) {
                tile.state = 1;
            }
        }
        // Set perimeter Tiles not on perimeter to be floor Tiles
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
        // If Tile is on world boundary or has adjacent non-perimeter wall, make it perimeter
        int key;
        for (Tile tile : tiles.values()) {
            if (tile.isFloor()) {
                wallNeighbors = 0;
                for (Point neighbor : tile.getNeighbors()) {
                    key = neighbor.x * columns + neighbor.y;
                    if (tiles.get(key).isWall()) {
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
        for (Map.Entry<Integer, Tile> entry : tiles.entrySet()) {
            Tile tile = entry.getValue();
            if (tile.getFloorNeighbors() == 1) {
                tile.state = 3;
            } else if (tile.getFloorNeighbors() == 0) {
                pruned.add(entry.getKey());
            }
        }
        // Remove pruned Tiles from Tiles list
        for (int removeKey : pruned) {
            tiles.remove(removeKey);
        }
        skin();
        placeWater();
    }

    private void skin() {
        for (Tile tile : tiles.values()) {
            tile.setBitmask(bitmasker.findBitmask(tile, tiles, columns));
            tile.findSprite();
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
        int key;
        for (Tile tile : tiles.values()) {
            int value = 0;
            List<Point> neighborPoints = tile.getNeighbors();
            for (Point point : neighborPoints) {
                key = point.x * columns + point.y;
                if (tiles.get(key).isFloor()) {
                    value++;
                }
            }
            tile.setFloorNeighbors(value);
        }
    }

    private Tile findRandomTile() {
        Random random = new Random();
        boolean found = false;
        while (!found) {
            int choiceX = random.nextInt(columns);
            int choiceY = random.nextInt(rows);
            if (tiles.containsKey(choiceX * columns + choiceY)) {
                Tile tile = tiles.get(choiceX * columns + choiceY);
                if (tile.isFloor()) {
                    found = true;
                    return tile;
                }
            }
        }
        return null;
    }
}