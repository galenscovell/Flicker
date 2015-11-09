package galenscovell.processing;

import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class RayCaster {
    private int centerX, centerY;
    private final int[][] mult;
    private int[][] resistanceMap;
    private int[][] rangeMap;
    private final Map<Integer, Tile> tiles;

    public RayCaster(Map<Integer, Tile> tiles) {
        this.tiles = tiles;
        this.mult = new int[][]{
            {1, 0, 0, -1, -1, 0, 0, 1},
            {0, 1, -1, 0, 0, -1, 1, 0},
            {0, 1, 1, 0, 0, -1, -1, 0},
            {1, 0, 0, 1, -1, 0, 0, -1}
        };
        this.updateResistanceMap();
    }

    public void updateResistanceMap() {
        int[][] resistanceMap = new int[Constants.MAPSIZE][Constants.MAPSIZE];
        for (Tile tile : this.tiles.values()) {
            int resistance = tile.isBlocking() ? 1 : 0;
            resistanceMap[tile.y][tile.x] = resistance;
        }
        this.resistanceMap = resistanceMap;
    }

    public List<Tile> instantiate(Entity entity, List<Tile> pattern, int radius) {
        this.centerX = entity.getX() / Constants.TILESIZE;
        this.centerY = entity.getY() / Constants.TILESIZE;
        this.rangeMap = new int[Constants.MAPSIZE][Constants.MAPSIZE];
        for (int i = 0; i < 8; i++) {
            this.castRay(radius, 1, 1.0f, 0.0f, this.mult[0][i], this.mult[1][i], this.mult[2][i], this.mult[3][i]);
        }
        return this.drawLine(pattern);
    }

    public List<Tile> drawLine(List<Tile> pattern) {
        List<Tile> range = new ArrayList<Tile>();
        for (int x = 0; x < this.rangeMap[0].length; x++) {
            for (int y = 0; y < this.rangeMap.length; y++) {
                if (this.rangeMap[y][x] == 1) {
                    Tile tile = this.tiles.get(x * Constants.MAPSIZE + y);
                    if (tile != null) {
                        range.add(tile);
                    }
                }
            }
        }
        range.retainAll(pattern);
        return range;
    }

    private void castRay(int radius, int row, float startSlope, float endSlope, int xx, int xy, int yx, int yy) {
        float newStart = 0.0f;
        if (startSlope < endSlope) {
            return;
        }

        boolean blocked = false;
        for (int distance = row; distance <= radius && !blocked; distance++) {
            int dy = -distance;
            for (int dx = -distance; dx <= 0; dx++) {
                int currentX = this.centerX + dx * xx + dy * xy;
                int currentY = this.centerY + dx * yx + dy * yy;
                float leftSlope = (dx - 0.5f) / (dy + 0.5f);
                float rightSlope = (dx + 0.5f) / (dy - 0.5f);

                if (!(currentX >= 0 && currentY >= 0 &&
                        currentX < this.resistanceMap[0].length &&
                        currentY < this.resistanceMap.length) ||
                        startSlope < rightSlope) {
                    continue;
                } else if (endSlope > leftSlope) {
                    break;
                }

                // Check if in desired range and not blocked, add to range display map
                float radiusCircle = (float) Math.sqrt(dx * dx + dy * dy);
                if (radiusCircle < radius && this.resistanceMap[currentY][currentX] != 1) {
                    this.rangeMap[currentY][currentX] = 1;
                }

                // Previous cell was blocking
                if (blocked) {
                    if (this.resistanceMap[currentY][currentX] == 1) {
                        newStart = rightSlope;
                        continue;
                    } else {
                        blocked = false;
                        startSlope = newStart;
                    }
                } else {
                    // Hit blocking object
                    if (this.resistanceMap[currentY][currentX] == 1 && distance < radius) {
                        blocked = true;
                        this.castRay(radius, distance + 1, startSlope, leftSlope, xx, xy, yx, yy);
                        newStart = rightSlope;
                    }
                }
            }
        }
    }
}