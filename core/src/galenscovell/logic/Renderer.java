package galenscovell.logic;

import galenscovell.entities.Entity;
import galenscovell.entities.Player;
import galenscovell.graphics.Fog;
import galenscovell.inanimates.Door;
import galenscovell.inanimates.Inanimate;
import galenscovell.inanimates.Stairs;
import galenscovell.util.MonsterParser;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.*;
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
    private RayHandler rayHandler;
    private PointLight torch;
    private World world;
    private Box2DDebugRenderer debug;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch spriteBatch;

    private int rows, columns, tileSize;
    private float minCamX, minCamY, maxCamX, maxCamY;

    public Renderer(World world, RayHandler rayHandler, Map<Integer, Tile> tiles, SpriteBatch spriteBatch, int tileSize) {
        this.tileSize = tileSize;
        this.camera = new OrthographicCamera(800, 480);
        this.viewport = new FitViewport(800, 480, camera);
        camera.setToOrtho(true, 800, 480);

        this.tiles = tiles;
        this.world = world;
        createTileBodies();
        this.spriteBatch = spriteBatch;
        this.entities = new ArrayList<Entity>();
        this.inanimates = new ArrayList<Inanimate>();

        this.fog = new Fog();
        this.rayHandler = rayHandler;
        RayHandler.useDiffuseLight(true);
        // Set environment to pitch black
        rayHandler.setAmbientLight(0.0f);
        this.torch = new PointLight(rayHandler, 40, new Color(0.9f, 0.9f, 0.95f, 1.0f), 400, 200, 200);
        // Depth which light continues through collision objects
        torch.setSoftnessLength(60);

        this.debug = new Box2DDebugRenderer();
    }

    public void render(double interpolation) {
        player.interpolate(interpolation);
        findCameraBounds();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        // Tile rendering: [x, y] are in Tiles, convert to pixels
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
        fog.render(spriteBatch);
        spriteBatch.end();

        rayHandler.setCombinedMatrix(camera.combined);
        rayHandler.updateAndRender();

        debug.render(world, camera.combined);
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
    }

    private boolean inViewport(int x, int y) {
        return ((x + tileSize) >= minCamX && x <= maxCamX && (y + tileSize) >= minCamY && y <= maxCamY);
    }

    private void createTileBodies() {
        // Setup box2D bodies for light collision
        PolygonShape tileShape = new PolygonShape();
        tileShape.setAsBox(16, 16);
        BodyDef tileBodyDef = new BodyDef();
        tileBodyDef.type = BodyDef.BodyType.StaticBody;
        FixtureDef tileFixture = new FixtureDef();
        tileFixture.shape = tileShape;

        for (Tile tile : tiles.values()) {
            if (tile.isPerimeter()) {
                tileFixture.filter.groupIndex = 0;
                tileBodyDef.position.set(tile.x * 32 + 16, tile.y * 32 + 16);
                Body tileBody = world.createBody(tileBodyDef);
                tileBody.createFixture(tileFixture);
            }
        }
        tileShape.dispose();
    }
}
