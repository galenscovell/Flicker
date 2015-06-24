package galenscovell.logic;

import java.util.List;
import java.util.Map;

/**
 * BITMASKER
 * Handles calculation of bitmask value for Tiles.
 *
 *    1         1       Total = (Sum of occupied values)
 *  8 * 2       * 2     ex total = (1 + 2) = 3
 *    4                 Bitmask = binary value of total = 11
 *
 * Bitmask value range: 0, 1111 [0, 15] (None occupied, all occupied)
 * Bitmask value determines sprite of Tile.
 *
 * @author Galen Scovell
 */

public class Bitmasker {
    public int findBitmask(Tile tile, Tile[][] tiles) {
        int value = 0;
        List<Point> neighbors = tile.getNeighbors();

        // Find neighbor positions
        for (Point neighbor : neighbors) {
            Tile neighborTile = tiles[neighbor.y][neighbor.x];
            // Find neighboring perimeter Tiles
            if (neighborTile != null && (neighborTile.isPerimeter() || (tile.isWater() && neighborTile.isFloor()))) {
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
            return calculateBinary(value);
        }
    }

    private int calculateBinary(int value) {
        int remainder;
        String strResult = "";

        // Binary value: divide value by 2, remainder is digit of binary.
        // Current value is then value from division, repeat.
        // Reverse final result.
        while (value != 0) {
            remainder = value % 2;
            strResult += Integer.toString(remainder);
            value /= 2;
        }

        String reversed = "";
        for (int i = strResult.length() - 1; i >= 0; i--) {
            reversed += strResult.charAt(i);
        }

        int result = Integer.parseInt(reversed);
        return result;
    }
}