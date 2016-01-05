package galenscovell.flicker.processing.actions;

import galenscovell.flicker.things.entities.Entity;
import galenscovell.flicker.world.Tile;

public interface Action {
    String[] getInfo();
    boolean setTarget(Tile tile);
    boolean initialize();
    boolean act();
    void resolve();
    void exit();
    Entity getUser();
    Tile getTarget();
}