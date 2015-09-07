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
    private ArrayList<Room> rooms;

    public DungeonBuilder(int columns, int rows) {
        this.rows = rows;
        this.columns = columns;
        this.grid = new Tile[64][64];
        build();
    }

    private void build() {
        // Construct Tile[rows][columns] grid of all walls
        int mapSize = 64;
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                grid[y][x] = new Tile(x, y);
            }
        }
        // Place random Rooms, ensuring that they do not collide
        // Minus one from width and height at end so rooms are separated
        System.out.println("Placing rooms...");
        this.rooms = new ArrayList<Room>();
        int roomCount = getRandom(10, 15);
        int minSize = 8;
        int maxSize = 15;
        for (int i = 0; i < roomCount; i++) {
            int x = getRandom(1, mapSize - maxSize - 1);
            int y = getRandom(1, mapSize - maxSize - 1);
            int w = getRandom(minSize, maxSize);
            int h = getRandom(minSize, maxSize);
            Room room = new Room(x, y, w, h);
            if (doesCollide(room)) {
                i--;
                continue;
            }
            room.width--;
            room.height--;
            this.rooms.add(room);
        }
        squashRooms();
        // Construct corridors between each Room and its closest neighboring Room
        System.out.println("Connecting rooms...");
        for (int i = 0; i < roomCount; i++) {
            Room roomA = this.rooms.get(i);
            Room roomB = findClosestRoom(roomA);
            int pointAX = getRandom(roomA.x, roomA.x + roomA.width);
            int pointAY = getRandom(roomA.y, roomA.y + roomA.height);
            int pointBX = getRandom(roomB.x, roomB.x + roomB.width);
            int pointBY = getRandom(roomB.y, roomB.y + roomB.width);
            while ((pointBX != pointAX) || (pointBY != pointAY)) {
                if (pointBX != pointAX) {
                    if (pointBX > pointAX) {
                        pointBX--;
                    } else {
                        pointBX++;
                    }
                } else if (pointBY != pointAY) {
                    if (pointBY > pointAY) {
                        pointBY--;
                    } else {
                        pointBY++;
                    }
                }
                this.grid[pointBY][pointBX].state = 1;
            }
        }
        // Set all Tiles within each Room as floor
        System.out.println("Filling rooms...");
        for (int i = 0; i < roomCount; i++) {
            Room room = this.rooms.get(i);
            for (int x = room.x; x < room.x + room.width; x++) {
                for (int y = room.y; y < room.y + room.height; y++) {
                    this.grid[y][x].state = 1;
                }
            }
        }
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

    private void squashRooms() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < this.rooms.size(); j++) {
                Room room = this.rooms.get(i);
                while (true) {
                    int oldX = room.x;
                    int oldY = room.y;
                    if (room.x > 1) {
                        room.x--;
                    }
                    if (room.y > 1) {
                        room.y--;
                    }
                    if ((room.x == 1) && (room.y == 1)) {
                        break;
                    }
                    if (doesCollide(room, j)) {
                        room.x = oldX;
                        room.y = oldY;
                        break;
                    }
                }
            }
        }
    }

    private Room findClosestRoom(Room room) {
        int midX = room.x + (room.width / 2);
        int midY = room.y + (room.height / 2);
        Room closest = null;
        int closestDistance = 1000;
        for (int i = 0; i < this.rooms.size(); i++) {
            Room check = this.rooms.get(i);
            if (check == room) {
                continue;
            }
            int checkMidX = check.x + (check.width / 2);
            int checkMidY = check.y + (check.height / 2);
            int distance = Math.min(Math.abs(midX - checkMidX) - (room.width / 2) - (check.width / 2), Math.abs(midY - checkMidY) - (room.height / 2) - (check.height / 2));
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = check;
            }
        }
        return closest;
    }

    private boolean doesCollide(Room room) {
        for (int i = 0; i < this.rooms.size(); i++) {
            Room check = this.rooms.get(i);
            if (!((room.x + room.width < check.x) || (room.x > check.x + check.width) || (room.y + room.height < check.y) || (room.y > check.y + check.height))) {
                return true;
            }
        }
        return false;
    }

    private boolean doesCollide(Room room, int ignore) {
        for (int i = 0; i < this.rooms.size(); i++) {
            if (i == ignore) {
                continue;
            }
            Room check = this.rooms.get(i);
            if (!((room.x + room.width < check.x) || (room.x > check.x + check.width) || (room.y + room.height < check.y) || (room.y > check.y + check.height))) {
                return true;
            }
        }
        return false;
    }

    private int getRandom(int lo, int hi) {
        return (int)(Math.random() * (hi - lo)) + lo;
    }

    private boolean isOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= columns || y >= rows);
    }

    public void print() {
        System.out.println("Beginning map print");
        for (Tile[] row : grid) {
            System.out.println();
            for (Tile tile : row) {
                if (tile.state == 0) {
                    System.out.print(' ');
                } else {
                    System.out.print('#');
                }
            }
        }
        System.out.println();
    }
}