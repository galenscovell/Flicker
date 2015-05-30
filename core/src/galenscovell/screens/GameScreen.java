
/**
 * GAMESCREEN CLASS
 * Primary screen in which main gameplay occurs.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.input.GestureDetector;

import galenscovell.entities.Player;
import galenscovell.flicker.FlickerMain;
import galenscovell.logic.Renderer;
import galenscovell.logic.Updater;
import galenscovell.logic.World;
import galenscovell.util.Constants;
import galenscovell.util.GestureHandler;
import galenscovell.util.InputHandler;
import galenscovell.util.PlayerParser;


public class GameScreen implements Screen {
    private FlickerMain main;
    private Player playerInstance;
    private HudDisplay hud;
    private InputMultiplexer fullInput;
    private Renderer renderer;
    private Updater updater;

    private boolean moving, up, down, left, right;
    private int[] move = new int[2];
    private double interpolation;
    private int accumulator = 0;


    public GameScreen(FlickerMain main, String classType) {
        // GLProfiler.enable();
        this.main = main;
        PlayerParser playerParser = new PlayerParser();
        this.playerInstance = playerParser.pullClassStats(classType);
        this.hud = new HudDisplay(this, playerInstance);

        this.fullInput = new InputMultiplexer();
        fullInput.addProcessor(hud.stage);
        fullInput.addProcessor(new GestureDetector(new GestureHandler(this)));
        fullInput.addProcessor(new InputHandler(this));
        Gdx.input.setInputProcessor(fullInput);

        createNewLevel();
    }

    public void setMovement(int x, int y) {
        left = (x == -1);
        right = (x == 1);
        up = (y == -1);
        down = (y == 1);
    }

    public void update() {
        move[0] = left ? -1 : right ? 1 : 0;
        move[1] = up ? -1 : down ? 1 : 0;
        if (move[0] == 0 && move[1] == 0) {
            moving = false;
        } else {
            moving = true;
            updater.move(move, renderer.getEntityList(), renderer.getInanimateList());
        }
    }

    @Override
    public void render(float delta) {
        if (!moving || accumulator > Constants.TIMESTEP) {
            update();
            accumulator = 0;
        }
        if (updater.descend()) {
            this.renderer = null;
            this.updater = null;
            System.gc(); // Suggest garbage collection on null references
            createNewLevel();
        }

        interpolation = (double) accumulator / Constants.TIMESTEP;
        renderer.render(interpolation, moving);
        accumulator++;
        // System.out.println("Draw calls: " + GLProfiler.drawCalls + ", Texture binds: " + GLProfiler.textureBindings);
        // GLProfiler.reset();
    }

    public void screenZoom(boolean zoomOut, boolean touchScreen) {
        float value = 0.25f;
        if (touchScreen) {
            value /= 8;
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
        main.setScreen(main.mainMenuScreen);
    }

    public void disableWorldInput() {
        Gdx.input.setInputProcessor(hud.stage);
    }

    public void enableWorldInput() {
        Gdx.input.setInputProcessor(fullInput);
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

    private void createNewLevel() {
        World world = new World();
        for (int i = 0; i < Constants.WORLD_SMOOTHING_PASSES; i++) {
            world.update();
        }
        world.optimizeLayout();
        this.renderer = new Renderer(world.getTiles());
        this.updater = new Updater(world.getTiles());
        renderer.assembleLevel(playerInstance);
        updater.setPlayer(playerInstance);
        updater.setStairs(renderer.getInanimateList());
        renderer.setHud(hud);
        updater.setHud(hud);
    }
}
