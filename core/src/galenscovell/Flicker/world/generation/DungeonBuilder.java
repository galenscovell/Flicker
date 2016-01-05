package galenscovell.flicker.world.generation;

import galenscovell.flicker.processing.Point;
import galenscovell.flicker.util.Constants;
import galenscovell.flicker.world.*;

import java.util.*;

public class DungeonBuilder {
    private Tile[][] grid;
    private ArrayList<Room> rooms;

    public DungeonBuilder() {
        this.grid = new Tile[Constants.MAPSIZE][Constants.MAPSIZE];
        build();
        setTileNeighbors();
    }

    private void build() {
        // Construct Tile[MAPSIZE][MAPSIZE] grid of all walls
        for (int x = 0; x < Constants.MAPSIZE; x++) {
            for (int y = 0; y < Constants.MAPSIZE; y++) {
                grid[y][x] = new Tile(x, y);
            }
        }
        int roomCount = getRandom(8, 14);
        placeRooms(roomCount);
        squashRooms();
        connectRooms(roomCount);
        // Set all Tiles within each Room as floor
        for (int i = 0; i < roomCount; i++) {
            ArrayList<Tile> roomTiles = new ArrayList<Tile>();
            Room room = this.rooms.get(i);
            for (int x = room.x; x < room.x + room.width; x++) {
                for (int y = room.y; y < room.y + room.height; y++) {
                    this.grid[y][x].becomeFloor();
                    roomTiles.add(this.grid[y][x]);
                }
            }
            room.setTiles(roomTiles);
        }
        // Make empty Tiles around floor Tiles walls
        for (int x = 0; x < Constants.MAPSIZE; x++) {
            for (int y = 0; y < Constants.MAPSIZE; y++) {
                if (this.grid[x][y].isFloor()) {
                    for (int xx = x - 1; xx <= x + 1; xx++) {
                        for (int yy = y - 1; yy <= y + 1; yy++) {
                            if (this.grid[xx][yy].isEmpty()) {
                                this.grid[xx][yy].becomeWall();
                            }
                        }
                    }
                }
            }
        }
    }

    private void placeRooms(int roomCount) {
        // Place random Rooms, ensuring that they do not collide
        // Minus one from width and height at end so rooms are separated
        this.rooms = new ArrayList<Room>();
        int minSize = 6;
        int maxSize = 12;
        for (int i = 0; i < roomCount; i++) {
            int x = getRandom(1, Constants.MAPSIZE - maxSize - 1);
            int y = getRandom(1, Constants.MAPSIZE - maxSize - 1);
            int w = getRandom(minSize, maxSize);
            int h = getRandom(minSize, maxSize);
            Room room = new Room(x, y, w, h);
            if (doesCollide(room, -1)) {
                i--;
                continue;
            }
            room.width--;
            room.height--;
            this.rooms.add(room);
        }
    }

    private void squashRooms() {
        // Shift each Room towards upper left corner to reduce distance between Rooms
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < this.rooms.size(); j++) {
                Room room = this.rooms.get(j);
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

    private void connectRooms(int roomCount) {
        // Construct corridors between each Room and its closest neighboring Room
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
                this.grid[pointBY][pointBX].becomeFloor();
            }
            roomA.addConnection(roomB);
            roomB.addConnection(roomA);
        }
    }

    private Room findClosestRoom(Room room) {
        // Return nearest Room to target Room
        int midX = room.x + (room.width / 2);
        int midY = room.y + (room.height / 2);
        Room closest = null;
        int closestDistance = 1000;
        for (int i = 0; i < this.rooms.size(); i++) {
            Room check = this.rooms.get(i);
            if (check == room || room.getConnections().contains(check)) {
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

    private boolean doesCollide(Room room, int ignore) {
        // Return if target Room overlaps already placed Room
        for (int i = 0; i < this.rooms.size(); i++) {
            if (i == ignore) {
                continue;
            }
            Room check = this.rooms.get(i);
            if (!((room.x + room.width < check.x - 2) || (room.x - 2 > check.x + check.width) || (room.y + room.height < check.y - 2) || (room.y - 2 > check.y + check.height))) {
                return true;
            }
        }
        return false;
    }

    private int getRandom(int lo, int hi) {
        // Return random int between low and high
        return (int)(Math.random() * (hi - lo)) + lo;
    }

    private void setTileNeighbors() {
        // Set each tiles neighboring points
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
        return (x < 0 || y < 0 || x >= Constants.MAPSIZE || y >= Constants.MAPSIZE);
    }

    public Map<Integer, Tile> getTiles() {
        // Translate Tile[][] grid to HashMap
        Map<Integer, Tile> tiles = new HashMap<Integer, Tile>();
        for (int x = 0; x < Constants.MAPSIZE; x++) {
            for (int y = 0; y < Constants.MAPSIZE; y++) {
                int key = x * Constants.MAPSIZE + y;
                tiles.put(key, grid[y][x]);
            }
        }
        return tiles;
    }

    public void print() {
        // Debug method: print built dungeon to console and exit
        for (Tile[] row : grid) {
            System.out.println();
            for (Tile tile : row) {
                if (tile.isFloor()) {
                    System.out.print('.');
                } else if (tile.isWall()) {
                    System.out.print('#');
                } else if (tile.hasDoor()) {
                    System.out.print('D');
                } else if (tile.isWater()) {
                    System.out.print('~');
                } else {
                    System.out.print(' ');
                }
            }
        }
    }
}