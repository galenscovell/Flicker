package galenscovell.processing.actions;

import galenscovell.things.entities.Entity;
import galenscovell.world.Tile;

public interface Action {
    String[] getInfo();
    void setTarget(Tile tile);
    boolean initialize();
    boolean act();
    void resolve();
    void exit();
    Entity getUser();
}