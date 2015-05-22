
/**
 * RENDERER CLASS
 * Handles game graphics.
 */

package galenscovell.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.entities.Entity;
import galenscovell.entities.Player;

import galenscovell.graphics.Fog;
import galenscovell.graphics.Torchlight;

import galenscovell.inanimates.Door;
import galenscovell.inanimates.Inanimate;
import galenscovell.inanimates.Stairs;

import galenscovell.screens.HudDisplay;

import galenscovell.util.Constants;
import galenscovell.util.MonsterParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Renderer {
    private Map<Integer, Tile> tiles;
    private List<Entity> entities;
    private List<Inanimate> inanimates;

    private HudDisplay hud;
    private Player player;
    private Fog fog;
    private Torchlight torchlight;

    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private int tileSize;
    private float minCamX, minCamY, maxCamX, maxCamY;


    public Renderer(Map<Integer, Tile> tiles) {
        this.tileSize = Constants.TILESIZE;
        this.camera = new OrthographicCamera(Constants.WINDOW_X, Constants.WINDOW_Y);
        camera.setToOrtho(true, Constants.WINDOW_X, Constants.WINDOW_Y);

        this.spriteBatch = new SpriteBatch();
        this.tiles = tiles;
        this.entities = new ArrayList<Entity>();
        this.inanimates = new ArrayList<Inanimate>();

        this.fog = new Fog();
    }

    public void setHud(HudDisplay hud) {
        this.hud = hud;
    }


    public void render(double interpolation) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        findCameraBounds();
        spriteBatch.begin();

        // World rendering: [x, y] are in Tiles, convert to pixels
        for (Tile tile : tiles.values()) {
            if (inViewport(tile.x * tileSize, tile.y * tileSize)) {
                spriteBatch.draw(tile.sprite, tile.x * tileSize, tile.y * tileSize, tileSize, tileSize);
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
        // Constants.TILESIZE = (int) (32 * camera.zoom);
    }

    public void pan(float dx, float dy) {
        camera.translate(-dx, -dy, 0);
    }

    public void assembleLevel(Player player) {
        placeInanimates();
        createResistanceMap();
        placePlayer(player);
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

    private void createResistanceMap() {
        float[][] resistanceMap = new float[Constants.TILE_ROWS][Constants.TILE_COLUMNS];
        for (Tile tile : tiles.values()) {
            if (tile.isPerimeter() || tile.isBlocking()) {
                resistanceMap[tile.y][tile.x] = 2.0f;
            } else {
                resistanceMap[tile.y][tile.x] = 0.0f;
            }
        }
        this.torchlight = new Torchlight(resistanceMap);
    }

    private void placePlayer(Player playerInstance) {
        Tile randomTile = findRandomTile();
        this.player = playerInstance;
        player.setPosition(randomTile.x * tileSize, randomTile.y * tileSize);
        randomTile.toggleOccupied();
        camera.position.set(player.getCurrentX(), player.getCurrentY(), 0);
        camera.update();
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
            int choiceX = random.nextInt(Constants.TILE_COLUMNS);
            int choiceY = random.nextInt(Constants.TILE_ROWS);
            if (tiles.containsKey(choiceX * Constants.TILE_COLUMNS + choiceY)) {
                Tile tile = tiles.get(choiceX * Constants.TILE_COLUMNS + choiceY);
                if (tile.isFloor() && !tile.isOccupied()) {
                    found = true;
                    return tile;
                }
            }
        }
        return null;
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
        // Respect 'rounding' of torchlight when illuminating entities
        if ((diffX == 0 && diffY == player.getSightRange()) || (diffY == 0 && diffX == player.getSightRange())) {
            return false;
        } else {
            return(diffX + diffY <= player.getSightRange());
        }
    }
}
