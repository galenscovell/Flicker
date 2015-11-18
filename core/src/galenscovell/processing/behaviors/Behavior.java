package galenscovell.processing.behaviors;

import galenscovell.things.entities.Entity;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.world.Tile;

public interface Behavior {
    void setTargetTile(Tile tile);
    void setTargetEntity(Entity entity);
    void setTargetInanimate(Inanimate inanimate);
}
