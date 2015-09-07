package galenscovell.logic;


/**
 * ROOM
 *
 * @author Galen Scovell
 */

public class Room {
    public int x, y, width, height;
    private Tile[] tiles;

    public Room(int topLeftX, int topLeftY, int width, int height) {
        this.x = topLeftX;
        this.y = topLeftY;
        this.width = width;
        this.height = height;
    }
}
