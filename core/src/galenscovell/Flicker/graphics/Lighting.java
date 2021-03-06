package galenscovell.flicker.graphics;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import galenscovell.flicker.processing.Repository;
import galenscovell.flicker.util.Constants;
import galenscovell.flicker.world.Tile;

import java.util.*;

public class Lighting {
    private final World world;
    private final RayHandler rayHandler;
    private final Torch playerTorch;
    private final Repository repo;
    // private final Box2DDebugRenderer debug;
    private List<Torch> torches;
    private Map<Integer, Body> bodies;

    public Lighting(Repository repo) {
        this.world = new World(new Vector2(0, 0), true);
        this.rayHandler = new RayHandler(world);
        RayHandler.useDiffuseLight(true);
        rayHandler.setAmbientLight(0, 0.025f, 0.025f, 1);
        rayHandler.setCulling(true);
        this.torches = new ArrayList<Torch>();
        this.playerTorch = new Torch(36, rayHandler, 0.98f, 0.85f, 0.85f, 1);
        this.repo = repo;
        // this.debug = new Box2DDebugRenderer();
        createTileBodies();
    }

    public boolean isInLight(float x, float y) {
        rayHandler.updateAndRender();
        return rayHandler.pointAtLight(x, y);
    }

    public void update(float x, float y, OrthographicCamera camera) {
        rayHandler.setCombinedMatrix(camera.combined, camera.position.x, camera.position.y, camera.viewportWidth * camera.zoom, camera.viewportHeight * camera.zoom);
        rayHandler.updateAndRender();
        animateTorches(x, y);
        // debug.render(world, camera.combined);
    }

    public void animateTorches(float x, float y) {
        playerTorch.setPosition(x, y);
        playerTorch.animate();
        for (Torch torch : torches) {
            torch.animate();
        }
    }

    public void placeTorch(int x, int y, int size, float r, float g, float b, float a) {
        Torch newTorch = new Torch(size, rayHandler, r, g, b, a);
        newTorch.setPosition(x, y);
        torches.add(newTorch);
    }

    public void createTileBodies() {
        this.bodies = new HashMap<Integer, Body>();
        PolygonShape tileShape = new PolygonShape();
        tileShape.setAsBox(Constants.TILESIZE / 2f, Constants.TILESIZE / 2f);
        BodyDef tileBodyDef = new BodyDef();
        tileBodyDef.type = BodyDef.BodyType.StaticBody;
        FixtureDef tileFixture = new FixtureDef();
        tileFixture.shape = tileShape;

        for (Tile tile : repo.getTiles().values()) {
            if (tile.isBlocking()) {
                tileFixture.filter.groupIndex = Constants.BIT_GROUP;
            } else {
                tileFixture.filter.groupIndex = -Constants.BIT_GROUP;
            }
            // Body position: center of (tileX * TILESIZE), center of (tileY * TILESIZE)
            tileBodyDef.position.set(tile.x * Constants.TILESIZE + (Constants.TILESIZE / 2f), tile.y * Constants.TILESIZE + (Constants.TILESIZE / 2f));
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
        tileShape.setAsBox(Constants.TILESIZE / 2f, Constants.TILESIZE / 2f);
        FixtureDef tileFixture = new FixtureDef();
        tileFixture.shape = tileShape;

        Tile updated = repo.findTile(tileX, tileY);
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
