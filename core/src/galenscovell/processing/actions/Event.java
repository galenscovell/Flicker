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

    public boolean start() {
        return action.initialized(entity, target);
    }

    public boolean step() {
        return action.act(entity, target);
    }

    public void finish() {
        action.resolve(entity);
    }
}
