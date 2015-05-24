
/**
 * HUDDISPLAY CLASS
 * Creates HUD and handles HUD updates.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.FitViewport;

import galenscovell.util.Constants;
import galenscovell.util.ScreenResources;


public class HudDisplay {
    private GameScreen game;
    public Stage stage;
    private Label eventLog;
    private ProgressBar health, mana;
    private OptionsMenu optionsMenu;
    private int eventLines = 1;
    private boolean optionsOpen, inventoryOpen, playerOpen;


    public HudDisplay(GameScreen game) {
        this.game = game;
        create();
    }

    public void create() {
        this.stage = new Stage(new FitViewport((float) Constants.WINDOW_X, (float) Constants.WINDOW_Y));
        this.optionsMenu = new OptionsMenu(this);

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
        topTable.add(topLeft).height(120).width(400).expand().top().left();


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
        playerBars.padLeft(30);
        this.health = createBar("healthfill", 50);
        this.mana = createBar("manafill", 30);
        playerBars.add(health).width(100).right();
        playerBars.row();
        playerBars.add(mana).width(60).right();
        topRight.add(playerBars).expand().top().right();
        topRight.add(playerButton).width(80).height(80).top().right();
        topTable.add(topRight).height(120).width(400).expand().top().right();

        mainTable.add(topTable).expand().fill().top();
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
                if (optionsOpen) {
                    optionsMenu.remove();
                    optionsOpen = false;
                } else {
                    stage.addActor(optionsMenu);
                    optionsOpen = true;
                }
            }
        });
        bottomLeft.add(examineButton).height(80).width(80);
        bottomLeft.add(inventoryButton).height(80).width(80);
        bottomLeft.add(optionsButton).height(80).width(80);
        bottomTable.add(bottomLeft).bottom().left().expand();


        // Bottom right section (d-pad)
        Table dpad = new Table();
        Button upButton = new Button(ScreenResources.buttonStyle);
        setIcon(upButton, "uparrow");
        dpad.add(upButton).width(70).height(70).expand().colspan(2).center();
        upButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(0, -1);
            }
        });
        dpad.row();
        Button leftButton = new Button(ScreenResources.buttonStyle);
        setIcon(leftButton, "leftarrow");
        dpad.add(leftButton).width(70).height(70).expand().left();
        leftButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(-1, 0);
            }
        });
        Button rightButton = new Button(ScreenResources.buttonStyle);
        setIcon(rightButton, "rightarrow");
        dpad.add(rightButton).width(70).height(70).expand().right();
        rightButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(1, 0);
            }
        });
        dpad.row();
        Button downButton = new Button(ScreenResources.buttonStyle);
        setIcon(downButton, "downarrow");
        dpad.add(downButton).width(70).height(70).expand().colspan(2).center();
        downButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(0, 1);
            }
        });
        bottomTable.add(dpad).height(160).width(220).expand().right();

        mainTable.add(bottomTable).bottom().fill();

        // Finalize HUD
        mainTable.pack();
        stage.addActor(mainTable);
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void returnToMainMenu() {
        optionsMenu.remove();
        optionsOpen = false;
        game.toMainMenu();
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
        icon.setScaling(Scaling.fit);
        table.add(icon).expand().fill().center();
    }

    public void setHealthBarRange(int max) {
        health.setRange(0, max);
    }

    public void setManaBarRange(int max) {
        mana.setRange(0, max);
    }

    private ProgressBar createBar(String path, int size) {
        TextureRegionDrawable fill = new TextureRegionDrawable(ScreenResources.uiAtlas.findRegion(path));
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(ScreenResources.frameBG, fill);
        ProgressBar bar = new ProgressBar(0, size, 1, false, barStyle);
        barStyle.knobAfter = fill;
        bar.setValue(0);
        bar.setAnimateDuration(0.2f);
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
