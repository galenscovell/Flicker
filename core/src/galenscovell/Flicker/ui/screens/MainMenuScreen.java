package galenscovell.flicker.ui.screens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import galenscovell.flicker.FlickerMain;
import galenscovell.flicker.ui.components.OptionMenu;
import galenscovell.flicker.util.*;

public class MainMenuScreen extends AbstractScreen {
    private OptionMenu optionMenu;

    public MainMenuScreen(FlickerMain root){
        super(root);
        this.stage = new Stage(new FitViewport(Constants.UI_X, Constants.UI_Y), root.spriteBatch);
    }

    @Override
    public void create() {
        this.optionMenu = new OptionMenu(this);
        Table mainTable = new Table();
        mainTable.setBackground(new TextureRegionDrawable(ResourceManager.mainMenuBG));
        mainTable.padBottom(4);
        mainTable.setFillParent(true);

        /**********************************
         * TOP TABLE                      *
         **********************************/
        Table optionsButton = new Table();
        optionsButton.setTouchable(Touchable.enabled);
        setIcon(optionsButton, "scroll", 42, 0.5f);
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (optionMenu.hasParent()) {
                    optionMenu.remove();
                } else {
                    stage.addActor(optionMenu);
                }
            }
        });
        mainTable.add(optionsButton).width(48).height(48).expand().fill().top().right();
        mainTable.row();

        Table topTable = new Table();
        Label titleLabel = new Label("FLICKER", ResourceManager.titleStyle);
        titleLabel.setAlignment(Align.center, Align.center);
        topTable.add(titleLabel).width(400).expand().fill().padTop(80);

        mainTable.add(topTable).expand().fill().top();
        mainTable.row();


        /**********************************
         * CENTER TABLE                   *
         **********************************/
        Table centerTable = new Table();
        centerTable.background(ResourceManager.frameUpDec);
        TextButton newGameButton = new TextButton("New Game", ResourceManager.fullButtonStyle);
        newGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                root.newGame();
            }
        });
        TextButton continueButton = new TextButton("Continue", ResourceManager.fullButtonStyle);
        continueButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Continue previous game
            }
        });
        centerTable.add(newGameButton).width(300).height(80).expand().fill();
        centerTable.row();
        centerTable.add(continueButton).width(300).height(80).expand().fill();
        mainTable.add(centerTable).expand().fill().width(440).height(300).bottom();
        mainTable.row();

        Label detailLabel = new Label("Flicker v0.1a \u00a9 2015, Galen Scovell", ResourceManager.detailStyle);
        detailLabel.setAlignment(Align.bottom);
        mainTable.add(detailLabel).width(150).fill().padTop(20).padBottom(10);
        mainTable.pack();

        stage.addActor(mainTable);
    }

    private void setIcon(Table table, String name, int height, float opacity) {
        Image icon = new Image(new TextureAtlas.AtlasRegion(ResourceManager.uiAtlas.findRegion(name)));
        icon.setScaling(Scaling.fillY);
        icon.setColor(1, 1, 1, opacity);
        table.add(icon).height(height).expand().fill().center();
    }
}
