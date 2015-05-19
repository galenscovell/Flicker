
/**
 * TORCHLIGHT CLASS
 * Handles torchlight effect surrounding player.
 */

package galenscovell.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.entities.Player;


public class Torchlight {
    private int radius, startX, startY;
    private int[][] mult;
    private float[][] resistanceMap;
    private float[][] lightMap;
    private Texture rect = new Texture(Gdx.files.internal("textures/blank.png"));


    public Torchlight(float[][] resistanceMap) {
        this.radius = 4;
        this.mult = new int[][]{{1, 0, 0, -1, -1, 0, 0, 1},
                                {0, 1, -1, 0, 0, -1, 1, 0},
                                {0, 1, 1, 0, 0, -1, -1, 0},
                                {1, 0, 0, 1, -1, 0, 0, -1}};
        this.resistanceMap = resistanceMap;
        this.lightMap = new float[resistanceMap.length][resistanceMap[0].length];
    }

    public void findFOV(Player player, int tileSize) {
        startX = player.getX() / tileSize;
        startY = player.getY() / tileSize;
        lightMap[startY][startX] = 0.9f;

        for (int i = 0; i < 8; i++) {
            castLight(1, 1.0f, 0.0f, mult[0][i], mult[1][i], mult[2][i], mult[3][i]);
        }
    }

    public void drawLight(SpriteBatch spriteBatch, int minX, int maxX, int minY, int maxY, int tileSize) {
        minX /= tileSize;
        minY /= tileSize;
        maxX /= tileSize;
        maxY /= tileSize;
        // Keep record of batch color before setting lighting transparency
        Color c = new Color(spriteBatch.getColor());
        // Fill alpha over Tile depending on lightMap value
        for (int x = 0; x < lightMap[0].length; x++) {
            for (int y = 0; y < lightMap.length; y++) {
                if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
                    spriteBatch.setColor(0.0f, 0.0f, 0.0f, 1.0f - lightMap[y][x]);
                    spriteBatch.draw(rect, x * tileSize, y * tileSize, tileSize, tileSize);
                }
                if (lightMap[y][x] > 0.0f) {
                    lightMap[y][x] = 0.1f;
                } else {
                    lightMap[y][x] = 0.0f;
                }
            }
        }
        // Return batch color to original
        spriteBatch.setColor(c);
    }

    private void castLight(int row, float startSlope, float endSlope, int xx, int xy, int yx, int yy) {
        float newStart = 0.0f;
        if (startSlope < endSlope) {
            return;
        }

        boolean blocked = false;
        for (int distance = row; distance <= radius && !blocked; distance++) {
            int dy = -distance;
            for (int dx = -distance; dx <= 0; dx++) {
                int currentX = startX + dx * xx + dy * xy;
                int currentY = startY + dx * yx + dy * yy;
                float leftSlope = (dx - 0.5f) / (dy + 0.5f);
                float rightSlope = (dx + 0.5f) / (dy - 0.5f);

                if (!(currentX >= 0 && currentY >= 0 && currentX < lightMap[0].length && currentY < lightMap.length) || startSlope < rightSlope) {
                    continue;
                } else if (endSlope > leftSlope) {
                    break;
                }

                // Check if in lightable area and light if needed
                float radiusCircle = (float) Math.sqrt(dx * dx + dy * dy);
                if (radiusCircle < radius) {
                    float brightness = (1.0f - (radiusCircle / radius));
                    lightMap[currentY][currentX] = brightness;
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
                        castLight(distance + 1, startSlope, leftSlope, xx, xy, yx, yy);
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