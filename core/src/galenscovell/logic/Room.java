package galenscovell.logic;

import java.util.ArrayList;

/**
 * ROOM DATA STRUCTURE
 * Stores Room position, dimensions and containing Tiles
 *
 * @author Galen Scovell
 */

public class Room {
    public int x, y, width, height;
    private ArrayList<Tile> tiles;

    public Room(int topLeftX, int topLeftY, int width, int height) {
        this.x = topLeftX;
        this.y = topLeftY;
        this.width = width;
        this.height = height;
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }
}
