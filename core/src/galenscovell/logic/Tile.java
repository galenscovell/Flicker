package galenscovell.logic;

import galenscovell.graphics.SpriteSheet;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * TILE
 * Keeps track of tile position, state and rendering.
 * State can be Wall(0), Floor(1), Corridor(2), Perimeter(3) or Water(4)
 *
 * @author Galen Scovell
 */

public class Tile {
    public int x, y, state;
    private int floorNeighbors;
    private List<Point> neighborTilePoints;
    private int bitmask;
    private Sprite[] sprites;
    private int currentFrame, frames;
    private boolean occupied, blocking;

    public Tile(int x, int y, int columns, int rows) {
        this.x = x;
        this.y = y;
        this.state = 0;
        this.bitmask = 0;
        this.neighborTilePoints = findNeighbors(columns, rows);
        this.sprites = new Sprite[2];
        this.frames = 0;
        this.currentFrame = 0;
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
        occupied = !occupied;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public void toggleBlocking() {
        blocking = !blocking;
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
        int s1 = 0;
        int s2 = 0;
        if (isPerimeter()) {
            switch (bitmask) {
                case 0:
                    s1 = 53;
                    s2 = 53;
                    break;
                case 1:
                case 100:
                case 101:
                    s1 = 16;
                    s2 = 16;
                    break;
                case 10:
                case 1000:
                case 1010:
                    s1 = 1;
                    s2 = 1;
                    break;
                case 11:
                    s1 = 32;
                    s2 = 32;
                    break;
                case 110:
                    s1 = 0;
                    s2 = 0;
                    break;
                case 111:
                    s1 = 19;
                    s2 = 19;
                    break;
                case 1001:
                    s1 = 34;
                    s2 = 34;
                    break;
                case 1011:
                    s1 = 36;
                    s2 = 36;
                    break;
                case 1100:
                    s1 = 2;
                    s2 = 2;
                    break;
                case 1101:
                    s1 = 21;
                    s2 = 21;
                    break;
                case 1110:
                    s1 = 4;
                    s2 = 4;
                    break;
                case 1111:
                default:
                    state = 1;
                    s1 = 5;
                    s2 = 5;
            }
        } else if (isFloor()) {
            switch (bitmask) {
                case 1:
                    s1 = 49;
                    s2 = 49;
                    break;
                case 10:
                    s1 = 66;
                    s2 = 66;
                    break;
                case 11:
                    s1 = 50;
                    s2 = 50;
                    break;
                case 100:
                    s1 = 81;
                    s2 = 81;
                    break;
                case 101:
                    s1 = 69;
                    s2 = 69;
                    break;
                case 110:
                    s1 = 82;
                    s2 = 82;
                    break;
                case 111:
                    s1 = 70;
                    s2 = 70;
                    break;
                case 1000:
                    s1 = 64;
                    s2 = 64;
                    break;
                case 1001:
                    s1 = 48;
                    s2 = 48;
                    break;
                case 1010:
                    s1 = 67;
                    s2 = 67;
                    break;
                case 1011:
                    s1 = 51;
                    s2 = 51;
                    break;
                case 1100:
                    s1 = 80;
                    s2 = 80;
                    break;
                case 1101:
                    s1 = 68;
                    s2 = 68;
                    break;
                case 1110:
                    s1 = 83;
                    s2 = 83;
                    break;
                case 1111:
                    s1 = 53;
                    s2 = 53;
                    break;
                default:
                    s1 = 65;
                    s2 = 65;
            }
        } else if (isWater()) {
            switch (bitmask) {
                case 1:
                case 101:
                    s1 = 9;
                    s2 = 41;
                    break;
                case 10:
                case 110:
                    s1 = 26;
                    s2 = 58;
                    break;
                case 11:
                case 111:
                    s1 = 10;
                    s2 = 42;
                    break;
                case 1000:
                case 1100:
                    s1 = 24;
                    s2 = 56;
                    break;
                case 1001:
                case 1101:
                    s1 = 8;
                    s2 = 40;
                    break;
                case 1010:
                case 1110:
                    s1 = 28;
                    s2 = 60;
                    break;
                case 1011:
                case 1111:
                    s1 = 12;
                    s2 = 44;
                    break;
                default:
                    s1 = 25;
                    s2 = 57;
            }
        }
        sprites[0] = new Sprite(sheet.getSprite(s1));
        sprites[1] = new Sprite(sheet.getSprite(s2));
    }

    public void draw(SpriteBatch batch, int tileSize) {
        if (frames == 60) {
            if (currentFrame == 0) {
                currentFrame++;
            } else if (currentFrame == 1) {
                currentFrame--;
            }
            frames -= frames;
        }
        batch.draw(sprites[currentFrame], x * tileSize, y * tileSize, tileSize, tileSize);
        frames++;
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

    private boolean isOutOfBounds(int x, int y, int columns, int rows) {
        return (x < 0 || y < 0 || x >= columns || y >= rows);
    }
}