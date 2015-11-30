package galenscovell.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import galenscovell.things.entities.Entity;

import java.util.*;

public class CombatTexter {
    private final List<TextObject> displayed;
    private final Stack<TextObject> finished;

    public CombatTexter() {
        this.displayed = new ArrayList<TextObject>();
        this.finished = new Stack<TextObject>();
    }

    public void addText(Entity entity, int val) {
        displayed.add(new TextObject(entity, val));
    }

    public void draw(SpriteBatch batch) {
        for (TextObject obj : displayed) {
            obj.draw(batch);
            if (obj.done()) {
                finished.push(obj);
            }
        }
        while (!finished.empty()) {
            displayed.remove(finished.pop());
        }
    }
}
