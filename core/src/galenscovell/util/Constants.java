
/**
 * CONSTANTS CLASS
 * Storage of constants used throughout application for easy access.
 */

package galenscovell.util;


public class Constants {

    private Constants() { }

    // Width/height of application window (in pixels)
    public static final int WINDOW_X = 1008;
    public static final int WINDOW_Y = 672;

    // Width of HUD (in pixels)
    public static final int HUD_HEIGHT = 144;

    // Width of game screen (in pixels)
    public static final int GAME_HEIGHT = WINDOW_Y - HUD_HEIGHT;

    // Width/height of world (in pixels)
    public static final int WORLD_WIDTH = 4800;
    public static final int WORLD_HEIGHT = 4800;

    // Tile square dimensions (in pixels)
    public static final int TILESIZE = 48;

    // Columns/rows in world (in Tiles)
    public static final int TILE_COLUMNS = WORLD_WIDTH / TILESIZE;
    public static final int TILE_ROWS = WORLD_HEIGHT / TILESIZE;

    // Renders per second = Framerate (Libgdx renders ~60/s)
    // Logic updates per second = Framerate / Timestep
    public static final float TIMESTEP = 20;

    // Number of smoothing passes during world generation
    public static final int WORLD_SMOOTHING_PASSES = 6;
}
