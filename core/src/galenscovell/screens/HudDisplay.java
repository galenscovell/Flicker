
/**
 * HUDDISPLAY CLASS
 * Creates HUD and handles HUD updates.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;

import galenscovell.util.Constants;
import galenscovell.util.ScreenResources;


public class HudDisplay {
    private GameScreen game;
    public Stage stage;
    private Label eventLog;
    private ProgressBar health, mana;
    private int eventLines = 1;
    private final int width = Gdx.graphics.getWidth();
    private final int height = Gdx.graphics.getHeight();


    public HudDisplay(GameScreen game) {
        this.game = game;
        create();
    }

    public void create() {
        this.stage = new Stage();

        // Init main HUD layout (fills screen)
        Table mainTable = new Table();
        mainTable.pad(2, 2, 2, 2);
        mainTable.setFillParent(true);

        /**********************************
         * TOP TABLE                      *
         **********************************/
        Table topTable = new Table();

        // Top left section
        Table topLeft = new Table();
        topLeft.pad(8, 8, 8, 8);
        this.eventLog = new Label("Events displayed here.", ScreenResources.detailStyle);
        eventLog.setAlignment(Align.topLeft, Align.topLeft);
        eventLog.setWrap(true);
        topLeft.add(eventLog).expand().fill().top().left();
        topTable.add(topLeft).height(height / 4).width(width / 2).expand().top().left();


        // Top right section
        Table topRight = new Table();
        Button playerButton = new Button(ScreenResources.frameStyle);
        setIcon(playerButton, "explorer");
        playerButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        // Player health and mana bar table
        Table playerBars = new Table();
        playerBars.padLeft(width / 24);
        this.health = createBar("healthfill", "barempty");
        this.mana = createBar("manafill", "barempty");
        playerBars.add(health).height(height / 22).width(width / 4).right();
        playerBars.row();
        playerBars.add(mana).height(height / 22).width(width / 8).right();
        topRight.add(playerBars).expand().top().right();
        topRight.add(playerButton).height(height / 7).width(width / 11).top().right();
        topTable.add(topRight).height(height / 4).width(width / 2).expand().top().right();

        mainTable.add(topTable).expand().top();
        mainTable.row();

        /**********************************
         * BOTTOM TABLE                   *
         **********************************/
        Table bottomTable = new Table();

        // Bottom left section
        Table bottomLeft = new Table();
        Button examineButton = new Button(ScreenResources.buttonStyle);
        setIcon(examineButton, "examine");
        examineButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        Button inventoryButton = new Button(ScreenResources.buttonStyle);
        setIcon(inventoryButton, "inventory");
        inventoryButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        Button optionsButton = new Button(ScreenResources.buttonStyle);
        setIcon(optionsButton, "options");
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        bottomLeft.add(examineButton).height(height / 7).width(width / 11);
        bottomLeft.add(inventoryButton).height(height / 7).width(width / 11);
        bottomLeft.add(optionsButton).height(height / 7).width(width / 11);
        bottomTable.add(bottomLeft).bottom().left().expand();


        // Bottom right section (d-pad)
        Table dpad = new Table();
        Button upButton = new Button(ScreenResources.buttonStyle);
        setIcon(upButton, "uparrow");
        dpad.add(upButton).width(width / 12).height(height / 8).expand().colspan(2).center();
        upButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(0, -1);
            }
        });
        dpad.row();
        Button leftButton = new Button(ScreenResources.buttonStyle);
        setIcon(leftButton, "leftarrow");
        dpad.add(leftButton).width(width / 12).height(height / 8).expand().left();
        leftButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(-1, 0);
            }
        });
        Button rightButton = new Button(ScreenResources.buttonStyle);
        setIcon(rightButton, "rightarrow");
        dpad.add(rightButton).width(width / 12).height(height / 8).expand().right();
        rightButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(1, 0);
            }
        });
        dpad.row();
        Button downButton = new Button(ScreenResources.buttonStyle);
        setIcon(downButton, "downarrow");
        dpad.add(downButton).width(width / 12).height(height / 8).expand().colspan(2).center();
        downButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(0, 1);
            }
        });
        bottomTable.add(dpad).height(height / 3).width(width / 3).right().expand();

        mainTable.add(bottomTable).bottom().fill();

        // Finalize HUD
        mainTable.pack();
        stage.addActor(mainTable);
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void addToLog(String text) {
        if (eventLines == 5) {
            eventLog.setText(text);
            eventLines = 1;
        } else {
            StringBuilder currentText = eventLog.getText();
            String newText = currentText + "\n" + text;
            eventLog.setText(newText);
            eventLines++;
        }
    }

    private void moveEvent(int x, int y) {
        int[] move = {x, y};
        game.update(move);
    }

    private void setIcon(Table table, String name) {
        Image icon = new Image(new TextureAtlas.AtlasRegion(ScreenResources.uiAtlas.findRegion(name)));
        icon.setScaling(Scaling.fillX);
        table.add(icon).width(table.getWidth() * 0.6f).center();
    }

    private ProgressBar createBar(String path1, String path2) {
        TextureRegionDrawable fill = new TextureRegionDrawable(ScreenResources.uiAtlas.findRegion(path1));
        TextureRegionDrawable empty = new TextureRegionDrawable(ScreenResources.uiAtlas.findRegion(path2));
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(fill, empty);
        ProgressBar bar = new ProgressBar(0, 50, 1, false, barStyle);
        barStyle.knobBefore = empty;
        bar.setValue(0);
        bar.setAnimateDuration(0.5f);
        bar.validate();
        return bar;
    }

    public void changeHealth(int amount) {
        health.setValue(health.getValue() + amount);
    }

    public void dispose() {
        stage.dispose();
    }
}
