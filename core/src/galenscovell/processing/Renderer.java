package galenscovell.processing;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import galenscovell.graphics.*;
import galenscovell.things.entities.*;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

public class Renderer {
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch spriteBatch;
    private Hero hero;
    private Lighting lighting;
    private Fog fog;
    private float minCamX, minCamY, maxCamX, maxCamY;

    public Renderer(Hero hero, Lighting lighting, SpriteBatch spriteBatch) {
        // Uses custom units (48, 80) rather than exact pixels (480, 800)
        this.camera = new OrthographicCamera(Constants.SCREEN_X, Constants.SCREEN_Y);
        this.viewport = new FitViewport(Constants.SCREEN_X, Constants.SCREEN_Y, camera);
        camera.setToOrtho(true, Constants.SCREEN_X, Constants.SCREEN_Y);
        this.spriteBatch = spriteBatch;
        this.hero = hero;
        this.lighting = lighting;
        this.fog = new Fog();
    }

    public void render(double interpolation) {
        findCameraBounds();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        // Tile rendering: [x, y] are in Tiles, convert to custom units
        for (Tile tile : Repository.tiles.values()) {
            if (inViewport(tile.x * Constants.TILESIZE, tile.y * Constants.TILESIZE)) {
                tile.draw(spriteBatch, Constants.TILESIZE);
            }
        }
        // Object rendering: [x, y] are in Tiles, convert to custom units
        for (Inanimate inanimate : Repository.inanimates) {
            if (inViewport(inanimate.getX() * Constants.TILESIZE, inanimate.getY() * Constants.TILESIZE)) {
                inanimate.draw(spriteBatch, Constants.TILESIZE);
            }
        }
        // Entity rendering: [x, y] are in custom units
        for (Entity entity : Repository.entities) {
            entity.draw(spriteBatch, Constants.TILESIZE, interpolation, hero);
        }
        // Player rendering: [x, y] are in custom units
        hero.draw(spriteBatch, Constants.TILESIZE, interpolation, null);
        // Background effect rendering
        fog.draw(spriteBatch);
        spriteBatch.end();
        // Lighting rendering
        lighting.update(hero.getCurrentX() + (Constants.TILESIZE / 2), hero.getCurrentY() + (Constants.TILESIZE / 2), camera);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void zoom(float value) {
        value /= 10000;
        if (camera.zoom + value > 1.5 || camera.zoom + value < 0.5) {
            return;
        }
        camera.zoom += value;
    }

    public void pan(float dx, float dy) {
        camera.translate(-dx / (Constants.TILESIZE * 3), -dy / (Constants.TILESIZE * 3), 0);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        centerOnPlayer();
    }

    private void centerOnPlayer() {
        camera.position.set(hero.getCurrentX(), hero.getCurrentY(), 0);
    }

    private void findCameraBounds() {
        // Find camera upper left coordinates
        minCamX = camera.position.x - (camera.viewportWidth / 2) * camera.zoom;
        minCamY = camera.position.y - (camera.viewportHeight / 2) * camera.zoom;
        // Find camera lower right coordinates
        maxCamX = minCamX + camera.viewportWidth * camera.zoom;
        maxCamY = minCamY + camera.viewportHeight * camera.zoom;
        camera.update();
    }

    private boolean inViewport(int x, int y) {
        return ((x + Constants.TILESIZE) >= minCamX && x <= maxCamX && (y + Constants.TILESIZE) >= minCamY && y <= maxCamY);
    }

    public void dispose() {
        lighting.dispose();
    }
}
