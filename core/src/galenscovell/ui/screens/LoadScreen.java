package galenscovell.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import galenscovell.flicker.FlickerMain;
import galenscovell.util.ResourceManager;

public class LoadScreen extends AbstractScreen {
    private ProgressBar loadingBar;

    public LoadScreen(FlickerMain root) {
        super(root);
    }

    @Override
    public void create() {
        this.stage = new Stage(new FitViewport(240, 400), root.spriteBatch);
        Table loadingMain = new Table();
        loadingMain.setFillParent(true);

        Table labelTable = new Table();
        Image loadingImage = new Image(new Texture(Gdx.files.internal("textures/loading.png")));
        labelTable.add(loadingImage).width(200).height(50).expand().fill();
        loadingMain.add(labelTable).expand().fill();

        stage.addActor(loadingMain);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        if (ResourceManager.assetManager.update()) {
            ResourceManager.done();
            stage.getRoot().addAction(Actions.sequence(Actions.fadeOut(0.4f), toMainMenuScreen));
        }
    }

    @Override
    public void show() {
        ResourceManager.create();
        create();
        stage.getRoot().getColor().a = 0;
        stage.getRoot().addAction(Actions.sequence(Actions.fadeIn(0.4f)));
    }

    Action toMainMenuScreen = new Action() {
        public boolean act(float delta) {
            root.setScreen(root.mainMenuScreen);
            return true;
        }
    };
}
