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
    private CorridorPathfinder pathfinder;

    public DungeonBuilder(int columns, int rows) {
        this.rows = rows;
        this.columns = columns;
        this.grid = new Tile[rows][columns];
        this.rooms = new ArrayList<Partition>();
        build();
        // Change number of rooms here
        partition(11);
        this.pathfinder = new CorridorPathfinder(grid, columns, rows);
        setTileNeighbors();
        connect();
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
        // Set room's center tile
        p.setCenterTile(grid[interiorY + (interiorHeight / 2)][interiorX + (interiorWidth / 2)]);
        // Save room tiles for later usage
        p.setInteriorTiles(interiorTiles);
    }

    private void connect() {
        // Iterate through rooms, for each room locate its two nearest rooms
        // Then use pathfinder to construct corridors connecting them
        List<Tile> roomCenters = new ArrayList<Tile>();
        for (Partition room : rooms) {
            roomCenters.add(room.centerTile);
        }
        for (Partition room : rooms) {
            Tile closest = null;
            Tile secondClosest = null;
            for (Tile center : roomCenters) {
                if (room.centerTile == center) {
                    continue;
                }
                if (closest == null || calcDistance(room.centerTile, center) < calcDistance(room.centerTile, closest)) {
                    closest = center;
                } else if (secondClosest == null || calcDistance(room.centerTile, center) < calcDistance(room.centerTile, secondClosest)) {
                    secondClosest = center;
                }
            }
            createCorridor(room.centerTile, closest);
            createCorridor(room.centerTile, secondClosest);
        }
    }

    private double calcDistance(Tile start, Tile end) {
        // Orthogonal Manhattan distance
        return Math.abs(end.x - start.x) + Math.abs(end.y - start.y);
    }

    private void createCorridor(Tile start, Tile end) {
        // Pop off tiles along path until start tile reached
        // At each tile, set it and all of its 8 neighbors as floor (corridors 3 tiles thick)
        Stack<Tile> path = pathfinder.findPath(start, end);
        while (!path.isEmpty()) {
            Tile tile = path.pop();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (isOutOfBounds(tile.x + dx, tile.y + dy)) {
                        continue;
                    }
                    grid[tile.y + dy][tile.x + dx].state = 1;
                }
            }
            // One tile thick corridors grid[tile.y][tile.x].state = 1;
        }
    }

    private void setTileNeighbors() {
        // Set each tiles neighboring points for later usage
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