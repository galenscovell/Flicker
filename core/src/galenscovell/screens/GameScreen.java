
/**
 * GAMESCREEN CLASS
 * Handles game loop calls to updater/renderer and player input.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;

import galenscovell.entities.Player;

import galenscovell.logic.Renderer;
import galenscovell.logic.Updater;
import galenscovell.logic.World;

import galenscovell.util.Constants;
import galenscovell.util.InputHandler;


public class GameScreen implements Screen {
    private Player playerInstance;
    private HudDisplay hud;
    private World world;
    private Renderer renderer;
    private Updater updater;

    private boolean moving;
    private boolean acting;
    private double interpolation;
    private int accumulator = 0;


    public GameScreen() {
        this.playerInstance = new Player();
        this.hud = new HudDisplay();
        createNewLevel();
        InputHandler worldInput = new InputHandler(this);
        InputMultiplexer multipleInputs = new InputMultiplexer();
        multipleInputs.addProcessor(hud.stage);
        multipleInputs.addProcessor(worldInput);
        Gdx.input.setInputProcessor(multipleInputs);
    }

    public void update(int[] move) {
        if (!moving && !acting || accumulator > Constants.TIMESTEP) {
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

        // Graphics rendering
        interpolation = (double) accumulator / Constants.TIMESTEP;
        renderer.render(interpolation);
        accumulator++;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
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
