
/**
 * CONSTANTS CLASS
 * Storage of constants used throughout application for easy access.
 */

package galenscovell.util;


public class Constants {

    private Constants() { }

    // Width/height of application window (in pixels)
    public static final int WINDOW_X = 960;
    public static final int WINDOW_Y = 624;

    // Width/height of world (in pixels)
    public static final int WORLD_WIDTH = 4800;
    public static final int WORLD_HEIGHT = 4800;

    // Tile square dimensions (in pixels)
    public static final int TILESIZE = 48;

    // Columns/rows in world (in Tiles)
    public static final int TILE_COLUMNS = WORLD_WIDTH / TILESIZE;
    public static final int TILE_ROWS = WORLD_HEIGHT / TILESIZE;

    // Height of HUD (in pixels)
    public static final int HUD_HEIGHT = TILESIZE * 2;

    // Renders per second = Framerate
    // Logic updates per second = Framerate / Timestep
    public static final int FRAMERATE = 60;
    public static final int TIMESTEP = 8;

    // Number of smoothing passes during world generation
    public static final int WORLD_SMOOTHING_PASSES = 6;
}
