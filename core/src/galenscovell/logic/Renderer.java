
/**
 * RENDERER CLASS
 * Handles game graphics.
 */

package galenscovell.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;


import galenscovell.entities.Player;
import galenscovell.util.Constants;


public class Renderer {
    private Player player;
    private OrthographicCamera viewport;
    private SpriteBatch batcher;


    public Renderer(Player player) {
        this.viewport = new OrthographicCamera();
        viewport.setToOrtho(true, Constants.WINDOW_X, Constants.WINDOW_Y - Constants.HUD_HEIGHT);

        this.batcher = new SpriteBatch();
        batcher.setProjectionMatrix(viewport.combined);

        this.player = player;
    }


    public void render(double interpolation) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.draw(interpolation);

        batcher.begin();
        batcher.draw(player.sprite, player.getCurrentX(), player.getCurrentY());
        batcher.end();
    }

}
