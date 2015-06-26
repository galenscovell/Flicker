package galenscovell.logic;

import galenscovell.entities.Entity;
import galenscovell.entities.Player;
import galenscovell.graphics.Fog;
import galenscovell.inanimates.Door;
import galenscovell.inanimates.Inanimate;
import galenscovell.inanimates.Stairs;
import galenscovell.util.MonsterParser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * RENDERER
 * Handles game graphics.
 *
 * @author Galen Scovell
 */

public class Renderer {
    private Map<Integer, Tile> tiles;
    private List<Entity> entities;
    private List<Inanimate> inanimates;

    private Player player;
    private Fog fog;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch spriteBatch;

    private int rows, columns, tileSize;
    private float minCamX, minCamY, maxCamX, maxCamY;

    public Renderer(Map<Integer, Tile> tiles, SpriteBatch spriteBatch, int tileSize) {
        this.tileSize = tileSize;
        this.camera = new OrthographicCamera(800, 480);
        this.viewport = new FitViewport(800, 480, camera);
        camera.setToOrtho(true, 800, 480);

        this.tiles = tiles;
        this.spriteBatch = spriteBatch;
        this.entities = new ArrayList<Entity>();
        this.inanimates = new ArrayList<Inanimate>();

        this.fog = new Fog();
    }

    public void render(double interpolation) {
        player.interpolate(interpolation);
        findCameraBounds();
        spriteBatch.begin();

        // World rendering: [x, y] are in Tiles, convert to pixels
        for (Tile tile : tiles.values()) {
            if (inViewport(tile.x * tileSize, tile.y * tileSize)) {
                tile.draw(spriteBatch, tileSize);
            }
        }

        // Object rendering: [x, y] are in Tiles, convert to pixels
        for (Inanimate inanimate : inanimates) {
            if (inViewport(inanimate.getX() * tileSize, inanimate.getY() * tileSize)) {
                inanimate.draw(spriteBatch, tileSize);
            }
        }

        // Entity rendering: [x, y] are in pixels
        for (Entity entity : entities) {
            entity.draw(spriteBatch, tileSize, interpolation, player);
            if (!entity.isInView()) {
                entity.toggleInView();
            } else if (entity.isInView()) {
                entity.toggleInView();
            }
        }

        // Player rendering: [x, y] are in pixels
        player.draw(spriteBatch, tileSize, interpolation, null);

        // Effect rendering
        fog.render(spriteBatch);

        spriteBatch.end();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public List<Entity> getEntityList() {
        return entities;
    }

    public List<Inanimate> getInanimateList() {
        return inanimates;
    }

    public void getTile(float x, float y) {
        int tileX = (int) (x / 32);
        int tileY = (int) (y / 32);
        Tile foundTile = tiles.get(tileX * columns + tileY);
        if (foundTile != null) {
            foundTile.toggleSelected();
        }
    }

    public void zoom(float value) {
        if (camera.zoom + value > 1.5 || camera.zoom + value < 0.25) {
            return;
        }
        camera.zoom += value;
    }

    public void pan(float dx, float dy) {
        camera.translate(-dx, -dy, 0);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        centerOnPlayer();
    }

    public void assembleLevel(Player player, int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        placeInanimates();
        placePlayer(player);
        MonsterParser monsterParser = new MonsterParser();
        for (int i = 0; i < 3; i++) {
            placeEntities(monsterParser);
        }
        monsterParser = null;
    }

    private void placeInanimates() {
        for (Tile tile : tiles.values()) {
            if (tile.isFloor() && tile.getFloorNeighbors() > 2) {
                if (tile.getBitmask() == 1010) {
                    inanimates.add(new Door(tile.x, tile.y, 0));
                    tile.toggleBlocking();
                    tile.toggleOccupied();
                } else if (tile.getBitmask() == 101) {
                    inanimates.add(new Door(tile.x, tile.y, 1));
                    tile.toggleBlocking();
                    tile.toggleOccupied();
                }
            }
        }
        Tile stairTile = findRandomTile();
        inanimates.add(new Stairs(stairTile.x, stairTile.y));
    }

    private void placePlayer(Player playerInstance) {
        Tile randomTile = findRandomTile();
        this.player = playerInstance;
        player.setPosition(randomTile.x * tileSize, randomTile.y * tileSize);
        randomTile.toggleOccupied();
        centerOnPlayer();
    }

    private void placeEntities(MonsterParser parser) {
        Tile randomTile = findRandomTile();
        Entity monster = parser.spawn(2);
        monster.setPosition(randomTile.x * tileSize, randomTile.y * tileSize);
        entities.add(monster);
        randomTile.toggleOccupied();
    }

    private Tile findRandomTile() {
        Random random = new Random();
        while (true) {
            int choiceY = random.nextInt(rows);
            int choiceX = random.nextInt(columns);
            Tile tile = tiles.get(choiceX * columns + choiceY);
            if (tile != null && tile.isFloor()) {
                if (tile.isOccupied()) {
                    continue;
                }
                return tile;
            }
        }
    }

    private void centerOnPlayer() {
        camera.position.set(player.getCurrentX(), player.getCurrentY(), 0);
    }

    private void findCameraBounds() {
        minCamX = camera.position.x - (camera.viewportWidth / 2) * camera.zoom;
        minCamY = camera.position.y - (camera.viewportHeight / 2) * camera.zoom;
        maxCamX = minCamX + camera.viewportWidth * camera.zoom;
        maxCamY = minCamY + camera.viewportHeight * camera.zoom;
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    private boolean inViewport(int x, int y) {
        return ((x + tileSize) >= minCamX && x <= maxCamX && (y + tileSize) >= minCamY && y <= maxCamY);
    }
}
