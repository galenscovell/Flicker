package galenscovell.processing;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import galenscovell.graphics.Fog;
import galenscovell.things.entities.Entity;
import galenscovell.things.entities.Hero;
import galenscovell.things.inanimates.Door;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.util.Constants;
import galenscovell.util.MonsterParser;
import galenscovell.world.Tile;

import java.util.*;

public class Renderer {
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch spriteBatch;

    private int tileSize, torchFrame;
    private float minCamX, minCamY, maxCamX, maxCamY;
    private Map<Integer, Tile> tiles;
    private List<Entity> entities;
    private List<Inanimate> inanimates;
    private Fog fog;
    private Hero hero;

    private RayHandler rayHandler;
    private PointLight torch;
    private World world;
    private Map<Integer, Body> bodies;
    private Box2DDebugRenderer debug;

    public Renderer(Map<Integer, Tile> tiles, SpriteBatch spriteBatch) {
        // Uses custom dimensions (48, 80) rather than exact pixels (480, 800)
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
        rayHandler.setAmbientLight(0, 0.1f, 0.1f, 1);
        this.torch = new PointLight(rayHandler, 40, new Color(0.98f, 0.9f, 0.9f, 1), 27, 0, 0);
        torch.setSoftnessLength(4);
        rayHandler.setCulling(false);
        torch.setContactFilter(Constants.BIT_LIGHT, Constants.BIT_GROUP, Constants.BIT_WALL);
        this.torchFrame = 0;
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
            entity.draw(spriteBatch, tileSize, interpolation, hero);
        }
        // Player rendering: [x, y] are in custom units
        hero.draw(spriteBatch, tileSize, interpolation, null);

        // Background effect rendering
        fog.draw(spriteBatch);
        spriteBatch.end();

        // Center torch on player
        torch.setPosition(hero.getCurrentX() + (tileSize / 2), hero.getCurrentY() + (tileSize / 2));
        rayHandler.setCombinedMatrix(camera.combined, camera.position.x, camera.position.y, camera.viewportWidth * camera.zoom, camera.viewportHeight * camera.zoom);
        rayHandler.updateAndRender();
        animateTorch();
        // debug.render(world, camera.combined);
    }

    private void animateTorch() {
        torchFrame++;
        if (torchFrame == 6) {
            torch.setDistance(26.5f);
        } else if (torchFrame == 12) {
            torch.setDistance(26);
        } else if (torchFrame == 18) {
            torch.setDistance(25.5f);
        } else if (torchFrame == 24) {
            torch.setDistance(26);
        } else if (torchFrame == 30) {
            torch.setDistance(26.5f);
        } else if (torchFrame == 36) {
            torch.setDistance(27);
            torchFrame = 0;
        }
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

    public void pan(float dx, float dy) {
        camera.translate(-dx / (tileSize * 3), -dy / (tileSize * 3), 0);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        centerOnPlayer();
    }

    public void assembleLevel(Hero hero) {
        placeInanimates();
        placePlayer(hero);
        MonsterParser monsterParser = new MonsterParser();
        for (int i = 0; i < 3; i++) {
            placeEntities(monsterParser);
        }
        monsterParser = null;
        createTileBodies();
    }

    private void placeInanimates() {
        // Place doors on floors with hallway bitmask, no adjacent doors, and more than 2 adjacent floor neighbors
        Random random = new Random();
        for (Tile tile : tiles.values()) {
            if (tile.isFloor() && tile.getFloorNeighbors() > 2) {
                if (tile.getBitmask() == 5 && suitableForDoor(tile)) {
                    inanimates.add(new Door(this, tile.x, tile.y, "h"));
                    tile.toggleBlocking();
                    tile.toggleOccupied();
                    tile.toggleDoor();
                } else if (tile.getBitmask() == 10 && suitableForDoor(tile)) {
                    inanimates.add(new Door(this, tile.x, tile.y, "v"));
                    tile.toggleBlocking();
                    tile.toggleOccupied();
                    tile.toggleDoor();
                }
            }
        }
    }

    private boolean suitableForDoor(Tile tile) {
        for (Point p : tile.getNeighbors()) {
            Tile n = tiles.get(p.x * Constants.MAPSIZE + p.y);
            if (n != null && (n.isWater() || n.hasDoor())) {
                return false;
            }
        }
        return true;
    }

    private void placePlayer(Hero heroInstance) {
        Tile randomTile = findRandomTile();
        this.hero = heroInstance;
        hero.setPosition(randomTile.x * tileSize, randomTile.y * tileSize);
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

    private void centerOnPlayer() {
        camera.position.set(hero.getCurrentX(), hero.getCurrentY(), 0);
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
