package galenscovell.ui.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import galenscovell.flicker.FlickerMain;

public class AbstractScreen implements Screen {
    protected FlickerMain root;
    protected Stage stage;

    public AbstractScreen(FlickerMain root) {
        this.root = root;
    }

    public void create() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.act(delta);
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (this.stage != null) {
            this.stage.getViewport().update(width, height);
        }
    }

    @Override
    public void show() {
        this.create();
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        if (this.stage != null) {
            this.stage.dispose();
        }
    }
}
