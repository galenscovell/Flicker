
/**
 * MAINMENUSCREEN CLASS
 * Opening screen. Contains New Game, Continue Game, Options, Quit and version info.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import galenscovell.flicker.FlickerMain;
import galenscovell.util.Constants;
import galenscovell.util.ResourceManager;


public class MainMenuScreen implements Screen {
    private FlickerMain main;
    private Stage stage;
    private NewGameMenu newGameMenu;
    private TextButton newGameButton, continueButton, optionsButton, quitButton;
    private String selectedClass;


    public MainMenuScreen(FlickerMain main){
        this.main = main;
        create();
    }

    public void create() {
        this.stage = new Stage(new FitViewport((float) Constants.WINDOW_X, (float) Constants.WINDOW_Y));
        this.newGameMenu = new NewGameMenu(this);

        Table mainTable = new Table();
        mainTable.padBottom(4);
        mainTable.setFillParent(true);

        /**********************************
         * TOP TABLE                      *
         **********************************/
        Table topTable = new Table();
        Label titleLabel = new Label("FLICKER", ResourceManager.titleStyle);
        titleLabel.setAlignment(Align.center, Align.center);
        topTable.add(titleLabel).width(400).expand().fill();
        mainTable.add(topTable).height(120).expand().fill().center();
        mainTable.row();


        /**********************************
         * BOTTOM TABLE                   *
         **********************************/
        Table bottomTable = new Table();

        this.newGameButton = new TextButton("New Game", ResourceManager.colorButtonStyle);
        newGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(newGameMenu, 1);
            }
        });
        this.continueButton = new TextButton("Continue Game", ResourceManager.colorButtonStyle);
        continueButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                stage.getRoot().addAction(Actions.sequence(Actions.fadeOut(1.0f), toContinuedGameScreen));
            }
        });
        this.optionsButton = new TextButton("Settings", ResourceManager.colorButtonStyle);
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                stage.getRoot().addAction(Actions.sequence(Actions.fadeOut(0.5f), toOptionScreen));
            }
        });
        this.quitButton = new TextButton("Quit Game", ResourceManager.colorButtonStyle);
        quitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                stage.getRoot().addAction(Actions.sequence(Actions.fadeOut(0.5f), quitGame));
            }
        });

        bottomTable.add(newGameButton).width(250).height(60).expand().fill();
        bottomTable.row();
        bottomTable.add(continueButton).width(250).height(60).expand().fill();
        bottomTable.row();
        bottomTable.add(optionsButton).width(250).height(60).expand().fill();
        bottomTable.row();
        bottomTable.add(quitButton).width(250).height(60).expand().fill();
        bottomTable.row();
        mainTable.add(bottomTable).expand().fill();
        mainTable.row();

        Label detailLabel = new Label("Flicker v0.1a \u00a9 2015, Galen Scovell", ResourceManager.detailStyle);
        detailLabel.setAlignment(Align.bottom);
        mainTable.add(detailLabel).width(150).expand().fill();
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
        stage.getViewport().update(width, height, true);
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
    }

    public void menuOperation(Table menu, int toggle) {
        if (toggle == 0) {
            menu.remove();
            newGameButton.setTouchable(Touchable.enabled);
            continueButton.setTouchable(Touchable.enabled);
            optionsButton.setTouchable(Touchable.enabled);
            quitButton.setTouchable(Touchable.enabled);
        } else {
            stage.addActor(menu);
            newGameButton.setTouchable(Touchable.disabled);
            continueButton.setTouchable(Touchable.disabled);
            optionsButton.setTouchable(Touchable.disabled);
            quitButton.setTouchable(Touchable.disabled);
        }
    }

    public void startGame(String classType) {
        this.selectedClass = classType;
        stage.getRoot().addAction(Actions.sequence(Actions.fadeOut(1.0f), toGameScreen));
    }

    Action toGameScreen = new Action() {
        public boolean act(float delta) {
            main.newGame(selectedClass);
            return true;
        }
    };

    Action toContinuedGameScreen = new Action() {
        public boolean act(float delta) {
            main.continueGame();
            return true;
        }
    };

    Action toOptionScreen = new Action() {
        public boolean act(float delta) {
            main.setScreen(main.optionsScreen);
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
