package galenscovell.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;

import java.util.*;

public class CombatTexter {
    private final List<TextObject> displayed;
    private final Stack<TextObject> finished;

    public CombatTexter() {
        this.displayed = new ArrayList<TextObject>();
        this.finished = new Stack<TextObject>();
    }

    public void addText(Entity entity, int val) {
        int[] vals = new int[] {val, 0};
        displayed.add(new TextObject(entity, val));
    }

    public void draw(SpriteBatch batch) {
        for (TextObject obj : displayed) {
            batch.draw(obj.getSprite(), obj.getX(), obj.getY(), Constants.TILESIZE / 2, Constants.TILESIZE / 2);
            if (obj.done()) {
                finished.push(obj);
            }
        }
        while (!finished.empty()) {
            displayed.remove(finished.pop());
        }
    }
}
