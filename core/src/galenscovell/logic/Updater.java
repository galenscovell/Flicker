
/**
 * UPDATER CLASS
 * Handles game logic: interactions, movements, behaviors and HUD updates.
 */

package galenscovell.logic;

import com.badlogic.gdx.math.Rectangle;

import java.util.List;

import galenscovell.util.Constants;


public class Updater {
    private List<Rectangle> objects;


    public Updater(List<Rectangle> objects) {
        this.objects = objects;
    }

    public void update(float delta) {
        for (Rectangle rect : objects) {
            rect.x++;
            if (rect.x > Constants.WINDOW_X) {
                rect.x = 0;
            }
        }

    }

}
