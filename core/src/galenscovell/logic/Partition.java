package galenscovell.logic;

import java.util.Random;

/**
 * PARTITION
 *
 * @author Galen Scovell
 */

public class Partition {
    private final int MIN_SIZE = 7;
    public int x, y, width, height;
    public Partition leftChild, rightChild;
    private Random random;

    public Partition(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.random = new Random();
    }

    public boolean split() {
        // Already split
        if (leftChild != null || rightChild != null) {
            return false;
        }
        // Direction of split
        boolean horizontal = random.nextBoolean();
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
