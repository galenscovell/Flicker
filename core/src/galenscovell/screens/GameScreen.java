package galenscovell.screens;

import galenscovell.entities.Player;
import galenscovell.flicker.FlickerMain;
import galenscovell.logic.Renderer;
import galenscovell.logic.Updater;
import galenscovell.logic.Level;
import galenscovell.util.GestureHandler;
import galenscovell.util.InputHandler;
import galenscovell.util.PlayerParser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.input.GestureDetector;

/**
 * GAME SCREEN
 * Main gameplay screen.
 * createNewLevel(int rows, int columns) is called for each level.
 *
 * @author Galen Scovell
 */

public class GameScreen implements Screen {
    private FlickerMain root;
    private Player playerInstance;
    private HudDisplay hud;
    private InputMultiplexer fullInput;
    private Renderer renderer;
    private Updater updater;

    private boolean tileSelection, moving, up, down, left, right;
    private double interpolation;

    private final int timestep = 12;
    private int[] move = new int[2];
    private int accumulator = 0;

    public GameScreen(FlickerMain root, String classType) {
        // GLProfiler.enable();
        this.root = root;
        PlayerParser playerParser = new PlayerParser();
        this.playerInstance = playerParser.pullClassStats(classType);
        this.hud = new HudDisplay(this, playerInstance);

        this.fullInput = new InputMultiplexer();
        fullInput.addProcessor(hud.stage);
        fullInput.addProcessor(new GestureDetector(new GestureHandler(this)));
        fullInput.addProcessor(new InputHandler(this));
        Gdx.input.setInputProcessor(fullInput);

        createNewLevel(10, 10);
    }

    @Override
    public void render(float delta) {
        if (!moving || accumulator > timestep) {
            update();
            accumulator = 0;
        }
        if (updater.descend()) {
            this.renderer = null;
            this.updater = null;
            System.gc(); // Suggest garbage collection on null references
            createNewLevel(10, 10);
        }

        interpolation = (double) accumulator / timestep;
        renderer.render(interpolation, moving);
        accumulator++;
        // System.out.println("Draw calls: " + GLProfiler.drawCalls + ", Texture binds: " + GLProfiler.textureBindings);
        // GLProfiler.reset();
    }

    public void screenZoom(boolean zoomOut, boolean touchScreen) {
        float value = 0.25f;
        if (touchScreen) {
            value /= 16;
        } else {
            value /= 2;
        }
        if (zoomOut) {
            renderer.zoom(value);
        } else {
            renderer.zoom(-value);
        }
    }

    public void screenPan(float dx, float dy) {
        renderer.pan(dx, dy);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    public void toMainMenu() {
        root.setScreen(root.mainMenuScreen);
    }

    public void disableWorldInput() {
        Gdx.input.setInputProcessor(hud.stage);
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

    @Override
    public void show() {
        enableWorldInput();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        hud.dispose();
        this.dispose();
    }

    public void setMovement(int x, int y) {
        left = (x == -1);
        right = (x == 1);
        up = (y == -1);
        down = (y == 1);
    }

    private void update() {
        move[0] = left ? -1 : right ? 1 : 0;
        move[1] = up ? -1 : down ? 1 : 0;
        if (move[0] == 0 && move[1] == 0) {
            moving = false;
        } else {
            moving = true;
            updater.move(move, renderer.getEntityList(), renderer.getInanimateList());
        }
    }

    private void createNewLevel(int rows, int columns) {
        Level level = new Level(rows, columns);
        // Modify smoothing passes here
        for (int i = 0; i < 6; i++) {
            level.update();
        }
        level.optimizeLayout();
        this.renderer = new Renderer(level.getTiles(), 32);
        this.updater = new Updater(level.getTiles(), 32);
        renderer.assembleLevel(playerInstance, rows, columns);
        updater.setPlayer(playerInstance);
        updater.setStairs(renderer.getInanimateList());
        renderer.setHud(hud);
        updater.setHud(hud);
    }
}
