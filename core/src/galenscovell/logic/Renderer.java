package galenscovell.logic;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import galenscovell.entities.Entity;
import galenscovell.entities.Player;
import galenscovell.graphics.Fog;
import galenscovell.inanimates.Boulder;
import galenscovell.inanimates.Inanimate;
import galenscovell.util.Constants;
import galenscovell.util.MonsterParser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.*;

/**
 * RENDERER
 * Handles game graphics.
 * Renderer uses custom units (400, 240) rather than pixel dimensions.
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
    private Map<Integer, Body> bodies;
    private Box2DDebugRenderer debug;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch spriteBatch;

    private int tileSize, torchFrames;
    private float minCamX, minCamY, maxCamX, maxCamY;

    public Renderer(World world, RayHandler rayHandler, Map<Integer, Tile> tiles, SpriteBatch spriteBatch) {
        this.tileSize = Constants.TILESIZE;
        this.camera = new OrthographicCamera(Constants.SCREEN_X, Constants.SCREEN_Y);
        this.viewport = new FitViewport(Constants.SCREEN_X, Constants.SCREEN_Y, camera);
        camera.setToOrtho(true, Constants.SCREEN_X, Constants.SCREEN_Y);

        this.tiles = tiles;
        this.world = world;
        this.spriteBatch = spriteBatch;
        this.entities = new ArrayList<Entity>();
        this.inanimates = new ArrayList<Inanimate>();
        this.fog = new Fog();

        this.rayHandler = rayHandler;
        RayHandler.useDiffuseLight(true);
        rayHandler.setAmbientLight(0.0f, 0.0f, 0.0f, 1.0f);
        this.torch = new PointLight(rayHandler, 60, new Color(0.95f, 0.9f, 0.9f, 0.9f), Constants.TILESIZE * 8, 0, 0);
        torch.setSoftnessLength(Constants.TILESIZE * 1.5f);
        torch.setContactFilter(Constants.BIT_LIGHT, Constants.BIT_GROUP, Constants.BIT_WALL);
        this.torchFrames = 30;

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
        fog.render(spriteBatch);
        spriteBatch.end();

        // Set torch position centered on player
        torch.setPosition(player.getCurrentX() + (tileSize / 2), player.getCurrentY() + (tileSize / 2));
        rayHandler.setCombinedMatrix(camera.combined, camera.position.x, camera.position.y, camera.viewportWidth * camera.zoom, camera.viewportHeight * camera.zoom);
        rayHandler.updateAndRender();
        // Torch 'flicker' effect
        if (torchFrames == 15) {
            torch.setColor(0.95f, 0.9f, 0.9f, 0.85f);
        } else if (torchFrames == 0) {
            torch.setColor(0.95f, 0.9f, 0.9f, 0.9f);
            torchFrames = 30;
        }
        torchFrames--;

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

    public void zoom(float value) {
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
    }

    private void placeInanimates() {
        for (Tile tile : tiles.values()) {
            if (tile.isFloor() && tile.getFloorNeighbors() > 2) {
                if (tile.getBitmask() == 1010 || tile.getBitmask() == 101) {
                    inanimates.add(new Boulder(tile.x, tile.y));
                    tile.toggleBlocking();
                    tile.toggleOccupied();
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
            int choiceY = random.nextInt(Constants.ROWS);
            int choiceX = random.nextInt(Constants.COLUMNS);
            Tile tile = tiles.get(choiceX * Constants.COLUMNS + choiceY);
            if (tile != null && tile.isFloor()) {
                if (tile.isOccupied()) {
                    continue;
                }
                return tile;
            }
        }
    }

    private void findCameraBounds() {
        centerOnPlayer();
        minCamX = camera.position.x - (camera.viewportWidth / 2) * camera.zoom;
        minCamY = camera.position.y - (camera.viewportHeight / 2) * camera.zoom;
        maxCamX = minCamX + camera.viewportWidth * camera.zoom;
        maxCamY = minCamY + camera.viewportHeight * camera.zoom;
        camera.update();
    }

    private void centerOnPlayer() {
        camera.position.set(player.getCurrentX(), player.getCurrentY(), 0);
    }

    private boolean inViewport(int x, int y) {
        return ((x + tileSize) >= minCamX && x <= maxCamX && (y + tileSize) >= minCamY && y <= maxCamY);
    }

    public void createTileBodies() {
        this.bodies = new HashMap<Integer, Body>();
        PolygonShape tileShape = new PolygonShape();
        tileShape.setAsBox(Constants.TILESIZE / 2f, Constants.TILESIZE / 2f);
        BodyDef tileBodyDef = new BodyDef();
        tileBodyDef.type = BodyDef.BodyType.StaticBody;
        FixtureDef tileFixture = new FixtureDef();
        tileFixture.shape = tileShape;

        for (Tile tile : tiles.values()) {
            if (tile.isBlocking()) {
                tileFixture.filter.groupIndex = Constants.BIT_GROUP;
            } else {
                continue;
                // Set body to ignore all collisions
                // tileFixture.filter.groupIndex = -Constants.BIT_GROUP;
            }
            // Body position: center of (tileX * TILESIZE), center of (tileY * TILESIZE)
            tileBodyDef.position.set(tile.x * Constants.TILESIZE + (Constants.TILESIZE / 2f), tile.y * Constants.TILESIZE + (Constants.TILESIZE / 2f));
            Body tileBody = world.createBody(tileBodyDef);
            tileBody.createFixture(tileFixture);
            bodies.put(tile.x * Constants.COLUMNS + tile.y, tileBody);
        }
        tileShape.dispose();
    }

    public void updateTileBody(float x, float y) {
        int tileX = (int) x / tileSize;
        int tileY = (int) y / tileSize;
        // Get body at object position
        Body updatedBody = bodies.get(tileX * Constants.COLUMNS + tileY);
        // Destory current fixture on body
        updatedBody.destroyFixture(updatedBody.getFixtureList().first());

        PolygonShape tileShape = new PolygonShape();
        tileShape.setAsBox(Constants.TILESIZE / 2f, Constants.TILESIZE / 2f);
        FixtureDef tileFixture = new FixtureDef();
        tileFixture.shape = tileShape;

        Tile updated = tiles.get(tileX * Constants.COLUMNS + tileY);
        if (updated.isBlocking()) {
            tileFixture.filter.groupIndex = Constants.BIT_GROUP;
        } else {
            tileFixture.filter.groupIndex = -Constants.BIT_GROUP;
        }
        updatedBody.createFixture(tileFixture);
        tileShape.dispose();
    }
}
