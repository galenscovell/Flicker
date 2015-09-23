package galenscovell.graphics;

import box2dLight.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import galenscovell.processing.Repository;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Lighting {
    private World world;
    private RayHandler rayHandler;
    private PointLight torch;
    private Map<Integer, Body> bodies;
    private Repository repo;
    private int torchFrame;
    // private Box2DDebugRenderer debug;

    public Lighting(Repository repo) {
        this.world = new World(new Vector2(0, 0), true);
        this.rayHandler = new RayHandler(world);
        RayHandler.useDiffuseLight(true);
        rayHandler.setAmbientLight(0, 0.1f, 0.1f, 1);
        this.torch = new PointLight(rayHandler, 40, new Color(0.98f, 0.9f, 0.9f, 1), 27, 0, 0);
        torch.setSoftnessLength(4);
        rayHandler.setCulling(false);
        torch.setContactFilter(Constants.BIT_LIGHT, Constants.BIT_GROUP, Constants.BIT_WALL);
        this.repo = repo;
        this.torchFrame = 0;
        // debug = new Box2DDebugRenderer();
        createTileBodies();
    }

    public void update(float x, float y, OrthographicCamera camera) {
        torch.setPosition(x, y);
        rayHandler.setCombinedMatrix(camera.combined, camera.position.x, camera.position.y, camera.viewportWidth * camera.zoom, camera.viewportHeight * camera.zoom);
        rayHandler.updateAndRender();
        animateTorch();
        // debug.render(world, camera.combined);
    }

    public void animateTorch() {
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

    public void createTileBodies() {
        this.bodies = new HashMap<Integer, Body>();
        PolygonShape tileShape = new PolygonShape();
        tileShape.setAsBox(Constants.TILESIZE / 2f, Constants.TILESIZE / 2f);
        BodyDef tileBodyDef = new BodyDef();
        tileBodyDef.type = BodyDef.BodyType.StaticBody;
        FixtureDef tileFixture = new FixtureDef();
        tileFixture.shape = tileShape;

        for (Tile tile : repo.tiles.values()) {
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
