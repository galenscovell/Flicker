package galenscovell.screens;

import box2dLight.RayHandler;

import galenscovell.entities.Player;
import galenscovell.flicker.FlickerMain;
import galenscovell.logic.Renderer;
import galenscovell.logic.Updater;
import galenscovell.logic.Level;
import galenscovell.util.Constants;
import galenscovell.util.GestureHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * GAME SCREEN
 * Main gameplay screen.
 * Has both a HUD (stage) and world (renderer). Updater handles logic.
 *
 * @author Galen Scovell
 */

public class GameScreen extends AbstractScreen {
    private Player player;
    private Renderer renderer;
    private Updater updater;
    private InputMultiplexer fullInput;

    private World world;
    private RayHandler rayHandler;

    private boolean examineMode;
    private final int timestep = 20;
    private int accumulator = 0;
    private int[] destination;

    public GameScreen(FlickerMain root) {
        super(root);
        create();
    }

    protected void create() {
        // GLProfiler.enable();
        this.player = new Player();
        this.stage = new HudStage(this, player, root.spriteBatch);
        createNewLevel();
    }

    public void update(float delta) {
        if (accumulator > timestep) {
            accumulator = 0;
            if (!(destination[0] == (player.getX() / Constants.TILESIZE) && destination[1] == (player.getY() / Constants.TILESIZE))) {
                updater.move(destination, renderer.getEntityList(), renderer.getInanimateList());
                destination[0] = player.getX() / Constants.TILESIZE;
                destination[1] = player.getY() / Constants.TILESIZE;
            }
            if (updater.descend()) {
                rayHandler.dispose();
                world.dispose();
                createNewLevel();
            }
        }
        world.step(delta, 6, 2);
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render((double) accumulator / timestep);
        stage.draw();
        accumulator++;
        // System.out.println("Draw calls: " + GLProfiler.drawCalls + ", Texture binds: " + GLProfiler.textureBindings);
        // GLProfiler.reset();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        enableWorldInput();
    }

    @Override
    public void dispose() {
        world.dispose();
        rayHandler.dispose();
        stage.dispose();
    }

    public void playerMove(float x, float y) {
        destination[0] = (int) x;
        destination[1] = (int) y;
    }

    public void screenZoom(boolean zoomOut) {
        if (zoomOut) {
            renderer.zoom(0.02f);
        } else {
            renderer.zoom(-0.02f);
        }
    }

    public void toMainMenu() {
        root.setScreen(root.mainMenuScreen);
    }

    public void disableWorldInput() {
        Gdx.input.setInputProcessor(stage);
    }

    public void enableWorldInput() {
        Gdx.input.setInputProcessor(fullInput);
    }

    public boolean examineMode() {
        return examineMode;
    }

    public void toggleExamineMode() {
        examineMode = !examineMode;
    }

    public void findTile(float x, float y) {
        updater.getTile(x, y);
    }

    private void createNewLevel() {
        this.world = new World(new Vector2(0, 0), true);
        this.rayHandler = new RayHandler(world);

        Level level = new Level();
        // Modify smoothing passes here
        for (int i = 0; i < 6; i++) {
            level.update();
        }
        level.optimize();
        this.renderer = new Renderer(world, rayHandler, level.getTiles(), root.spriteBatch);
        this.updater = new Updater(player, level.getTiles());
        renderer.assembleLevel(player);
        renderer.createTileBodies();
        updater.setStairs(renderer.getInanimateList());
        updater.setHud((HudStage) stage);

        this.fullInput = new InputMultiplexer();
        fullInput.addProcessor(stage);
        fullInput.addProcessor(new GestureDetector(20, 0.4f, 0.1f, 0.15f, new GestureHandler(this, renderer.getCamera())));
        enableWorldInput();
        // Set initial movement destination to player starting position
        this.destination = new int[]{player.getX() / Constants.TILESIZE, player.getY() / Constants.TILESIZE};
    }
}
