package galenscovell.logic;

import java.util.*;

/**
 * DUNGEON BUILDER
 * Constructs a new world tileset with dungeon features.
 * (Rectangular rooms connected by corridors)
 *
 * @author Galen Scovell
 */

public class DungeonBuilder {
    private int rows, columns;
    private Tile[][] grid;
    private List<Partition> rooms;

    public DungeonBuilder(int columns, int rows) {
        this.rows = rows;
        this.columns = columns;
        this.grid = new Tile[rows][columns];
        this.rooms = new ArrayList<Partition>();
        build();
        // Change number of rooms here
        partition(11);
        setTileNeighbors();
    }

    public Map<Integer, Tile> getTiles() {
        // Translate Tile[][] grid to HashMap
        Map<Integer, Tile> tiles = new HashMap<Integer, Tile>();
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                int key = x * columns + y;
                tiles.put(key, grid[y][x]);
            }
        }
        return tiles;
    }

    private void build() {
        // Construct Tile[rows][columns] grid of all walls
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                grid[y][x] = new Tile(x, y);
            }
        }
    }

    private void partition(int splits) {
        // Binary Space Partitioning
        Random random = new Random();
        List<Partition> partitions = new ArrayList<Partition>();
        Partition root = new Partition(0, 0, columns, rows);
        partitions.add(root);
        while (splits > 0) {
            int chosenPartition = random.nextInt(partitions.size());
            Partition toSplit = partitions.get(chosenPartition);
            if (toSplit.split()) {
                partitions.add(toSplit.leftChild);
                partitions.add(toSplit.rightChild);
                splits--;
            }
        }
        generateInterior(root);
    }

    private void generateInterior(Partition p) {
        // Create interior within lowest children
        if (p.leftChild == null) {
            rooms.add(p);
            createRoom(p);
        } else {
            generateInterior(p.leftChild);
            generateInterior(p.rightChild);
        }
    }

    private void createRoom(Partition p) {
        // Create floor tiled room of random size within partition
        List<Tile> interiorTiles = new ArrayList<Tile>();
        Random random = new Random();
        int interiorX = random.nextInt(2) + p.x;
        int interiorY = random.nextInt(2) + p.y;
        int interiorWidth = p.width - random.nextInt(2) - 2;
        int interiorHeight = p.height - random.nextInt(2) - 2;
        if (interiorWidth < 7) {
            interiorWidth = 7;
        }
        if (interiorHeight < 7) {
            interiorHeight = 7;
        }
        for (int x = interiorX; x < interiorX + interiorWidth; x++) {
            for (int y = interiorY; y < interiorY + interiorHeight; y++) {
                // If interior coordinate happens to fall out of bounds, ignore it
                if (isOutOfBounds(x, y)) {
                    continue;
                }
                // Round upper left corner
                if (x == interiorX && y == interiorY) {
                    continue;
                // Round lower left corner
                } else if (x == interiorX && y == interiorY + interiorHeight - 1) {
                    continue;
                // Round upper right corner
                } else if (y == interiorY && x == interiorX + interiorWidth - 1) {
                    continue;
                // Round bottom right corner
                } else if (x == interiorX + interiorWidth - 1 && y == interiorY + interiorHeight - 1) {
                    continue;
                }
                interiorTiles.add(grid[y][x]);
                grid[y][x].state = 1;
            }
        }
        // Save room Tiles for later usage
        p.setInteriorTiles(interiorTiles);
    }

    private void setTileNeighbors() {
        for (Tile[] row : grid) {
            for (Tile tile : row) {
                List<Point> points = new ArrayList<Point>();
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (tile.x + dx == tile.x && tile.y + dy == tile.y || isOutOfBounds(tile.x + dx, tile.y + dy)) {
                            continue;
                        }
                        points.add(new Point(tile.x + dx, tile.y + dy));
                    }
                }
                tile.setNeighbors(points);
            }
        }
    }

    private boolean isOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= columns || y >= rows);
    }
}