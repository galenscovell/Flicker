package galenscovell.screens;

import galenscovell.entities.Player;
import galenscovell.flicker.FlickerMain;
import galenscovell.logic.Renderer;
import galenscovell.logic.Updater;
import galenscovell.logic.Level;
import galenscovell.util.Constants;
import galenscovell.util.GestureHandler;
import galenscovell.util.PlayerParser;

import box2dLight.RayHandler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;

/**
 * GAME SCREEN
 * Main gameplay screen. Has both a HUD (stage) and world (renderer). Updater handles logic.
 *
 * @author Galen Scovell
 */

public class GameScreen extends AbstractScreen {
    private Player playerInstance;
    private Renderer renderer;
    private Updater updater;
    private InputMultiplexer fullInput;

    private World world;
    private RayHandler rayHandler;

    private boolean tileSelection;
    private final int timestep = 10;
    private int accumulator = 0;
    private int[] move;

    public GameScreen(FlickerMain root, String classType) {
        super(root);
        create(classType);
    }

    protected void create(String classType) {
        // GLProfiler.enable();
        PlayerParser playerParser = new PlayerParser();
        this.playerInstance = playerParser.pullClassStats(classType);
        this.stage = new HudStage(this, playerInstance, root.spriteBatch);
        this.world = new World(new Vector2(0, 0), true);
        this.rayHandler = new RayHandler(world);
        createNewLevel();
        this.move = new int[2];
    }

    @Override
    public void render(float delta) {
        world.step(delta, 6, 2);
        stage.act(delta);
        if (accumulator > timestep) {
            if (move[0] != 0 || move[1] != 0) {
                update(move);
                move[0] = 0;
                move[1] = 0;
            }
            accumulator = 0;
        }

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
        float diffX = x - playerInstance.getCurrentX();
        float diffY = y - playerInstance.getCurrentY();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (diffX > 0) {
                move[0] = 1;
            } else {
                move[0] = -1;
            }
        } else {
            if (diffY > 0) {
                move[1] = 1;
            } else {
                move[1] = -1;
            }
        }
    }

    public void screenZoom(boolean zoomOut) {
        float value = 0.015625f;
        if (zoomOut) {
            renderer.zoom(value);
        } else {
            renderer.zoom(-value);
        }
    }

    public void screenPan(float dx, float dy) {
        renderer.pan(dx, dy);
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

    public boolean tileSelection() {
        return tileSelection;
    }

    public void toggleTileSelection() {
        tileSelection = !tileSelection;
    }

    public void findTile(float x, float y) {
        renderer.getTile(x, y);
    }

    private void createNewLevel() {
        Level level = new Level();
        // Modify smoothing passes here
        for (int i = 0; i < 6; i++) {
            level.update();
        }
        level.optimize();
        this.renderer = new Renderer(world, rayHandler, level.getTiles(), root.spriteBatch);
        this.updater = new Updater(playerInstance, level.getTiles());
        renderer.assembleLevel(playerInstance);
        updater.setStairs(renderer.getInanimateList());
        updater.setHud((HudStage) stage);

        this.fullInput = new InputMultiplexer();
        fullInput.addProcessor(stage);
        fullInput.addProcessor(new GestureDetector(new GestureHandler(this, renderer.getCamera())));
        enableWorldInput();
    }

    private void update(int[] move) {
        updater.move(move, null, null);
    }
}
