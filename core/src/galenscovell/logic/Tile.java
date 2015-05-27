
/**
 * TILE CLASS
 * Keeps track of tile position, state and rendering.
 * State can be Wall(0), Floor(1), Corridor(2) or Perimeter(3)
 */

package galenscovell.logic;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.List;

import galenscovell.graphics.SpriteSheet;


public class Tile {
    public int x, y, state;
    private int floorNeighbors;
    private List<Point> neighborTilePoints;
    private int bitmask;
    public Sprite sprite;
    private boolean occupied, blocking;


    public Tile(int x, int y, int columns, int rows) {
        this.x = x;
        this.y = y;
        this.state = 0;
        this.bitmask = 0;
        this.neighborTilePoints = findNeighbors(columns, rows);
    }

    public boolean isWall() {
        return state == 0;
    }

    public boolean isFloor() {
        return state == 1;
    }

    public boolean isCorridor() {
        return state == 2;
    }

    public boolean isPerimeter() {
        return state == 3;
    }

    public boolean isWater() {
        return state == 4;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void toggleOccupied() {
        if (occupied) {
            occupied = false;
        } else {
            occupied = true;
        }
    }

    public boolean isBlocking() {
        return blocking;
    }

    public void toggleBlocking() {
        if (blocking) {
            blocking = false;
        } else {
            blocking = true;
        }
    }

    public void setFloorNeighbors(int value) {
        floorNeighbors = value;
    }

    public int getFloorNeighbors() {
        return floorNeighbors;
    }

    public List<Point> getNeighbors() {
        return neighborTilePoints;
    }

    public void setBitmask(int value) {
        bitmask = value;
    }

    public int getBitmask() {
        return bitmask;
    }

    public void findSprite() {
        SpriteSheet sheet = SpriteSheet.tilesheet;
        if (isPerimeter()) {
            switch (bitmask) {
                case 0:
                    sprite = new Sprite(sheet.getSprite(53));
                    break;
                case 1:
                case 100:
                case 101:
                    sprite = new Sprite(sheet.getSprite(16));
                    break;
                case 10:
                case 1000:
                case 1010:
                    sprite = new Sprite(sheet.getSprite(1));
                    break;
                case 11:
                    sprite = new Sprite(sheet.getSprite(32));
                    break;
                case 110:
                    sprite = new Sprite(sheet.getSprite(0));
                    break;
                case 111:
                    sprite = new Sprite(sheet.getSprite(19));
                    break;
                case 1001:
                    sprite = new Sprite(sheet.getSprite(34));
                    break;
                case 1011:
                    sprite = new Sprite(sheet.getSprite(36));
                    break;
                case 1100:
                    sprite = new Sprite(sheet.getSprite(2));
                    break;
                case 1101:
                    sprite = new Sprite(sheet.getSprite(21));
                    break;
                case 1110:
                    sprite = new Sprite(sheet.getSprite(4));
                    break;
                case 1111:
                default:
                    state = 1;
                    sprite = new Sprite(sheet.getSprite(5));
            }
        } else if (isFloor()) {
            switch (bitmask) {
                case 0:
                    sprite = new Sprite(sheet.getSprite(65));
                    break;
                case 1:
                    sprite = new Sprite(sheet.getSprite(49));
                    break;
                case 10:
                    sprite = new Sprite(sheet.getSprite(66));
                    break;
                case 11:
                    sprite = new Sprite(sheet.getSprite(50));
                    break;
                case 100:
                    sprite = new Sprite(sheet.getSprite(81));
                    break;
                case 101:
                    sprite = new Sprite(sheet.getSprite(69));
                    break;
                case 110:
                    sprite = new Sprite(sheet.getSprite(82));
                    break;
                case 111:
                    sprite = new Sprite(sheet.getSprite(70));
                    break;
                case 1000:
                    sprite = new Sprite(sheet.getSprite(64));
                    break;
                case 1001:
                    sprite = new Sprite(sheet.getSprite(48));
                    break;
                case 1010:
                    sprite = new Sprite(sheet.getSprite(67));
                    break;
                case 1011:
                    sprite = new Sprite(sheet.getSprite(51));
                    break;
                case 1100:
                    sprite = new Sprite(sheet.getSprite(80));
                    break;
                case 1101:
                    sprite = new Sprite(sheet.getSprite(68));
                    break;
                case 1110:
                    sprite = new Sprite(sheet.getSprite(83));
                    break;
                case 1111:
                    sprite = new Sprite(sheet.getSprite(53));
                    break;
                default:
                    sprite = new Sprite(sheet.getSprite(65));
            }
        } else if (isWater()) {
            switch (bitmask) {
                case 0:
                case 100:
                    sprite = new Sprite(sheet.getSprite(25));
                    break;
                case 1:
                case 101:
                    sprite = new Sprite(sheet.getSprite(9));
                    break;
                case 10:
                case 110:
                    sprite = new Sprite(sheet.getSprite(26));
                    break;
                case 11:
                case 111:
                    sprite = new Sprite(sheet.getSprite(10));
                    break;
                case 1000:
                case 1100:
                    sprite = new Sprite(sheet.getSprite(24));
                    break;
                case 1001:
                case 1101:
                    sprite = new Sprite(sheet.getSprite(8));
                    break;
                case 1010:
                case 1110:
                    sprite = new Sprite(sheet.getSprite(28));
                    break;
                case 1011:
                case 1111:
                    sprite = new Sprite(sheet.getSprite(12));
                    break;
                default:
                    sprite = new Sprite(sheet.getSprite(25));
            }
        }
    }

    private List<Point> findNeighbors(int columns, int rows) {
        List<Point> points = new ArrayList<Point>();
        int sumX, sumY;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                sumX = x + i;
                sumY = y + j;

                if ((sumX == x && sumY == y || (isOutOfBounds(sumX, sumY, columns, rows)))) {
                    continue;
                }
                points.add(new Point(sumX, sumY));
            }
        }
        return points;
    }

    private boolean isOutOfBounds(int i, int j, int columns, int rows) {
        if (i < 0 || j < 0) {
            return true;
        } else if (i >= columns || j >= rows) {
            return true;
        } else {
            return false;
        }
    }
}