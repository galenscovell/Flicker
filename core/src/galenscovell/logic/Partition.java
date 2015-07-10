package galenscovell.logic;

import java.util.List;
import java.util.Random;

/**
 * PARTITION DATA STRUCTURE
 * Partition is a tree structure containing right and left Partition children.
 *
 * @author Galen Scovell
 */

public class Partition {
    public int x, y, width, height;
    public Partition leftChild, rightChild;
    public List<Tile> interiorTiles;
    public Tile centerTile;

    public Partition(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setInteriorTiles(List<Tile> tiles) {
        this.interiorTiles = tiles;
    }

    public void setCenterTile(Tile tile) {
        this.centerTile = tile;
    }

    public boolean split() {
        int MIN_SIZE = 7;
        Random random = new Random();
        // Already split
        if (leftChild != null || rightChild != null) {
            return false;
        }
        // Direction of split
        Boolean horizontal = random.nextBoolean();
        // Max height/width to split off
        int max = (horizontal ? height : width) - MIN_SIZE;
        // Area too small
        if (max < MIN_SIZE) {
            return false;
        }
        // Generate split point
        int split = random.nextInt(max);
        if (split < MIN_SIZE) {
            return false;
        }
        if (horizontal) {
            // Top horizontal child
            leftChild = new Partition(x, y, width, split);
            // Bottom horizontal child
            rightChild = new Partition(x, y + split, width, height - split);
        } else {
            // Left vertical child
            leftChild = new Partition(x, y, split, height);
            // Right vertical child
            rightChild = new Partition(x + split, y, width - split, height);
        }
        // Split successful
        return true;
    }
}
