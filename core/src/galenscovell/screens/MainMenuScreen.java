
/**
 * MAINMENUSCREEN CLASS
 * Opening screen. Contains New Game, Continue Game, Options and Quit.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import galenscovell.flicker.FlickerMain;


public class MainMenuScreen implements Screen {
    private FlickerMain main;
    private Stage stage;
    private TextureAtlas uiAtlas;
    private float width = Gdx.graphics.getWidth();
    private float height = Gdx.graphics.getHeight();


    public MainMenuScreen(FlickerMain main){
        this.main = main;
        create();
    }

    public void create() {
        this.stage = new Stage();
        this.uiAtlas = new TextureAtlas(Gdx.files.internal("ui/uiAtlas.pack"));

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/SDS_8x8.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;
        BitmapFont customFont = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();

        TextureRegionDrawable hudBg = new TextureRegionDrawable(uiAtlas.findRegion("hudbg"));
        TextureRegionDrawable buttonDown = new TextureRegionDrawable(uiAtlas.findRegion("buttonDown"));
        TextButton.TextButtonStyle customStyle = new TextButton.TextButtonStyle(hudBg, buttonDown, hudBg, customFont);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        /**********************************
         * TOP TABLE                      *
         **********************************/
        Table topTable = new Table();
        mainTable.add(topTable).height(height / 4).expand().fill().center();
        mainTable.row();


        /**********************************
         * BOTTOM TABLE                   *
         **********************************/
        Table bottomTable = new Table();

        TextButton newGameButton = new TextButton("NEW GAME", customStyle);
        newGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(main.gameScreen);
            }
        });
        TextButton continueButton = new TextButton("CONTINUE GAME", customStyle);
        continueButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Continue game pressed.");
            }
        });
        TextButton optionsButton = new TextButton("OPTIONS", customStyle);
        TextButton quitButton = new TextButton("QUIT", customStyle);

        bottomTable.add(newGameButton).width(width / 3).height(height / 8).expand().fill();
        bottomTable.row();
        bottomTable.add(continueButton).width(width / 3).height(height / 8).expand().fill();
        bottomTable.row();
        bottomTable.add(optionsButton).width(width / 3).height(height / 8).expand().fill();
        bottomTable.row();
        bottomTable.add(quitButton).width(width / 3).height(height / 8).expand().fill();
        bottomTable.row();
        mainTable.add(bottomTable).expand().fill();

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
}
