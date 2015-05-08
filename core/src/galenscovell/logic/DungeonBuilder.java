
/**
 * DUNGEONBUILDER CLASS
 * Constructs a new world grid and tileset with dungeon features. 
 * (Rectangular rooms connected by Tile-width corridors)
 */

package galenscovell.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class DungeonBuilder implements Builder {
    private int rows, columns;
    private Tile[][] grid;


    public DungeonBuilder(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        this.grid = new Tile[rows][columns];
    }

    public void build() {
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                // All tiles begin as walls
                grid[y][x] = new Tile(x, y, columns, rows);
            }
        }
        // Find center of Tile grid for creation of first room
        int midColumn = (columns - 1) / 2;
        int midRow = (rows - 1) / 2;
        createRoom(midColumn, midRow);
    }

    public void createRoom(int centerX, int centerY) {
        Random generator = new Random();
        // Possible room sizes from (5x5) to (9x9) tiles
        int roomSize = generator.nextInt(3) + 2;
        List<Point> perimeterPoints = new ArrayList<Point>();
        int sumX, sumY;

        // Set center Tile as floor
        grid[centerY][centerX].state = 1;

        // Find all tiles within (-roomsize, roomsize) from center
        for (int x = -roomSize; x <= roomSize; x++) {
            for (int y = -roomSize; y <= roomSize; y++) {
                sumX = centerX + x;
                sumY = centerY + y;
                if (isOutOfBounds(sumX, sumY)) {
                    continue;
                }
                // Find perimeter tiles
                if (Math.abs(x) == roomSize || Math.abs(y) == roomSize) {
                    // If point is corner of room, do not use as corridor entry point
                    if (Math.abs(x) == roomSize && Math.abs(y) == roomSize) {
                        // Uncomment to make rooms have square corners
                        // grid[sumY][sumX].state = 1;
                        continue;
                    }
                    perimeterPoints.add(new Point(sumX, sumY));
                }
                grid[sumY][sumX].state = 1;
            }
        }
        int chosenPoint = generator.nextInt(perimeterPoints.size() - 1);
        Point corridorPoint = perimeterPoints.get(chosenPoint);
        findCorridorDirection(corridorPoint.x, corridorPoint.y);
    }

    public void findCorridorDirection(int startX, int startY) {
        int sumX, sumY;
        String direction = "";

        for (int x = -1; x <= 1; x += 2) {
            sumX = startX + x;
            sumY = startY;
            if (isOutOfBounds(sumX, sumY)) {
                continue;
            }
            if (grid[sumY][sumX].isWall()) {
                if (x == -1) {
                    direction = "left";
                } else if (x == 1) {
                    direction = "right";
                }
            }
        }
        for (int y = -1; y <= 1; y += 2) {
            sumX = startX;
            sumY = startY + y;
            if (isOutOfBounds(sumX, sumY)) {
                continue;
            }
            if (grid[sumY][sumX].isWall()) {
                if (y == -1) {
                    direction = "up";
                } else if (y == 1) {
                    direction = "down";
                }
            }
        }
        extendCorridor(direction, startX, startY);
    }

    public void extendCorridor(String direction, int startX, int startY) {
        Random generator = new Random();
        // Possible corridor length from 5 to 10 tiles
        int corridorSize = generator.nextInt(6) + 5;
        int currentX = startX;
        int currentY = startY;

        // Set corridor starting point as Floor
        grid[startY][startX].state = 1;

        for (int i = 0; i < corridorSize; i++) {
            if (direction.equals("up") && !isOutOfBounds(currentX, currentY - 1)) {
                currentY -= 1;
            } else if (direction.equals("right") && !isOutOfBounds(currentX + 1, currentY)) {
                currentX += 1;
            } else if (direction.equals("down") && !isOutOfBounds(currentX, currentY + 1)) {
                currentY += 1;
            } else if (direction.equals("left") && !isOutOfBounds(currentX - 1, currentY)) {
                currentX -= 1;
            }
            grid[currentY][currentX].state = 1;
        }
        // Set final Tile as Corridor for next room creation
        grid[currentY][currentX].state = 2;
    }

    public boolean isOutOfBounds(int x, int y) {
        if (x < 0 || y < 0){
            return true;
        } else if (x >= columns || y >= rows){
            return true;
        } else {
            return false;
        }
    }

    public void smooth(Tile tile) {
        // Create new room at final Tile from extendCorridor()
        if (tile.isCorridor()) {
            if (tile.getFloorNeighbors() < 5) {
                createRoom(tile.x, tile.y);
            } else {
                tile.state = 1;
            }
        }
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
}