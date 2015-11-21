package galenscovell.world;

import com.badlogic.gdx.graphics.g2d.*;
import galenscovell.processing.Point;
import galenscovell.util.ResourceManager;

import java.util.List;

public class Tile {
    public int x, y;
    private TileType type;
    private int floorNeighbors, currentFrame, frames;
    private List<Point> neighborTilePoints;
    private short bitmask;
    private Sprite[] sprites;
    private boolean occupied, blocking, door, highlighted;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = TileType.EMPTY;
        this.frames = 0;
        this.currentFrame = 0;
        this.door = false;
    }

    public String examine() {
        return "Empty space";
    }

    public Sprite getSprite() {
        return sprites[0];
    }

    public boolean isEmpty() {
        return type == TileType.EMPTY;
    }

    public void becomeEmpty() {
        type = TileType.EMPTY;
    }

    public boolean isFloor() {
        return type == TileType.FLOOR;
    }

    public void becomeFloor() {
        type = TileType.FLOOR;
    }

    public boolean isWall() {
        return type == TileType.WALL;
    }

    public void becomeWall() {
        type = TileType.WALL;
    }

    public boolean isWater() {
        return type == TileType.WATER;
    }

    public void becomeWater() {
        type = TileType.WATER;
    }

    public boolean hasDoor() {
        return door;
    }

    public void toggleDoor() {
        door = !door;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void enableHighlight() {
        highlighted = true;
    }

    public void disableHighlight() {
        highlighted = false;
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
        if (highlighted) {
            batch.draw(ResourceManager.highlight, x * tileSize, y * tileSize, tileSize, tileSize);
        }
        frames++;
    }
}