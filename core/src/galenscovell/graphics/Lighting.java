package galenscovell.graphics;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import galenscovell.processing.Repository;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Lighting {
    private final World world;
    private final RayHandler rayHandler;
    private final List<Torch> torches;
    private final Torch playerTorch;
    private Map<Integer, Body> bodies;
    private final Repository repo;
    // private Box2DDebugRenderer debug;

    public Lighting(Repository repo) {
        this.world = new World(new Vector2(0, 0), true);
        this.rayHandler = new RayHandler(this.world);
        RayHandler.useDiffuseLight(true);
        this.rayHandler.setAmbientLight(0, 0.025f, 0.025f, 1);
        this.rayHandler.setCulling(false);
        this.torches = new ArrayList<Torch>();
        this.playerTorch = new Torch(27, this.rayHandler, 0.98f, 0.9f, 0.9f, 1);
        this.repo = repo;
        // debug = new Box2DDebugRenderer();
        this.createTileBodies();
    }

    public void update(float x, float y, OrthographicCamera camera) {
        this.rayHandler.setCombinedMatrix(camera.combined, camera.position.x, camera.position.y, camera.viewportWidth * camera.zoom, camera.viewportHeight * camera.zoom);
        this.rayHandler.updateAndRender();
        this.animateTorches(x, y);
        // debug.render(world, camera.combined);
    }

    public void animateTorches(float x, float y) {
        this.playerTorch.setPosition(x, y);
        this.playerTorch.animate();
        for (Torch torch : this.torches) {
            torch.animate();
        }
    }

    public void placeTorch(int x, int y, int size, float r, float g, float b, float a) {
        Torch newTorch = new Torch(size, this.rayHandler, r, g, b, a);
        newTorch.setPosition(x, y);
        this.torches.add(newTorch);
    }

    public void createTileBodies() {
        this.bodies = new HashMap<Integer, Body>();
        PolygonShape tileShape = new PolygonShape();
        tileShape.setAsBox(Constants.TILESIZE / 2f, Constants.TILESIZE / 2f);
        BodyDef tileBodyDef = new BodyDef();
        tileBodyDef.type = BodyType.StaticBody;
        FixtureDef tileFixture = new FixtureDef();
        tileFixture.shape = tileShape;

        for (Tile tile : this.repo.tiles.values()) {
            if (tile.isBlocking()) {
                tileFixture.filter.groupIndex = Constants.BIT_GROUP;
            } else {
                tileFixture.filter.groupIndex = -Constants.BIT_GROUP;
            }
            // Body position: center of (tileX * TILESIZE), center of (tileY * TILESIZE)
            tileBodyDef.position.set(tile.x * Constants.TILESIZE + Constants.TILESIZE / 2f, tile.y * Constants.TILESIZE + Constants.TILESIZE / 2f);
            Body tileBody = this.world.createBody(tileBodyDef);
            tileBody.createFixture(tileFixture);
            this.bodies.put(tile.x * Constants.MAPSIZE + tile.y, tileBody);
        }
        tileShape.dispose();
    }

    public void updateTileBody(int tileX, int tileY) {
        // Get body at object position
        Body updatedBody = this.bodies.get(tileX * Constants.MAPSIZE + tileY);
        // Destroy current fixture on body
        updatedBody.destroyFixture(updatedBody.getFixtureList().first());

        PolygonShape tileShape = new PolygonShape();
        tileShape.setAsBox(Constants.TILESIZE / 2f, Constants.TILESIZE / 2f);
        FixtureDef tileFixture = new FixtureDef();
        tileFixture.shape = tileShape;

        Tile updated = this.repo.findTile(tileX, tileY);
        if (updated.isBlocking()) {
            tileFixture.filter.groupIndex = Constants.BIT_GROUP;
        } else {
            tileFixture.filter.groupIndex = -Constants.BIT_GROUP;
        }
        updatedBody.createFixture(tileFixture);
        tileShape.dispose();
    }

    public void dispose() {
        this.world.dispose();
        this.rayHandler.dispose();
    }
}
