package galenscovell.processing.actions;

import galenscovell.things.entities.Entity;
import galenscovell.world.Tile;

public class Event {
    private final Entity entity;
    private final Action action;
    private Tile target;

    public Event(Entity entity, Tile target, Action action) {
        this.entity = entity;
        this.target = target;
        this.action = action;
    }

    public void setTarget(Tile target) {
        this.target = target;
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

    public Entity getEntity() {
        return entity;
    }
}
