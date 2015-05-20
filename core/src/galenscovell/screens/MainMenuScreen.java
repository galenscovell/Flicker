
/**
 * MAINMENUSCREEN CLASS
 * Opening screen. Contains New Game, Continue Game, Options, Quit and version info.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import galenscovell.flicker.FlickerMain;


public class MainMenuScreen implements Screen {
    private FlickerMain main;
    private Stage stage;
    private TextureAtlas uiAtlas;
    private float width = Gdx.graphics.getWidth();
    private float height = Gdx.graphics.getHeight();
    private boolean newDetailsShown, continueDetailsShown, optionsShown, quitShown;


    public MainMenuScreen(FlickerMain main){
        this.main = main;
        create();
    }

    public void create() {
        this.stage = new Stage();
        this.uiAtlas = new TextureAtlas(Gdx.files.internal("ui/uiAtlas.pack"));

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 8;
        BitmapFont detailFont = fontGenerator.generateFont(parameter);
        parameter.size = 14;
        BitmapFont buttonFont = fontGenerator.generateFont(parameter);
        parameter.size = 48;
        BitmapFont titleFont = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();

        Label.LabelStyle detailStyle = new Label.LabelStyle(detailFont, Color.WHITE);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        TextureRegionDrawable hudBg = new TextureRegionDrawable(uiAtlas.findRegion("hudbg"));
        TextureRegionDrawable buttonDown = new TextureRegionDrawable(uiAtlas.findRegion("buttonDown"));
        TextButton.TextButtonStyle customStyle = new TextButton.TextButtonStyle(hudBg, buttonDown, hudBg, buttonFont);

        Table mainTable = new Table();
        mainTable.padBottom(4);
        mainTable.setFillParent(true);
        // mainTable.setDebug(true);

        /**********************************
         * TOP TABLE                      *
         **********************************/
        Table topTable = new Table();
        Label titleLabel = new Label("FLICKER", titleStyle);
        titleLabel.setAlignment(Align.center, Align.center);
        topTable.add(titleLabel).width(width / 2).expand().fill();
        mainTable.add(topTable).height(height / 4).expand().fill().center();
        mainTable.row();


        /**********************************
         * BOTTOM TABLE                   *
         **********************************/
        final Table bottomTable = new Table();
        // bottomTable.setDebug(true);

        TextButton newGameButton = new TextButton("NEW GAME", customStyle);
        newGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                stage.getRoot().addAction(Actions.sequence(Actions.fadeOut(1.0f), switchScreen));
            }
        });
        TextButton continueButton = new TextButton("CONTINUE GAME", customStyle);
        continueButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        TextButton optionsButton = new TextButton("OPTIONS", customStyle);
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        TextButton quitButton = new TextButton("QUIT", customStyle);
        quitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                stage.getRoot().addAction(Actions.sequence(Actions.fadeOut(1.0f), quitGame));
            }
        });

        bottomTable.add(newGameButton).width(width / 3).height(height / 8).expand().fill();
        bottomTable.row();
        bottomTable.add(continueButton).width(width / 3).height(height / 8).expand().fill();
        bottomTable.row();
        bottomTable.add(optionsButton).width(width / 3).height(height / 8).expand().fill();
        bottomTable.row();
        bottomTable.add(quitButton).width(width / 3).height(height / 8).expand().fill();
        bottomTable.row();
        mainTable.add(bottomTable).expand().fill();
        mainTable.row();

        Label detailLabel = new Label("Flicker v0.1a \u00a9 2015, Galen Scovell", detailStyle);
        detailLabel.setAlignment(Align.bottom);
        mainTable.add(detailLabel).width(width / 6).expand().fill();
        mainTable.pack();
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        stage.getRoot().getColor().a = 0;
        stage.getRoot().addAction(Actions.fadeIn(1.0f));
        Gdx.input.setInputProcessor(stage);
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
        stage.dispose();
        uiAtlas.dispose();
    }

    Action switchScreen = new Action() {
        public boolean act(float delta) {
            main.setScreen(main.gameScreen);
            return true;
        }
    };

    Action quitGame = new Action() {
        public boolean act(float delta) {
            Gdx.app.exit();
            return true;
        }
    };
}
