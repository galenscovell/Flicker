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
    private int columns, rows;
    private DungeonBuilder builder;
    private Map<Integer, Tile> tiles;

    public Level() {
        this.rows = Constants.ROWS;
        this.columns = Constants.COLUMNS;
        this.builder = new DungeonBuilder(columns, rows);
        this.tiles = builder.getTiles();
    }

    public Map<Integer, Tile> getTiles() {
        return tiles;
    }

    public void optimize() {
        // Remove walls without floor neighbors, also switch over any remaining corridor tiles
        for (Tile tile : tiles.values()) {
            if (tile.isWall()) {
                if (tile.getFloorNeighbors() == 0) {
                    tile.setUnused();
                }
            } else if (tile.isCorridor()) {
                tile.state = 1;
            }
        }
        // Check if floor tiles are adjacent to pruned tiles, if so make them wall
        for (Tile tile : tiles.values()) {
            if (tile.isFloor()) {
                for (Point neighbor : tile.getNeighbors()) {
                    if (tiles.get(neighbor.x * columns + neighbor.y).isUnused()) {
                        tile.state = 0;
                    }
                }
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
        prune();
        placeWater();
        skin();
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
            Tile neighborTile = tiles.get(point.x * columns + point.y);
            if (neighborTile != null && neighborTile.isFloor()) {
                neighborTile.state = 3;
                waterTiles.add(neighborTile);
            }
        }
    }

    private void skin() {
        Bitmasker bitmasker = new Bitmasker();
        for (Tile tile : tiles.values()) {
            tile.setBitmask(bitmasker.findBitmask(tile, tiles, columns));
            tile.findSprite();
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