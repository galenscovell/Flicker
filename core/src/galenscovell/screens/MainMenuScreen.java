package galenscovell.screens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Scaling;
import galenscovell.flicker.FlickerMain;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * MAINMENU SCREEN
 * Displays primary game menu.
 *
 * @author Galen Scovell
 */

public class MainMenuScreen extends AbstractScreen {
    private Table centerTable, buttonTable;

    public MainMenuScreen(FlickerMain root){
        super(root);
    }

    protected void create() {
        this.stage = new Stage(new FitViewport(800, 480), root.spriteBatch);

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
         * CENTER TABLE                   *
         **********************************/
        this.centerTable = new Table();
        createButtonTable();
        centerTable.add(buttonTable).expand().fill();

        mainTable.add(centerTable).expand().fill();
        mainTable.row();

        Label detailLabel = new Label("Flicker v0.1a \u00a9 2015, Galen Scovell", ResourceManager.detailStyle);
        detailLabel.setAlignment(Align.bottom);
        mainTable.add(detailLabel).width(150).expand().fill();
        mainTable.pack();
        stage.addActor(mainTable);
    }

    private void createButtonTable() {
        this.buttonTable = new Table();
        TextButton newGameButton = new TextButton("New Game", ResourceManager.buttonStyle);
        newGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                root.newGame();
            }
        });
        TextButton continueButton = new TextButton("Continue", ResourceManager.buttonStyle);
        continueButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Continue previous game
            }
        });
        TextButton settingsButton = new TextButton("Settings", ResourceManager.buttonStyle);
        settingsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Settings screen
            }
        });
        TextButton quitButton = new TextButton("Quit Game", ResourceManager.buttonStyle);
        quitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        buttonTable.add(newGameButton).width(300).height(70).expand().fill().padBottom(10);
        buttonTable.row();
        buttonTable.add(continueButton).width(300).height(70).expand().fill().padBottom(10);
        buttonTable.row();
        buttonTable.add(settingsButton).width(300).height(70).expand().fill().padBottom(10);
        buttonTable.row();
        buttonTable.add(quitButton).width(300).height(70).expand().fill();
    }
}
