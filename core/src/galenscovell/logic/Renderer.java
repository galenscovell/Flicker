package galenscovell.logic;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import galenscovell.entities.Entity;
import galenscovell.entities.Player;
import galenscovell.graphics.Fog;
import galenscovell.inanimates.Door;
import galenscovell.inanimates.Inanimate;
import galenscovell.util.Constants;
import galenscovell.util.MonsterParser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.*;

/**
 * RENDERER
 * Handles game graphics.
 * Renderer uses custom units (400, 240) rather than pixel dimensions.
 *
 * @author Galen Scovell
 */

public class Renderer {
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch spriteBatch;

    private int tileSize;
    private float minCamX, minCamY, maxCamX, maxCamY;
    private Map<Integer, Tile> tiles;
    private List<Entity> entities;
    private List<Inanimate> inanimates;
    private Fog fog;
    private Player player;

    private RayHandler rayHandler;
    private PointLight torch;
    private World world;
    private Map<Integer, Body> bodies;
    private Box2DDebugRenderer debug;

    public Renderer(Map<Integer, Tile> tiles, SpriteBatch spriteBatch) {
        this.camera = new OrthographicCamera(Constants.SCREEN_X, Constants.SCREEN_Y);
        this.viewport = new FitViewport(Constants.SCREEN_X, Constants.SCREEN_Y, camera);
        camera.setToOrtho(true, Constants.SCREEN_X, Constants.SCREEN_Y);
        this.spriteBatch = spriteBatch;

        this.tileSize = Constants.TILESIZE;
        this.tiles = tiles;
        this.entities = new ArrayList<Entity>();
        this.inanimates = new ArrayList<Inanimate>();
        this.fog = new Fog();

        // Box2D lighting
        this.world = new World(new Vector2(0, 0), true);
        this.rayHandler = new RayHandler(world);
        RayHandler.useDiffuseLight(true);
        rayHandler.setAmbientLight(0, 0, 0, 1);
        this.torch = new PointLight(rayHandler, 60, new Color(0.9f, 0.98f, 0.98f, 1), tileSize * 8, 0, 0);
        torch.setSoftnessLength(tileSize);
        torch.setContactFilter(Constants.BIT_LIGHT, Constants.BIT_GROUP, Constants.BIT_WALL);
        this.debug = new Box2DDebugRenderer();
    }

