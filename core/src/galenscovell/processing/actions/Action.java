package galenscovell.processing.actions;

import galenscovell.things.entities.Entity;
import galenscovell.world.Tile;

public interface Action {
    void define(int moveType);
    boolean initialized(Entity entity, Tile target);
    boolean act(Entity entity, Tile target);
    void resolve(Entity entity);
}