package galenscovell.graphics;

import box2dLight.*;
import com.badlogic.gdx.graphics.Color;
import galenscovell.util.Constants;

public class Torch {
    public int size;
    private int frame;
    public PointLight outerLight, innerLight;

    public Torch(int size, RayHandler rayHandler, float r, float g, float b, float a) {
        this.size = size;
        this.frame = 0;
        this.outerLight = new PointLight(rayHandler, 40, new Color(r, g, b, a), size, 0, 0);
        outerLight.setSoftnessLength(4);
        outerLight.setContactFilter(Constants.BIT_LIGHT, Constants.BIT_GROUP, Constants.BIT_WALL);
        this.innerLight = new PointLight(rayHandler, 40, new Color(0.95f, 0.95f, 0.95f, 0.95f), size - 4, 0, 0);
        innerLight.setSoftnessLength(4);
        innerLight.setContactFilter(Constants.BIT_LIGHT, Constants.BIT_GROUP, Constants.BIT_WALL);
    }

    public void setPosition(float x, float y) {
        outerLight.setPosition(x, y);
        innerLight.setPosition(x, y);
    }

    public void animate() {
        frame++;
        if (frame == 6) {
            outerLight.setDistance(size - 0.5f);
        } else if (frame == 12) {
            outerLight.setDistance(size - 1);
        } else if (frame == 18) {
            outerLight.setDistance(size - 1.5f);
        } else if (frame == 24) {
            outerLight.setDistance(size - 1);
        } else if (frame == 30) {
            outerLight.setDistance(size - 0.5f);
        } else if (frame == 36) {
            outerLight.setDistance(size);
            frame = 0;
        }
    }
}
