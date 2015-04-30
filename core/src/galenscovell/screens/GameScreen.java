
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
    private Player player;
    private World world;
    private Renderer renderer;
    private Updater updater;

    private double interpolation;
    private int updateAccumulator;


    public GameScreen() {
        this.player = new Player(48, 48, 48);

        this.world = new World();
        this.renderer = new Renderer(player);
        this.updater = new Updater(player);

        this.updateAccumulator = 0;
    }

    @Override
    public void render(float delta) {
        // Player movement and entity logic
        if (updateAccumulator > Constants.TIMESTEP) {
            updater.update(delta, checkMovement());
            updateAccumulator = 0;
        }

        // Graphics rendering
        interpolation = (double) updateAccumulator / Constants.TIMESTEP;
        renderer.render(interpolation);
        updateAccumulator++;
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

    }

    private int[] checkMovement() {
        int[] direction = new int[2];

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction[1]--;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction[1]++;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction[0]--;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction[0]++;
        }
        return direction;
    }

}
