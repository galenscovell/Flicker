package galenscovell.flicker.processing.behaviors;

import galenscovell.flicker.things.entities.Entity;
import galenscovell.flicker.things.inanimates.Inanimate;
import galenscovell.flicker.world.Tile;

public interface Behavior {
    void setTargetTile(Tile tile);
    void setTargetEntity(Entity entity);
    void setTargetInanimate(Inanimate inanimate);
}
