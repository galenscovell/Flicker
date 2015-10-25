package galenscovell.processing.actions;

import galenscovell.things.entities.Entity;
import galenscovell.world.Tile;

public class Event {
    public Entity entity;
    public Tile target;
    private Action action;

    public Event(Entity entity, Tile target, Move move) {
        this.entity = entity;
        this.target = target;
        this.action = move;
    }

    public boolean initialized() {
        return action.initialized(entity, target);
    }

    public boolean act() {
        return action.act(entity, target);
    }

    public void resolve() {
        action.resolve(entity);
    }
}
