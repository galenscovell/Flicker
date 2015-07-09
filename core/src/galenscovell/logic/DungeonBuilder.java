package galenscovell.logic;

import java.util.*;

/**
 * DUNGEON BUILDER
 * Constructs a new world tileset with dungeon features.
 * (Rectangular rooms connected by Tile-width corridors)
 *
 * @author Galen Scovell
 */

public class DungeonBuilder {
    private int rows, columns;
    private Tile[][] grid;

    public DungeonBuilder(int columns, int rows) {
        this.rows = rows;
        this.columns = columns;
        this.grid = new Tile[rows][columns];
        build();
    }

    public Map<Integer, Tile> getTiles() {
        Map<Integer, Tile> tiles = new HashMap<Integer, Tile>();
        int key;
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                key = x * columns + y;
                tiles.put(key, grid[y][x]);
            }
        }
        return tiles;
    }

    public void smooth(Tile tile) {

    }

    private void build() {
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                // All tiles begin as walls
                grid[y][x] = new Tile(x, y, columns, rows);
            }
        }
        // Binary Space Partitioning
        Random random = new Random();
        List<Partition> partitions = new ArrayList<Partition>();
        Partition root = new Partition(0, 0, columns, rows);
        partitions.add(root);
        int splits = 8;
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
        for (Tile[] row : grid) {
            for (Tile tile : row) {
                if (tile.state == 1) {
                    System.out.print("F ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.exit(0);
    }

    private void generateInterior(Partition p) {
        if (p.leftChild == null) {
            createRoom(p);
        } else {
            generateInterior(p.leftChild);
            generateInterior(p.rightChild);
        }
    }

    private void createRoom(Partition p) {
        for (int x = p.x; x < p.x + p.width; x++) {
            for (int y = p.y; y < p.y + p.height; y++) {
                if (isOutOfBounds(x, y)) {
                    continue;
                }
                if (x == p.x || x == (p.x + p.width) - 1 || y == p.y || y == (p.y + p.height) - 1) {
                    grid[y][x].state = 1;
                }
            }
        }
    }

    private boolean isOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= columns || y >= rows);
    }
}