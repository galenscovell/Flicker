package galenscovell.processing;

import galenscovell.things.entities.Hero;

public class RayCaster {
    private int radius, centerX, centerY;
    private int[][] mult;
    private float[][] resistanceMap;

    public RayCaster(float[][] resistanceMap, Hero hero) {
        this.radius = 3;
        this.mult = new int[][]{
            {1, 0, 0, -1, -1, 0, 0, 1},
            {0, 1, -1, 0, 0, -1, 1, 0},
            {0, 1, 1, 0, 0, -1, -1, 0},
            {1, 0, 0, 1, -1, 0, 0, -1}
        };
        this.resistanceMap = resistanceMap;
    }

    public void instantiate(Hero hero, int tileSize) {
        centerX = hero.getX() / tileSize;
        centerY = hero.getY() / tileSize;
        for (int i = 0; i < 8; i++) {
            castRay(1, 1.0f, 0.0f, mult[0][i], mult[1][i], mult[2][i], mult[3][i]);
        }
    }

    public void drawLine(int minX, int maxX, int minY, int maxY, int tileSize) {
        minX /= tileSize;
        minY /= tileSize;
        maxX /= tileSize;
        maxY /= tileSize;
        for (int x = 0; x < resistanceMap[0].length; x++) {
            for (int y = 0; y < resistanceMap.length; y++) {
                if (x >= minX && x <= maxX && y >= minY && y <= maxY) {

                }
            }
        }
    }

    private void castRay(int row, float startSlope, float endSlope, int xx, int xy, int yx, int yy) {
        float newStart = 0.0f;
        if (startSlope < endSlope) {
            return;
        }

        boolean blocked = false;
        for (int distance = row; distance <= radius && !blocked; distance++) {
            int dy = -distance;
            for (int dx = -distance; dx <= 0; dx++) {
                int currentX = centerX + dx * xx + dy * xy;
                int currentY = centerY + dx * yx + dy * yy;
                float leftSlope = (dx - 0.5f) / (dy + 0.5f);
                float rightSlope = (dx + 0.5f) / (dy - 0.5f);

                if (!(currentX >= 0 && currentY >= 0 && currentX < resistanceMap[0].length && currentY < resistanceMap.length) || startSlope < rightSlope) {
                    continue;
                } else if (endSlope > leftSlope) {
                    break;
                }

                // Check if in lightable area and light if needed
                float radiusCircle = (float) Math.sqrt(dx * dx + dy * dy);
                if (radiusCircle < radius) {
                    float brightness = (1.0f - (radiusCircle / radius));
                    resistanceMap[currentY][currentX] = brightness;
                }

                // Previous cell was blocking
                if (blocked) {
                    if (resistanceMap[currentY][currentX] >= 1) {
                        newStart = rightSlope;
                        continue;
                    } else {
                        blocked = false;
                        startSlope = newStart;
                    }
                } else {
                    // Hit wall within sight line
                    if (resistanceMap[currentY][currentX] >= 1 && distance < radius) {
                        blocked = true;
                        castRay(distance + 1, startSlope, leftSlope, xx, xy, yx, yy);
                        newStart = rightSlope;
                    }
                }
            }
        }
    }

    public void updateResistanceMap(int x, int y, boolean blocking) {
        if (blocking) {
            resistanceMap[y][x] = 2.0f;
        } else {
            resistanceMap[y][x] = 0.0f;
        }
    }
}