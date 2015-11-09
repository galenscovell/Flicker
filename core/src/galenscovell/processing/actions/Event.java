package galenscovell.processing.actions;

import galenscovell.things.entities.Entity;
import galenscovell.world.Tile;

public class Event {
    public Entity entity;
    public Tile target;
    private final Action action;

    public Event(Entity entity, Tile target, Action action) {
        this.entity = entity;
        this.target = target;
        this.action = action;
    }

    public void setTarget(Tile target) {
        this.target = target;
    }

    public boolean start() {
        return this.action.initialized(this.entity, this.target);
    }

    public boolean step() {
        return this.action.act(this.entity, this.target);
    }

    public void finish() {
        this.action.resolve(this.entity);
    }
}
