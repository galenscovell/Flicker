
/**
 * RENDERER CLASS
 * Handles game graphics.
 */

package galenscovell.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.entities.Player;
import galenscovell.util.Constants;

import java.util.Map;


public class Renderer {
    private Player player;
    private Map<Integer, Tile> tiles;
    private OrthographicCamera viewport;
    private SpriteBatch spriteBatch;
    private int tileSize;
    private float minCamX, minCamY, maxCamX, maxCamY;


    public Renderer(Player player, Map<Integer, Tile> tiles) {
        this.tiles = tiles;
        this.tileSize = Constants.TILESIZE;
        this.viewport = new OrthographicCamera(Constants.WINDOW_X, Constants.WINDOW_Y - Constants.HUD_HEIGHT);
        viewport.setToOrtho(true, Constants.WINDOW_X, Constants.WINDOW_Y - Constants.HUD_HEIGHT);
        this.spriteBatch = new SpriteBatch();
        this.player = player;
    }


    public void render(double interpolation) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.draw(interpolation);
        findCameraBounds();
        spriteBatch.begin();

        for (Tile tile : tiles.values()) {
            // Tile [x, y] are in Tiles, convert to pixels
            if (inViewport(tile.x * tileSize, tile.y * tileSize)) {
                spriteBatch.draw(tile.sprite, tile.x * tileSize, tile.y * tileSize, tileSize, tileSize);
            }
        }

        spriteBatch.draw(player.sprite, player.getCurrentX(), player.getCurrentY(), tileSize, tileSize);
        spriteBatch.end();
    }

    private void findCameraBounds() {
        minCamX = player.getCurrentX() - (viewport.viewportWidth / 2);
        minCamY = player.getCurrentY() - (viewport.viewportHeight / 2);
        maxCamX = minCamX + viewport.viewportWidth;
        maxCamY = minCamY + viewport.viewportHeight;

        viewport.position.set(player.getCurrentX(), player.getCurrentY(), 0);
        viewport.update();
        spriteBatch.setProjectionMatrix(viewport.combined);
    }

    private boolean inViewport(int x, int y) {
        if ((x + tileSize) >= minCamX && x <= maxCamX && (y + tileSize) >= minCamY && y <= maxCamY) {
            return true;
        } else {
            return false;
        }
    }
}
