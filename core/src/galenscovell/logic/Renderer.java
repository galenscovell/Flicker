
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

import galenscovell.entities.Player;
import galenscovell.util.Constants;


public class Renderer {
    private Player player;
    private OrthographicCamera viewport;
    private ShapeRenderer shapeRenderer;


    public Renderer(Player player) {
        this.viewport = new OrthographicCamera();
        viewport.setToOrtho(true, Constants.WINDOW_X, Constants.WINDOW_Y - Constants.HUD_HEIGHT);

        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(viewport.combined);

        this.player = player;
    }


    public void render(double interpolation) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.draw(interpolation);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.0f, 0.5f, 0.0f, 0.8f);
        shapeRenderer.rect(player.getCurrentX(), player.getCurrentY(), player.size, player.size);
        shapeRenderer.end();
    }

}
