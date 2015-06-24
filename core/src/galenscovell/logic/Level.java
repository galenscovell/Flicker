package galenscovell.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * WORLD
 * World is composed of a 2D array grid and a list of matching Tile instances.
 *
 * @author Galen Scovell
 */

public class Level {
    private int columns, rows;
    private DungeonBuilder builder;
    private Tile[][] tiles;

    public Level(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.builder = new DungeonBuilder(columns, rows);
        this.tiles = builder.getTiles();
    }

    public void update() {
        checkAdjacent();
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                builder.smooth(tile);
            }
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void optimizeLayout() {
        // If Tile is a wall connected to a floor Tile, it becomes a perimeter Tile
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.isWall()) {
                    if (tile.getFloorNeighbors() > 0) {
                        tile.state = 3;
                    } else {
                        // Otherwise remove Tile
                        tile = null;
                    }
                } else if (tile.isCorridor()) {
                    // Switch over lingering corridor tiles
                    tile.state = 1;
                }
            }
        }
        // Set perimeter Tiles not on perimeter to be floor Tiles
        int wallNeighbors;
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.isPerimeter()) {
                    wallNeighbors = 0;
                    for (Point neighbor : tile.getNeighbors()) {
                        if (tiles[neighbor.y][neighbor.x].isWall()) {
                            wallNeighbors++;
                        }
                    }
                    if (wallNeighbors == 0) {
                        tile.state = 1;
                    }
                }
            }
        }
        // If Tile is on world boundary or has adjacent non-perimeter wall, make it perimeter
        int key;
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.isFloor()) {
                    wallNeighbors = 0;
                    for (Point neighbor : tile.getNeighbors()) {
                        if (tiles[neighbor.y][neighbor.x].isWall()) {
                            wallNeighbors++;
                        }
                    }
                    if (wallNeighbors > 0 || tile.getNeighbors().size() < 8) {
                        tile.state = 3;
                    }
                }
            }
        }
        // Recheck Tiles for floor neighbors, if floor exists without any adjacent
        // floor Tiles remove it. If Tile has only one adjacent floor make it perimeter.
        checkAdjacent();
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.getFloorNeighbors() == 1) {
                    tile.state = 3;
                } else if (tile.getFloorNeighbors() == 0) {
                    tile = null;
                }
            }
        }
        skin();
        placeWater();
    }

    private void skin() {
        Bitmasker bitmasker = new Bitmasker();
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                tile.setBitmask(bitmasker.findBitmask(tile, tiles));
                tile.findSprite();
            }
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
            tile.setBitmask(bitmasker.findBitmask(tile, tiles));
            tile.findSprite();
        }
    }

    private void expandWater(Tile tile, List<Tile> waterTiles) {
        List<Point> neighbors = tile.getNeighbors();
        for (Point point : neighbors) {
            Tile neighborTile = tiles[point.y][point.x];
            if (neighborTile.isFloor()) {
                neighborTile.state = 4;
                waterTiles.add(neighborTile);
            }
        }
    }

    private void checkAdjacent() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                int value = 0;
                List<Point> neighborPoints = tile.getNeighbors();
                for (Point point : neighborPoints) {
                    if (tiles[point.y][point.x].isFloor()) {
                        value++;
                    }
                }
                tile.setFloorNeighbors(value);
            }
        }
    }

    private Tile findRandomTile() {
        Random random = new Random();
        boolean found = false;
        while (!found) {
            int choiceY = random.nextInt(rows);
            int choiceX = random.nextInt(columns);
            if (tiles[choiceY][choiceX] != null) {
                Tile tile = tiles[choiceY][choiceX];
                if (tile.isFloor()) {
                    found = true;
                    return tile;
                }
            }
        }
        return null;
    }
}