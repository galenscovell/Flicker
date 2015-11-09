package galenscovell.world;

import java.util.ArrayList;

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
        this.type = newType;
    }

    public String getType() {
        return type;
    }

    public void addConnection(Room room) {
        this.connections.add(room);
    }

    public ArrayList<Room> getConnections() {
        return this.connections;
    }
}
