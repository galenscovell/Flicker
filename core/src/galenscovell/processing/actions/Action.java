package galenscovell.processing.actions;

import galenscovell.things.entities.Entity;
import galenscovell.world.Tile;

public interface Action {
    public void define(int moveType);
    public boolean initialized(Entity entity, Tile target);
    public boolean act(Entity entity, Tile target);
    public void resolve(Entity entity);
}