package galenscovell.logic.world;

import java.util.ArrayList;

/**
 * ROOM DATA STRUCTURE
 * Stores Room position, dimensions, containing Tiles and directly connected Rooms
 *
 * @author Galen Scovell
 */

public class Room {
    public int x, y, width, height;
    public ArrayList<Tile> tiles;
    private String type;
    private ArrayList<Room> connections;

    public Room(int topLeftX, int topLeftY, int width, int height) {
        this.x = topLeftX;
        this.y = topLeftY;
        this.width = width;
        this.height = height;
        this.connections = new ArrayList<Room>();
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    public void setType(String newType) {
        type = newType;
    }

    public String getType() {
        return type;
    }

    public void addConnection(Room room) {
        connections.add(room);
    }

    public ArrayList<Room> getConnections() {
        return connections;
    }
}
