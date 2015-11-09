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
        this.outerLight.setSoftnessLength(4);
        Light.setContactFilter(Constants.BIT_LIGHT, Constants.BIT_GROUP, Constants.BIT_WALL);
        this.innerLight = new PointLight(rayHandler, 40, new Color(0.95f, 0.95f, 0.95f, 0.95f), size - 4, 0, 0);
        this.innerLight.setSoftnessLength(4);
        Light.setContactFilter(Constants.BIT_LIGHT, Constants.BIT_GROUP, Constants.BIT_WALL);
    }

    public void setPosition(float x, float y) {
        this.outerLight.setPosition(x, y);
        this.innerLight.setPosition(x, y);
    }

    public void animate() {
        this.frame++;
        if (this.frame == 6) {
            this.outerLight.setDistance(this.size - 0.5f);
        } else if (this.frame == 12) {
            this.outerLight.setDistance(this.size - 1);
        } else if (this.frame == 18) {
            this.outerLight.setDistance(this.size - 1.5f);
        } else if (this.frame == 24) {
            this.outerLight.setDistance(this.size - 1);
        } else if (this.frame == 30) {
            this.outerLight.setDistance(this.size - 0.5f);
        } else if (this.frame == 36) {
            this.outerLight.setDistance(this.size);
            this.frame = 0;
        }
    }
}
