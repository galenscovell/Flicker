
/**
 * RENDERER CLASS
 * Handles game graphics.
 */

package galenscovell.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

import galenscovell.util.Constants;


public class Renderer {
    private List<Rectangle> objects;
    private OrthographicCamera viewport;
    private ShapeRenderer shapeRenderer;


    public Renderer(List<Rectangle> objects) {
        this.viewport = new OrthographicCamera();
        viewport.setToOrtho(true, Constants.WINDOW_X, Constants.WINDOW_Y - Constants.HUD_HEIGHT);

        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(viewport.combined);

        this.objects = objects;
    }


    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (Rectangle rect : objects) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.0f, 0.5f, 0.0f, 0.8f);
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            shapeRenderer.end();
        }
    }

}
