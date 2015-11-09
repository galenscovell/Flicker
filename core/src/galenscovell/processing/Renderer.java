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
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final SpriteBatch spriteBatch;
    private final Repository repo;
    private final Hero hero;
    private final Lighting lighting;
    private final Fog fog;
    private float minCamX, minCamY, maxCamX, maxCamY;

    public Renderer(Hero hero, Lighting lighting, SpriteBatch spriteBatch, Repository repo) {
        // Uses custom units (48, 80) rather than exact pixels (480, 800)
        this.camera = new OrthographicCamera(Constants.SCREEN_X, Constants.SCREEN_Y);
        this.viewport = new FitViewport(Constants.SCREEN_X, Constants.SCREEN_Y, this.camera);
        this.camera.setToOrtho(true, Constants.SCREEN_X, Constants.SCREEN_Y);
        this.spriteBatch = spriteBatch;
        this.repo = repo;
        this.hero = hero;
        this.lighting = lighting;
        this.fog = new Fog();
    }

    public void render(double interpolation) {
        this.findCameraBounds();
        this.spriteBatch.setProjectionMatrix(this.camera.combined);
        this.spriteBatch.begin();
        // Tile rendering: [x, y] are in Tiles, convert to custom units
        for (Tile tile : this.repo.tiles.values()) {
            if (inViewport(tile.x * Constants.TILESIZE, tile.y * Constants.TILESIZE)) {
                tile.draw(this.spriteBatch, Constants.TILESIZE);
            }
        }
        // Object rendering: [x, y] are in Tiles, convert to custom units
        for (Inanimate inanimate : this.repo.inanimates) {
            if (inViewport(inanimate.getX() * Constants.TILESIZE, inanimate.getY() * Constants.TILESIZE)) {
                inanimate.draw(this.spriteBatch, Constants.TILESIZE);
            }
        }
        // Entity rendering: [x, y] are in custom units
        for (Entity entity : this.repo.entities) {
            entity.draw(this.spriteBatch, Constants.TILESIZE, interpolation, hero);
        }
        // Player rendering: [x, y] are in custom units
        this.hero.draw(this.spriteBatch, Constants.TILESIZE, interpolation, null);
        // Background effect rendering
        this.fog.draw(this.spriteBatch);
        this.spriteBatch.end();
        // Lighting rendering
        this.lighting.update(this.hero.getCurrentX() + (Constants.TILESIZE / 2), this.hero.getCurrentY() + (Constants.TILESIZE / 2), this.camera);
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public void zoom(float value) {
        value /= 10000;
        if (this.camera.zoom + value > 1.5 || this.camera.zoom + value < 0.5) {
            return;
        }
        this.camera.zoom += value;
    }

    public void pan(float dx, float dy) {
        this.camera.translate(-dx / (Constants.TILESIZE * 3), -dy / (Constants.TILESIZE * 3), 0);
    }

    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
        this.centerOnPlayer();
    }

    private void centerOnPlayer() {
        this.camera.position.set(this.hero.getCurrentX(), this.hero.getCurrentY(), 0);
    }

    private void findCameraBounds() {
        // Find camera upper left coordinates
        minCamX = this.camera.position.x - (this.camera.viewportWidth / 2) * this.camera.zoom;
        minCamY = this.camera.position.y - (this.camera.viewportHeight / 2) * this.camera.zoom;
        // Find camera lower right coordinates
        maxCamX = minCamX + this.camera.viewportWidth * this.camera.zoom;
        maxCamY = minCamY + this.camera.viewportHeight * this.camera.zoom;
        this.camera.update();
    }

    private boolean inViewport(int x, int y) {
        return ((x + Constants.TILESIZE) >= minCamX && x <= maxCamX && (y + Constants.TILESIZE) >= minCamY && y <= maxCamY);
    }

    public void dispose() {
        this.lighting.dispose();
    }
}
