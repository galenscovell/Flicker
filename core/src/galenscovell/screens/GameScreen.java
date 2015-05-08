
/**
 * GAMESCREEN CLASS
 * Handles game loop calls to updater/renderer and player input.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

import galenscovell.entities.Player;

import galenscovell.logic.Renderer;
import galenscovell.logic.Updater;
import galenscovell.logic.World;

import galenscovell.util.Constants;


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
    }

    @Override
    public void render(float delta) {
        // Player movement and entity logic
        // Scan for input at framerate until input found, then switch to logic updates
        if (!moving && !acting) {
            updater.update(checkMovement(), moving, acting, renderer.getEntityList(), renderer.getInanimateList());
            accumulator = 0;
        } else if (accumulator > Constants.TIMESTEP) {
            updater.update(checkMovement(), moving, acting, renderer.getEntityList(), renderer.getInanimateList());
            accumulator = 0;
        }

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

    private int[] checkMovement() {
        int[] playerInput = new int[3];

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerInput[1]--;
            moving = true;
            acting = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerInput[1]++;
            moving = true;
            acting = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerInput[0]--;
            moving = true;
            acting = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerInput[0]++;
            moving = true;
            acting = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            playerInput[2]++;
            moving = false;
            acting = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            playerInput[2]--;
            moving = false;
            acting = true;
        } else {
            moving = false;
            acting = false;
        }
        return playerInput;
    }
}
