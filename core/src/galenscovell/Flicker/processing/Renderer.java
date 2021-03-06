package galenscovell.flicker.processing;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import galenscovell.flicker.graphics.*;
import galenscovell.flicker.things.entities.*;
import galenscovell.flicker.things.inanimates.Inanimate;
import galenscovell.flicker.util.Constants;
import galenscovell.flicker.world.Tile;

public class Renderer {
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final SpriteBatch spriteBatch;
    private final Repository repo;
    private final Hero hero;
    private final Lighting lighting;
    private final Fog fog;
    private final Vector3 lerpPos;
    private float minCamX, minCamY, maxCamX, maxCamY;
    private boolean cameraFollow;

    public Renderer(Hero hero, Lighting lighting, SpriteBatch spriteBatch, Repository repo) {
        this.hero = hero;
        this.lighting = lighting;
        this.spriteBatch = spriteBatch;
        this.repo = repo;
        // Camera custom units rather than exact pixels
        this.camera = new OrthographicCamera(Constants.SCREEN_X, Constants.SCREEN_Y);
        this.viewport = new FitViewport(Constants.SCREEN_X, Constants.SCREEN_Y, camera);
        camera.setToOrtho(true, Constants.SCREEN_X, Constants.SCREEN_Y);
        this.fog = new Fog();
        this.lerpPos = new Vector3(0, 0, 0);
    }

    public void render(double interpolation) {
        findCameraBounds();
        if (cameraFollow) {
            centerOnPlayer();
        }
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        // Tile rendering: [x, y] are in Tiles, convert to custom units
        for (Tile tile : repo.getTiles().values()) {
            if (inViewport(tile.x * Constants.TILESIZE, tile.y * Constants.TILESIZE)) {
                tile.draw(spriteBatch, Constants.TILESIZE);
            }
        }
        // Object rendering: [x, y] are in Tiles, convert to custom units
        for (Inanimate inanimate : repo.getInanimates()) {
            if (inViewport(inanimate.getX() * Constants.TILESIZE, inanimate.getY() * Constants.TILESIZE)) {
                inanimate.draw(spriteBatch, Constants.TILESIZE);
            }
        }
        // Entity rendering: [x, y] are in custom units
        for (Entity entity : repo.getEntities()) {
            entity.draw(spriteBatch, Constants.TILESIZE, interpolation, hero);
        }
        // Player rendering: [x, y] are in custom units
        hero.draw(spriteBatch, Constants.TILESIZE, interpolation, null);
        // Effect rendering
        fog.draw(spriteBatch);
        repo.getCombatTexter().draw(spriteBatch);
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
        camera.position.set(hero.getCurrentX(), hero.getCurrentY(), 0);
    }

    public void setCameraFollow(boolean setting) {
        cameraFollow = setting;
    }

    private void centerOnPlayer() {
        lerpPos.set(hero.getCurrentX(), hero.getCurrentY(), 0);
        camera.position.lerp(lerpPos, 0.1f);
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
        return ((x + Constants.TILESIZE) >= minCamX &&
                x <= maxCamX &&
                (y + Constants.TILESIZE) >= minCamY &&
                y <= maxCamY);
    }

    public void dispose() {
        lighting.dispose();
    }
}
