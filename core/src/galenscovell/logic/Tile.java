package galenscovell.logic;

import galenscovell.util.ResourceManager;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

/**
 * TILE
 * Keeps track of tile position, state and rendering.
 * State can be Floor(1), Wall(2) or Water(3)
 *
 * @author Galen Scovell
 */

public class Tile {
    public int x, y, state;
    private int floorNeighbors, currentFrame, frames;
    private List<Point> neighborTilePoints;
    private short bitmask;
    private Sprite[] sprites;
    private boolean occupied, blocking, destination;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = 0;
        this.frames = 0;
        this.currentFrame = 0;
    }

    public boolean isUnused() {
        return state == 0;
    }

    public boolean isFloor() {
        return state == 1;
    }

    public boolean isWall() {
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

    public void setNeighbors(List<Point> points) {
        this.neighborTilePoints = points;
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
            sprites[0] = new Sprite(ResourceManager.tileAtlas.createSprite("wall" + bitmask));
            sprites[1] = new Sprite(ResourceManager.tileAtlas.createSprite("wall" + bitmask));
        } else if (isFloor()) {
            sprites[0] = new Sprite(ResourceManager.tileAtlas.createSprite("floor" + bitmask));
            sprites[1] = new Sprite(ResourceManager.tileAtlas.createSprite("floor" + bitmask));
        } else if (isWater()) {
            sprites[0] = new Sprite(ResourceManager.tileAtlas.createSprite("waterA" + bitmask));
            sprites[1] = new Sprite(ResourceManager.tileAtlas.createSprite("waterB" + bitmask));
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
}