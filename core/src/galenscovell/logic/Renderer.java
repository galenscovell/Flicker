
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

    private OrthographicCamera viewport;
    private SpriteBatch spriteBatch;
    private int tileSize;
    private float minCamX, minCamY, maxCamX, maxCamY;


    public Renderer(Map<Integer, Tile> tiles) {
        this.tileSize = Constants.TILESIZE;
        this.viewport = new OrthographicCamera(Constants.GAME_WIDTH, Constants.WINDOW_Y);
        viewport.setToOrtho(true, Constants.GAME_WIDTH, Constants.WINDOW_Y);
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

        player.interpolate(interpolation);
        findCameraBounds();

        // Set rendering area for batch (this line splits the game from the HUD)
        Gdx.gl.glViewport(0, 0, Constants.GAME_WIDTH, Constants.WINDOW_Y);
        spriteBatch.begin();

        for (Tile tile : tiles.values()) {
            // Tile [x, y] are in Tiles, convert to pixels
            if (inViewport(tile.x * tileSize, tile.y * tileSize)) {
                spriteBatch.draw(tile.sprite, tile.x * tileSize, tile.y * tileSize, tileSize, tileSize);
            }
        }

        for (Inanimate inanimate : inanimates) {
            // Inanimate [x, y] are in Tiles, convert to pixels
            if (inViewport(inanimate.getX() * tileSize, inanimate.getY() * tileSize)) {
                spriteBatch.draw(inanimate.getSprite(), inanimate.getX() * tileSize, inanimate.getY() * tileSize, tileSize, tileSize);
                torchlight.updateResistanceMap(inanimate.getX(), inanimate.getY(), inanimate.isBlocking());
            }
        }

        for (Entity entity : entities) {
            // Entity [x, y] are in pixels
            if (inViewport(entity.getX(), entity.getY())) {
                entity.interpolate(interpolation);
                if (entity.isAttacking()) {
                    entity.attack(interpolation, player);
                }
                spriteBatch.draw(entity.getSprite(), entity.getCurrentX(), entity.getCurrentY(), tileSize, tileSize);

                int diffX = Math.abs(player.getX() / tileSize - entity.getX() / tileSize);
                int diffY = Math.abs(player.getY() / tileSize - entity.getY() / tileSize);
                if (!entity.isInView() && (diffX + diffY <= entity.getSightRange())) {
                    entity.toggleInView();
                } else if (entity.isInView() && (diffX + diffY > entity.getSightRange())) {
                    entity.toggleInView();
                }
            }
        }

        if (player.isAttacking()) {
            player.attack(interpolation, null);
        }
        spriteBatch.draw(player.getSprite(), player.getCurrentX(), player.getCurrentY(), tileSize, tileSize);

        torchlight.findFOV(player, tileSize);
        torchlight.drawLight(spriteBatch, (int) minCamX, (int) maxCamX, (int) minCamY, (int) maxCamY, tileSize);

        fog.render(spriteBatch);

        Gdx.gl.glViewport(Constants.GAME_WIDTH, 0, Constants.HUD_WIDTH, Constants.WINDOW_Y);
        hud.render();
        spriteBatch.end();
    }

    public List<Entity> getEntityList() {
        return entities;
    }

    public List<Inanimate> getInanimateList() {
        return inanimates;
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
    }

    private void placeEntities(MonsterParser parser) {
        Tile randomTile = findRandomTile();
        Entity monster = parser.spawn(2);
        monster.setPosition(randomTile.x * tileSize, randomTile.y * tileSize);
        System.out.println(monster);
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
        minCamX = player.getCurrentX() - (viewport.viewportWidth / 2);
        minCamY = player.getCurrentY() - (viewport.viewportHeight / 2);
        maxCamX = minCamX + viewport.viewportWidth;
        maxCamY = minCamY + viewport.viewportHeight;

        viewport.position.set(player.getCurrentX(), player.getCurrentY(), 0);
        viewport.update();
        spriteBatch.setProjectionMatrix(viewport.combined);
    }

    private boolean inViewport(int x, int y) {
        return ((x + tileSize) >= minCamX && x <= maxCamX && (y + tileSize) >= minCamY && y <= maxCamY);
    }
}
