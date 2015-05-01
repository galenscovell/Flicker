
/**
 * RENDERER CLASS
 * Handles game graphics.
 */

package galenscovell.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.entities.Dead;
import galenscovell.entities.Entity;
import galenscovell.entities.Player;
import galenscovell.entities.Salamander;
import galenscovell.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Renderer {
    private Map<Integer, Tile> tiles;
    private List<Entity> entities;
    private List<Dead> deadList;

    private Player player;

    private OrthographicCamera viewport;
    private SpriteBatch spriteBatch;
    private int tileSize;
    private float minCamX, minCamY, maxCamX, maxCamY;


    public Renderer(Map<Integer, Tile> tiles) {
        this.tileSize = Constants.TILESIZE;
        this.viewport = new OrthographicCamera(Constants.WINDOW_X, Constants.WINDOW_Y - Constants.HUD_HEIGHT);
        viewport.setToOrtho(true, Constants.WINDOW_X, Constants.WINDOW_Y - Constants.HUD_HEIGHT);
        this.spriteBatch = new SpriteBatch();

        this.tiles = tiles;
        this.entities = new ArrayList<Entity>();
        this.deadList = new ArrayList<Dead>();
    }


    public void render(double interpolation) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.interpolate(interpolation);

        findCameraBounds();
        spriteBatch.begin();

        for (Tile tile : tiles.values()) {
            // Tile [x, y] are in Tiles, convert to pixels
            if (inViewport(tile.x * tileSize, tile.y * tileSize)) {
                spriteBatch.draw(tile.sprite, tile.x * tileSize, tile.y * tileSize, tileSize, tileSize);
            }
        }

        for (Dead dead : deadList) {
            // Dead [x, y] are in Tiles, convert to pixels
            if (inViewport(dead.getX() * tileSize, dead.getY() * tileSize)) {
                spriteBatch.draw(dead.sprite, dead.x * tileSize, dead.y * tileSize, tileSize, tileSize);
            }
        }

        for (Entity entity : entities) {
            // Entity [x, y] are in pixels
            if (inViewport(entity.getX(), entity.getY())) {
                entity.interpolate(interpolation);
                if (entity.isAttacking()) {
                    entity.attack(interpolation, player);
                } else {
                    spriteBatch.draw(entity.getSprite(), entity.getCurrentX(), entity.getCurrentY(), tileSize, tileSize);
                }

                int diffX = Math.abs(player.getX() / tileSize - entity.getX() / tileSize);
                int diffY = Math.abs(player.getY() / tileSize - entity.getY() / tileSize);
                if (!entity.isInView() && (diffX + diffY <= entity.getSightRange())) {
                    System.out.println("In LOS, Diff: " + diffX + ", " + diffY);
                    entity.toggleInView();
                } else if (entity.isInView() && (diffX + diffY > entity.getSightRange())) {
                    System.out.println("Out of LOS, Diff: " + diffX + ", " + diffY);
                    entity.toggleInView();
                }
            }
        }

        spriteBatch.draw(player.sprite, player.getCurrentX(), player.getCurrentY(), tileSize, tileSize);
        spriteBatch.end();
    }

    public List<Entity> getEntityList() {
        return entities;
    }

    public List<Dead> getDeadList() {
        return deadList;
    }

    public void assembleLevel(Player player) {
        placePlayer(player);
    }

    public void deconstruct() {
        entities = null;
        deadList = null;
        tiles = null;
    }

    private void placePlayer(Player playerInstance) {
        int placements = 2;
        boolean playerPlaced = false;
        while (placements > 0) {
            Tile tile = findRandomTile();
            if (playerPlaced) {
                entities.add(new Salamander(tile.x * tileSize, tile.y * tileSize));
                tile.toggleOccupied();
            } else {
                this.player = playerInstance;
                player.setPosition(tile.x * tileSize, tile.y * tileSize);
                tile.toggleOccupied();
                playerPlaced = true;
            }
            placements--;
        }
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
        if ((x + tileSize) >= minCamX && x <= maxCamX && (y + tileSize) >= minCamY && y <= maxCamY) {
            return true;
        } else {
            return false;
        }
    }
}
