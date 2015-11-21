package galenscovell.processing.actions;

import galenscovell.things.entities.Entity;
import galenscovell.world.Tile;

public interface Action {
    void setTarget(Tile tile);
    boolean initialize();
    boolean act();
    void resolve();
    Entity getUser();
}