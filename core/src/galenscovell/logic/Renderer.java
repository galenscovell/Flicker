
/**
 * RENDERER CLASS
 * Handles game graphics.
 */

package galenscovell.logic;

import galenscovell.entities.Player;
import galenscovell.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Map;


public class Renderer {
    private Player player;
    private Map<Integer, Tile> tiles;
    private OrthographicCamera viewport;
    private SpriteBatch batcher;
    private int tileSize;


    public Renderer(Player player, Map<Integer, Tile> tiles) {
        this.tiles = tiles;
        this.tileSize = Constants.TILESIZE;
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
        for (Tile tile : tiles.values()) {
            batcher.draw(tile.sprite, tile.x * tileSize, tile.y * tileSize, tileSize, tileSize);
        }
        batcher.draw(player.sprite, player.getCurrentX(), player.getCurrentY(), tileSize, tileSize);
        batcher.end();
    }

}
