package galenscovell.screens;

import galenscovell.flicker.FlickerMain;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * LOADING SCREEN
 * Displays loading animation asynchronously while game resources are loading.
 *
 * @author Galen Scovell
 */

public class LoadingScreen extends AbstractScreen {
    private ProgressBar loadingBar;

    public LoadingScreen(FlickerMain root) {
        super(root);
    }

    protected void create() {
        this.stage = new Stage(new FitViewport(400, 240), root.spriteBatch);
        Table loadingMain = new Table();
        loadingMain.setFillParent(true);

        Table labelTable = new Table();
        Image loadingImage = new Image(new Texture(Gdx.files.internal("textures/loading.png")));
        labelTable.add(loadingImage).width(200).height(50).expand().fill().bottom();
        loadingMain.add(labelTable).expand().fill();
        loadingMain.row();

        Table barTable = new Table();
        this.loadingBar = createBar();
        barTable.add(loadingBar).width(200).height(60).expand().fill().top();
        loadingMain.add(barTable).expand().fill();

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
        loadingBar.setValue(ResourceManager.assetManager.getLoadedAssets());
    }

    @Override
    public void show() {
        ResourceManager.create();
        create();
        stage.getRoot().getColor().a = 0;
        stage.getRoot().addAction(Actions.sequence(Actions.fadeIn(0.2f)));
    }

    private ProgressBar createBar() {
        TextureRegionDrawable fill = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("textures/loadingFill.png"))));
        TextureRegionDrawable empty = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("textures/loadingEmpty.png"))));
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(empty, fill);
        ProgressBar bar = new ProgressBar(0, 15, 1, false, barStyle);
        barStyle.knobBefore = fill;
        bar.setValue(0);
        bar.setAnimateDuration(0.1f);
        return bar;
    }

    Action toMainMenuScreen = new Action() {
        public boolean act(float delta) {
            root.setScreen(root.mainMenuScreen);
            return true;
        }
    };
}