    public void render(double interpolation) {
        findCameraBounds();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        // Tile rendering: [x, y] are in Tiles, convert to custom units
        for (Tile tile : tiles.values()) {
            if (inViewport(tile.x * tileSize, tile.y * tileSize)) {
                tile.draw(spriteBatch, tileSize);
            }
        }
        // Object rendering: [x, y] are in Tiles, convert to custom units
        for (Inanimate inanimate : inanimates) {
            if (inViewport(inanimate.getX() * tileSize, inanimate.getY() * tileSize)) {
                inanimate.draw(spriteBatch, tileSize);
            }
        }
        // Entity rendering: [x, y] are in custom units
        for (Entity entity : entities) {
            entity.draw(spriteBatch, tileSize, interpolation, player);
        }
        // Player rendering: [x, y] are in custom units
        player.draw(spriteBatch, tileSize, interpolation, null);
        // Background effect rendering
        fog.draw(spriteBatch);
        spriteBatch.end();

        // Center torch on player
        torch.setPosition(player.getCurrentX() + (tileSize / 2), player.getCurrentY() + (tileSize / 2));
        rayHandler.setCombinedMatrix(camera.combined, camera.position.x, camera.position.y, camera.viewportWidth * camera.zoom, camera.viewportHeight * camera.zoom);
        rayHandler.updateAndRender();
        // debug.render(world, camera.combined);
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

    public void zoom(float value) {
        value /= 10000;
        if (camera.zoom + value > 1.5 || camera.zoom + value < 0.5) {
            return;
        }
        camera.zoom += value;
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void assembleLevel(Player player) {
        placeInanimates();
        placePlayer(player);
        MonsterParser monsterParser = new MonsterParser();
        for (int i = 0; i < 3; i++) {
            placeEntities(monsterParser);
        }
        monsterParser = null;
        createTileBodies();
    }

    private void placeInanimates() {
        // Place doors on floors with hallway bitmask and more than 2 adjacent floor neighbors
        Random random = new Random();
        for (Tile tile : tiles.values()) {
            if (tile.isFloor() && tile.getFloorNeighbors() > 3) {
                if (tile.getBitmask() == 5) {
                    inanimates.add(new Door(this, tile.x, tile.y, "h"));
                    tile.toggleBlocking();
                    tile.toggleOccupied();
                    tile.state = 4;
                } else if (tile.getBitmask() == 10) {
                    inanimates.add(new Door(this, tile.x, tile.y, "v"));
                    tile.toggleBlocking();
                    tile.toggleOccupied();
                    tile.state = 4;
                }
            }
        }
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
        entities.add(monster);
        randomTile.toggleOccupied();
    }

    private Tile findRandomTile() {
        Random random = new Random();
        while (true) {
            int choiceY = random.nextInt(Constants.MAPSIZE);
            int choiceX = random.nextInt(Constants.MAPSIZE);
            Tile tile = tiles.get(choiceX * Constants.MAPSIZE + choiceY);
            if (tile != null && tile.isFloor()) {
                if (tile.isOccupied()) {
                    continue;
                }
                return tile;
            }
        }
    }

    private void findCameraBounds() {
        // Center on player coordinates
        camera.position.set(player.getCurrentX(), player.getCurrentY(), 0);
        // Find camera upper left coordinates
        minCamX = camera.position.x - (camera.viewportWidth / 2) * camera.zoom;
        minCamY = camera.position.y - (camera.viewportHeight / 2) * camera.zoom;
        // Find camera lower right coordinates
        maxCamX = minCamX + camera.viewportWidth * camera.zoom;
        maxCamY = minCamY + camera.viewportHeight * camera.zoom;
        camera.update();
    }

    private boolean inViewport(int x, int y) {
        return ((x + tileSize) >= minCamX && x <= maxCamX && (y + tileSize) >= minCamY && y <= maxCamY);
    }

    public void createTileBodies() {
        this.bodies = new HashMap<Integer, Body>();
        PolygonShape tileShape = new PolygonShape();
        tileShape.setAsBox(tileSize / 2f, tileSize / 2f);
        BodyDef tileBodyDef = new BodyDef();
        tileBodyDef.type = BodyDef.BodyType.StaticBody;
        FixtureDef tileFixture = new FixtureDef();
        tileFixture.shape = tileShape;

        for (Tile tile : tiles.values()) {
            if (tile.isBlocking()) {
                tileFixture.filter.groupIndex = Constants.BIT_GROUP;
            } else {
                tileFixture.filter.groupIndex = -Constants.BIT_GROUP;
            }
            // Body position: center of (tileX * TILESIZE), center of (tileY * TILESIZE)
            tileBodyDef.position.set(tile.x * tileSize + (tileSize / 2f), tile.y * tileSize + (tileSize / 2f));
            Body tileBody = world.createBody(tileBodyDef);
            tileBody.createFixture(tileFixture);
            bodies.put(tile.x * Constants.MAPSIZE + tile.y, tileBody);
        }
        tileShape.dispose();
    }

    public void updateTileBody(int tileX, int tileY) {
        // Get body at object position
        Body updatedBody = bodies.get(tileX * Constants.MAPSIZE + tileY);
        // Destroy current fixture on body
        updatedBody.destroyFixture(updatedBody.getFixtureList().first());

        PolygonShape tileShape = new PolygonShape();
        tileShape.setAsBox(tileSize / 2f, tileSize / 2f);
        FixtureDef tileFixture = new FixtureDef();
        tileFixture.shape = tileShape;

        Tile updated = tiles.get(tileX * Constants.MAPSIZE + tileY);
        if (updated.isBlocking()) {
            tileFixture.filter.groupIndex = Constants.BIT_GROUP;
        } else {
            tileFixture.filter.groupIndex = -Constants.BIT_GROUP;
        }
        updatedBody.createFixture(tileFixture);
        tileShape.dispose();
    }

    public void dispose() {
        world.dispose();
        rayHandler.dispose();
    }
}
