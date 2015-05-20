
/**
 * GAMESCREEN CLASS
 * Primary screen in which main gameplay occurs.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
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


public class GameScreen implements Screen {
    private FlickerMain main;
    private FPSLogger logger;
    private Player playerInstance;
    private HudDisplay hud;
    private World world;
    private Renderer renderer;
    private Updater updater;

    private boolean acting;
    private double interpolation;
    private int accumulator = 0;


    public GameScreen(FlickerMain main) {
        // GLProfiler.enable();
        this.main = main;
        this.logger = new FPSLogger();
        this.playerInstance = new Player();
        this.hud = new HudDisplay(this);
        createNewLevel();
    }

    public void changeScreen(Screen screen) {
        main.setScreen(screen);
    }

    public void update(int[] move) {
        if (accumulator > Constants.TIMESTEP) {
            updater.update(move, true, acting, renderer.getEntityList(), renderer.getInanimateList());
            accumulator = 0;
        }
    }

    @Override
    public void render(float delta) {
        if (updater.playerDescends()) {
            this.renderer = null;
            this.updater = null;
            this.world = null;
            System.gc(); // Suggest garbage collection on null references
            createNewLevel();
        }

        interpolation = (double) accumulator / Constants.TIMESTEP;
        renderer.render(interpolation);
        accumulator++;


        /****************
         *  DEBUG
         ****************/
        // logger.log();
        // System.out.println("Draws: " + GLProfiler.drawCalls + "\nTexture Bindings: " + GLProfiler.textureBindings + "\nShader switches: " + GLProfiler.shaderSwitches + "\nTotal calls: " + GLProfiler.calls);
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
    }

    @Override
    public void show() {
        InputMultiplexer multipleInputs = new InputMultiplexer();
        multipleInputs.addProcessor(hud.stage);
        multipleInputs.addProcessor(new GestureDetector(new GestureHandler(this)));
        multipleInputs.addProcessor(new InputHandler(this));
        Gdx.input.setInputProcessor(multipleInputs);
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
        this.world = new World();
        this.renderer = new Renderer(world.getTiles());
        this.updater = new Updater(world.getTiles());

        int smoothTicks = Constants.WORLD_SMOOTHING_PASSES;
        while (smoothTicks > 0) {
            world.update();
            smoothTicks--;
        }

        world.optimizeLayout();
        renderer.assembleLevel(playerInstance);
        updater.setPlayer(playerInstance);
        updater.setStairs(renderer.getInanimateList());
        renderer.setHud(hud);
        updater.setHud(hud);
    }
}
