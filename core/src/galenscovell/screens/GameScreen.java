
/**
 * GAMESCREEN CLASS
 *
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Screen;

import galenscovell.logic.Renderer;
import galenscovell.logic.Updater;
import galenscovell.logic.World;


public class GameScreen implements Screen {
    private World world;
    private Renderer renderer;
    private Updater updater;


    public GameScreen() {
        this.world = new World();
        this.renderer = new Renderer(world.getObjects());
        this.updater = new Updater(world.getObjects());
    }

    @Override
    public void render(float delta) {
        updater.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GameScreen", "resizing");
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show called");
    }

    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "hide called");
    }
    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "pause called");
    }
    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume called");
    }
    @Override
    public void dispose() {

    }
}
