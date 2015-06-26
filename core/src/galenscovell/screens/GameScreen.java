package galenscovell.screens;

import galenscovell.entities.Player;
import galenscovell.flicker.FlickerMain;
import galenscovell.logic.Renderer;
import galenscovell.logic.Updater;
import galenscovell.logic.Level;
import galenscovell.util.GestureHandler;
import galenscovell.util.PlayerParser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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

    private boolean tileSelection;
    private final int timestep = 12;
    private int accumulator = 0;

    public GameScreen(FlickerMain root, String classType) {
        super(root);
        create(classType);
    }

    protected void create(String classType) {
        // GLProfiler.enable();
        PlayerParser playerParser = new PlayerParser();
        this.playerInstance = playerParser.pullClassStats(classType);
        this.stage = new HudStage(this, playerInstance, root.spriteBatch);
        createNewLevel();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        if (accumulator > timestep) {
            update();
            accumulator = 0;
        }
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
        // Modify level dimensions here
        int rows = 10;
        int columns = 10;
        Level level = new Level(rows, columns);
        // Modify smoothing passes here
        for (int i = 0; i < 6; i++) {
            level.update();
        }
        level.optimize();
        this.renderer = new Renderer(level.getTiles(), root.spriteBatch, 32);
        this.updater = new Updater(playerInstance, level.getTiles(), 32, columns);
        renderer.assembleLevel(playerInstance, rows, columns);
        updater.setStairs(renderer.getInanimateList());
        updater.setHud((HudStage) stage);

        this.fullInput = new InputMultiplexer();
        fullInput.addProcessor(stage);
        fullInput.addProcessor(new GestureDetector(new GestureHandler(this, renderer.getCamera())));
        enableWorldInput();
    }

    private void update() {

    }
}
