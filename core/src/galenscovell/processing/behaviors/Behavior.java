package galenscovell.processing.behaviors;

import galenscovell.things.entities.Entity;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.world.Tile;

public interface Behavior {
    void setEntityTarget(Entity entity);
    void setInanimateTarget(Inanimate inanimate);
    void setTileTarget(Tile tile);
    void updatePriority();

    void setMoveRange(int[][] pattern);
    boolean isAggressive();
}
