package galenscovell.flicker.util;

public class Constants {
    private Constants() {}

    // Base movement style
    public static final boolean DIAGONAL_MOVEMENT = false;

    // Lighting designations
    public static final short BIT_LIGHT = 1;
    public static final short BIT_WALL = 2;
    public static final short BIT_GROUP = 5;

    // UI dimensions (actual pixel size)
    public static final int UI_X = 480;
    public static final int UI_Y = 800;
    // Game dimensions (custom units)
    public static final int SCREEN_X = 48;
    public static final int SCREEN_Y = 80;
    public static final int TILESIZE = 5;
    // Tile dimensions of map (MAPSIZExMAPSIZE)
    public static final int MAPSIZE = 64;

    // 100-level skills: physical damage
    public static final int LUNGE_TYPE = 101;
    public static final int BASH_TYPE = 102;
    // 200-level skills: maneuverability
    public static final int ROLL_TYPE = 201;
    public static final int LEAP_TYPE = 202;
    // 300-level skills: magic damage

    // 400-level skills: buffs

    // 500-level skills: debuffs
}
