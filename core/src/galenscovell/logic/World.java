
/**
 * WORLD CLASS
 * World will be composed of a 2D array grid and a list of matching Tile instances.
 */

package galenscovell.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;


public class World {
    private List<Rectangle> objects;


    public World() {
        this.objects = new ArrayList<Rectangle>();
        objects.add(new Rectangle(0, 0, 48, 48));
        objects.add(new Rectangle(0, 48, 48, 48));
    }


    public List<Rectangle> getObjects() {
        return objects;
    }
}
