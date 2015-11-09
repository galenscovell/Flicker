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

    public boolean isEmpty() {
        return this.type == TileType.EMPTY;
    }

    public void becomeEmpty() {
        this.type = TileType.EMPTY;
    }

    public boolean isFloor() {
        return this.type == TileType.FLOOR;
    }

    public void becomeFloor() {
        this.type = TileType.FLOOR;
    }

    public boolean isWall() {
        return this.type == TileType.WALL;
    }

    public void becomeWall() {
        this.type = TileType.WALL;
    }

    public boolean isWater() {
        return this.type == TileType.WATER;
    }

    public void becomeWater() {
        this.type = TileType.WATER;
    }

    public boolean hasDoor() {
        return this.door;
    }

    public void toggleDoor() {
        this.door = !this.door;
    }

    public boolean isHighlighted() {
        return this.highlighted;
    }

    public void toggleHighlighted() {
        this.highlighted = !this.highlighted;
    }

    public boolean isOccupied() {
        return this.occupied;
    }

    public void toggleOccupied() {
        this.occupied = !this.occupied;
    }

    public boolean isBlocking() {
        return this.blocking;
    }

    public void toggleBlocking() {
        this.blocking = !this.blocking;
    }

    public void setFloorNeighbors(int value) {
        this.floorNeighbors = value;
    }

    public int getFloorNeighbors() {
        return this.floorNeighbors;
    }

    public void setNeighbors(List<Point> points) {
        this.neighborTilePoints = points;
    }

    public List<Point> getNeighbors() {
        return this.neighborTilePoints;
    }

    public void setBitmask(short value) {
        this.bitmask = value;
    }

    public short getBitmask() {
        return this.bitmask;
    }

    public void findSprite() {
        this.sprites = new Sprite[2];
        if (this.isWall()) {
            this.sprites[0] = new Sprite(ResourceManager.tileAtlas.createSprite("wall" + this.bitmask));
            this.sprites[1] = new Sprite(ResourceManager.tileAtlas.createSprite("wall" + this.bitmask));
        } else if (this.isFloor()) {
            this.sprites[0] = new Sprite(ResourceManager.tileAtlas.createSprite("floor" + this.bitmask));
            this.sprites[1] = new Sprite(ResourceManager.tileAtlas.createSprite("floor" + this.bitmask));
        } else if (this.isWater()) {
            this.sprites[0] = new Sprite(ResourceManager.tileAtlas.createSprite("waterA" + this.bitmask));
            this.sprites[1] = new Sprite(ResourceManager.tileAtlas.createSprite("waterB" + this.bitmask));
        }
        this.sprites[0].flip(false, true);
        this.sprites[1].flip(false, true);
    }

    public void draw(SpriteBatch batch, int tileSize) {
        if (this.frames == 60) {
            if (this.currentFrame == 0) {
                this.currentFrame++;
            } else if (this.currentFrame == 1) {
                this.currentFrame--;
            }
            this.frames -= this.frames;
        }
        batch.draw(this.sprites[this.currentFrame], this.x * tileSize, this.y * tileSize, tileSize, tileSize);
        if (this.highlighted) {
            batch.draw(ResourceManager.highlight, this.x * tileSize, this.y * tileSize, tileSize, tileSize);
        }
        this.frames++;
    }
}