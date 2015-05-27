
/**
 * CONSTANTS CLASS
 * Storage of constants used throughout application for easy access.
 */

package galenscovell.util;


public class Constants {

    private Constants() { }

    // Width/height of application window (in pixels)
    public static final int WINDOW_X = 800;
    public static final int WINDOW_Y = 480;

    // Width/height of world (in pixels)
    public static final int WORLD_WIDTH = 2000;
    public static final int WORLD_HEIGHT = 2000;

    // Tile square dimensions (in pixels)
    public static final int TILESIZE = 32;

    // Columns/rows in world (in Tiles)
    public static final int TILE_COLUMNS = WORLD_WIDTH / TILESIZE;
    public static final int TILE_ROWS = WORLD_HEIGHT / TILESIZE;

    // Renders per second = Framerate (Libgdx renders ~60/s)
    // Logic updates per second = Framerate / Timestep
    public static final float TIMESTEP = 12;

    // Number of smoothing passes during world generation
    public static final int WORLD_SMOOTHING_PASSES = 6;
}
