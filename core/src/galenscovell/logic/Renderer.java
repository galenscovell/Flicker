package galenscovell.logic;

import galenscovell.entities.Entity;
import galenscovell.entities.Player;
import galenscovell.graphics.Fog;
import galenscovell.graphics.Torchlight;
import galenscovell.inanimates.Door;
import galenscovell.inanimates.Inanimate;
import galenscovell.inanimates.Stairs;
import galenscovell.screens.HudDisplay;
import galenscovell.util.MonsterParser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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

    private HudDisplay hud;
    private Player player;
    private Fog fog;
    private Torchlight torchlight;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch spriteBatch;
    private int tileSize;
    private float minCamX, minCamY, maxCamX, maxCamY;

    public Renderer(Map<Integer, Tile> tiles) {
        this.tileSize = 32;
        this.camera = new OrthographicCamera(800, 480);
        this.viewport = new FitViewport(800, 480, camera);
        camera.setToOrtho(true, 800, 480);

        this.tiles = tiles;
        this.spriteBatch = new SpriteBatch();
        this.entities = new ArrayList<Entity>();
        this.inanimates = new ArrayList<Inanimate>();

        this.fog = new Fog();
    }

    public void setHud(HudDisplay hud) {
        this.hud = hud;
    }

    public void render(double interpolation, boolean moving) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.interpolate(interpolation);
        if (moving) {
            centerOnPlayer();
        }
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
                inanimate.draw(spriteBatch, tileSize, torchlight);
            }
        }

        // Entity rendering: [x, y] are in pixels
        for (Entity entity : entities) {
            if (illuminated(entity.getX() / tileSize, entity.getY() / tileSize)) {
                entity.draw(spriteBatch, tileSize, interpolation, player);
                if (!entity.isInView()) {
                    entity.toggleInView();
                }
            } else if (entity.isInView()) {
                entity.toggleInView();
            }
        }

        // Player rendering: [x, y] are in pixels
        player.draw(spriteBatch, tileSize, interpolation, null);

        // Effect rendering
        torchlight.findFOV(player, tileSize);
        torchlight.drawLight(spriteBatch, (int) minCamX, (int) maxCamX, (int) minCamY, (int) maxCamY, tileSize);
        fog.render(spriteBatch);

        spriteBatch.end();
        hud.render();
    }

    public List<Entity> getEntityList() {
        return entities;
    }

    public List<Inanimate> getInanimateList() {
        return inanimates;
    }

    public void zoom(float value) {
        if (camera.zoom + value > 2 || camera.zoom + value < 0.25) {
            return;
        }
        camera.zoom += value;
    }

    public void pan(float dx, float dy) {
        camera.translate(-dx, -dy, 0);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hud.resize(width, height);
        centerOnPlayer();
    }

    public void assembleLevel(Player player) {
        placeInanimates();
        placePlayer(player);
        createResistanceMap();
        MonsterParser monsterParser = new MonsterParser();
        for (int i = 0; i < 5; i++) {
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

    private void createResistanceMap() {
        float[][] resistanceMap = new float[60][60];
        for (Tile tile : tiles.values()) {
            if (tile.isPerimeter() || tile.isBlocking()) {
                resistanceMap[tile.y][tile.x] = 2.0f;
            } else {
                resistanceMap[tile.y][tile.x] = 0.0f;
            }
        }
        this.torchlight = new Torchlight(resistanceMap, player);
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
        boolean found = false;
        while (!found) {
            int choiceX = random.nextInt(60);
            int choiceY = random.nextInt(60);
            if (tiles.containsKey(choiceX * 60 + choiceY)) {
                Tile tile = tiles.get(choiceX * 60 + choiceY);
                if (tile.isFloor() && !tile.isOccupied()) {
                    found = true;
                    return tile;
                }
            }
        }
        return null;
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

    private boolean illuminated(int x, int y) {
        int diffX = Math.abs(x - player.getX() / tileSize);
        int diffY = Math.abs(y - player.getY() / tileSize);
        int playerVision = player.getStat("vision");
        // Respect 'rounding' of torchlight when illuminating entities
        if ((diffX == 0 && diffY == playerVision) || (diffY == 0 && diffX == playerVision)) {
            return false;
        } else {
            return(diffX + diffY <= playerVision);
        }
    }
}
