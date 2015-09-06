package galenscovell.screens;

import galenscovell.flicker.FlickerMain;
import galenscovell.screens.components.OptionsMenu;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * MAINMENU SCREEN
 * Displays primary game menu.
 *
 * @author Galen Scovell
 */

public class MainMenuScreen extends AbstractScreen {
    private OptionsMenu optionsMenu;

    public MainMenuScreen(FlickerMain root){
        super(root);
    }

    @Override
    public void create() {
        this.stage = new Stage(new FitViewport(480, 800), root.spriteBatch);
        this.optionsMenu = new OptionsMenu(this);

        Table mainTable = new Table();
        mainTable.padBottom(4);
        mainTable.setFillParent(true);

        /**********************************
         * TOP TABLE                      *
         **********************************/
        Table optionsButton = new Table();
        optionsButton.setTouchable(Touchable.enabled);
        setIcon(optionsButton, "options", 32);
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(optionsMenu);
            }
        });
        mainTable.add(optionsButton).width(48).height(48).expand().fill().top().right();
        mainTable.row();

        Table topTable = new Table();
        Label titleLabel = new Label("FLICKER", ResourceManager.titleStyle);
        titleLabel.setAlignment(Align.center, Align.center);
        topTable.add(titleLabel).width(400).expand().fill();

        mainTable.add(topTable).expand().fill().top();
        mainTable.row();


        /**********************************
         * CENTER TABLE                   *
         **********************************/
        Table centerTable = new Table();
        centerTable.background(ResourceManager.buttonUp);
        TextButton newGameButton = new TextButton("New Game", ResourceManager.frameStyle);
        newGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                root.newGame();
            }
        });
        TextButton continueButton = new TextButton("Continue", ResourceManager.frameStyle);
        continueButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Continue previous game
            }
        });
        centerTable.add(newGameButton).width(300).height(80).expand().fill();
        centerTable.row();
        centerTable.add(continueButton).width(300).height(80).expand().fill();
        mainTable.add(centerTable).expand().fill().width(440).height(400);
        mainTable.row();

        Label detailLabel = new Label("Flicker v0.1a \u00a9 2015, Galen Scovell", ResourceManager.detailStyle);
        detailLabel.setAlignment(Align.bottom);
        mainTable.add(detailLabel).width(150).expand().fill();
        mainTable.pack();

        stage.addActor(mainTable);
    }

    public void closeOptions() {
        optionsMenu.remove();
    }

    private void setIcon(Table table, String name, int height) {
        Image icon = new Image(new TextureAtlas.AtlasRegion(ResourceManager.uiAtlas.findRegion(name)));
        icon.setScaling(Scaling.fillY);
        table.add(icon).height(height).expand().fill().center();
    }
}
