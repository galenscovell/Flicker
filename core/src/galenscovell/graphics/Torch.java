package galenscovell.graphics;

import box2dLight.*;
import com.badlogic.gdx.graphics.Color;
import galenscovell.util.Constants;

public class Torch {
    public int size;
    private int frame;
    public PointLight light;

    public Torch(int size, RayHandler rayHandler, float r, float g, float b, float a) {
        this.size = size;
        this.frame = 0;
        this.light = new PointLight(rayHandler, 40, new Color(r, g, b, a), size, 0, 0);
        light.setSoftnessLength(4);
        light.setContactFilter(Constants.BIT_LIGHT, Constants.BIT_GROUP, Constants.BIT_WALL);
    }

    public void setPosition(float x, float y) {
        light.setPosition(x, y);
    }

    public void animate() {
        frame++;
        if (frame == 6) {
            light.setDistance(size - 0.5f);
        } else if (frame == 12) {
            light.setDistance(size - 1);
        } else if (frame == 18) {
            light.setDistance(size - 1.5f);
        } else if (frame == 24) {
            light.setDistance(size - 1);
        } else if (frame == 30) {
            light.setDistance(size - 0.5f);
        } else if (frame == 36) {
            light.setDistance(size);
            frame = 0;
        }
    }
}
