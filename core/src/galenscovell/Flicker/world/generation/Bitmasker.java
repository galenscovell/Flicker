package galenscovell.flicker.world.generation;

import galenscovell.flicker.processing.Point;
import galenscovell.flicker.util.Constants;
import galenscovell.flicker.world.Tile;

import java.util.*;

public class Bitmasker {
    /**
     *
     *    1         1       Total = (Sum of occupied values)
     *  8 * 2       * 2     ex total = (1 + 2) = 3
     *    4
     * Bitmask value range: 0, 15 (None occupied, all occupied)
     * Bitmask value determines sprite of Tile.
     */
    public short findBitmask(Tile tile, Map<Integer, Tile> tiles) {
        short value = 0;
        List<Point> neighbors = tile.getNeighbors();

        // If analyzed tile is wall, checks if neighbors are walls
        // If analyzed tile is floor, checks if neighbors are walls or water
        // If analyzed tile is water, checks if neighbors are floors or walls
        for (Point neighbor : neighbors) {
            Tile neighborTile = tiles.get(neighbor.x * Constants.MAPSIZE + neighbor.y);
            if (neighborTile != null && (neighborTile.isWall() || (tile.isFloor() && neighborTile.isWater()) || (tile.isWater() && neighborTile.isFloor()))) {
                int diffX = tile.x - neighborTile.x;
                int diffY = tile.y - neighborTile.y;

                if (diffX == -1 && diffY == 0) {
                    value += 2;
                } else if (diffX == 0) {
                    if (diffY == -1) {
                        value += 4;
                    } else if (diffY == 1) {
                        value += 1;
                    }
                } else if (diffX == 1 && diffY == 0) {
                    value += 8;
                }
            }
        }

        if (value == 0) {
            return 0;
        } else {
            return value;
        }
    }
}