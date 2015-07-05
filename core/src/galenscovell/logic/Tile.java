package galenscovell.logic;

import galenscovell.util.ResourceManager;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * TILE
 * Keeps track of tile position, state and rendering.
 * State can be Wall(0), Floor(1), Corridor(2), or Water(3)
 *
 * @author Galen Scovell
 */

public class Tile {
    public int x, y, state;
    private int floorNeighbors;
    private List<Point> neighborTilePoints;
    private short bitmask;
    private Sprite[] sprites;
    private int currentFrame, frames;
    private boolean occupied, blocking, unused, destination;

    public Tile(int x, int y, int columns, int rows) {
        this.x = x;
        this.y = y;
        this.state = 0;
        this.neighborTilePoints = findNeighbors(columns, rows);
        this.frames = 0;
        this.currentFrame = 0;
    }

    public void setUnused() {
        this.unused = true;
    }

    public boolean isEmpty() {
        return unused;
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

    public boolean isWater() {
        return state == 3;
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

    public void setAsDestination() {
        destination = true;
    }

    public void removeAsDestination() {
        destination = false;
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

    public void setBitmask(short value) {
        bitmask = value;
    }

    public short getBitmask() {
        return bitmask;
    }

    public void findSprite() {
        this.sprites = new Sprite[2];
        if (isWall()) {
            sprites[0] = new Sprite(ResourceManager.tileAtlas.findRegion("wall" + bitmask));
            sprites[1] = new Sprite(ResourceManager.tileAtlas.findRegion("wall" + bitmask));
        } else if (isFloor()) {
            sprites[0] = new Sprite(ResourceManager.tileAtlas.findRegion("floor" + bitmask));
            sprites[1] = new Sprite(ResourceManager.tileAtlas.findRegion("floor" + bitmask));
        } else if (isWater()) {
            sprites[0] = new Sprite(ResourceManager.tileAtlas.findRegion("waterA" + bitmask));
            sprites[1] = new Sprite(ResourceManager.tileAtlas.findRegion("waterB" + bitmask));
        }
        sprites[0].flip(false, true);
        sprites[1].flip(false, true);
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
        if (destination) {
            batch.draw(ResourceManager.destinationMarker, x * tileSize, y * tileSize, tileSize, tileSize);
        }
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