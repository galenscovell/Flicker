package galenscovell.logic;

import java.util.Map;

/**
 * BUILDER INTERFACE
 * All builders build(), smooth() and getTiles()
 *
 * @author Galen Scovell
 */

public interface Builder {
    public void build();
    public void smooth(Tile tile);
    public Map<Integer, Tile> getTiles();
}