
/**
 * HUDDISPLAY CLASS
 * Creates HUD and handles HUD updates.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.FitViewport;

import galenscovell.util.Constants;
import galenscovell.util.ResourceManager;


public class HudDisplay {
    private GameScreen game;
    public Stage stage;
    private int eventLines = 1;
    private Label eventLog;
    private ProgressBar health, mana;
    private Table playerMenu, inventoryMenu, optionsMenu;
    private Button playerButton, examineButton, inventoryButton, optionsButton;


    public HudDisplay(GameScreen game) {
        this.game = game;
        create();
    }

    public void create() {
        this.stage = new Stage(new FitViewport((float) Constants.WINDOW_X, (float) Constants.WINDOW_Y));
        this.optionsMenu = new OptionsMenu(this);
        this.playerMenu = new PlayerMenu(this);
        this.inventoryMenu = new InventoryMenu(this);

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
        this.eventLog = new Label("Events displayed here.", ResourceManager.detailStyle);
        eventLog.setAlignment(Align.topLeft, Align.topLeft);
        eventLog.setWrap(true);
        topLeft.add(eventLog).expand().fill().top().left();
        topTable.add(topLeft).height(120).width(400).expand().top().left();


        // Top right section
        Table topRight = new Table();
        this.playerButton = new Button(ResourceManager.frameStyle);
        setIcon(playerButton, "explorer", 0.9f);
        playerButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(playerMenu);
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
        this.examineButton = new Button(ResourceManager.buttonStyle);
        setIcon(examineButton, "examine", 0.9f);
        examineButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        this.inventoryButton = new Button(ResourceManager.buttonStyle);
        setIcon(inventoryButton, "inventory", 0.9f);
        inventoryButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(inventoryMenu);
            }
        });
        this.optionsButton = new Button(ResourceManager.buttonStyle);
        setIcon(optionsButton, "options", 0.9f);
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(optionsMenu);
            }
        });
        bottomLeft.add(examineButton).height(80).width(80);
        bottomLeft.add(inventoryButton).height(80).width(80);
        bottomLeft.add(optionsButton).height(80).width(80);
        bottomTable.add(bottomLeft).bottom().left().expand();


        // Bottom right section (d-pad)
        Table dpad = new Table();
        Table upButton = new Table();
        setIcon(upButton, "uparrow", 0.4f);
        dpad.add(upButton).width(70).height(70).expand().colspan(2).center();
        upButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setMovement(0, -1);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setMovement(0, 0);
            }
        });
        dpad.row();
        Table leftButton = new Table();
        setIcon(leftButton, "leftarrow", 0.4f);
        dpad.add(leftButton).width(70).height(70).expand().left();
        leftButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setMovement(-1, 0);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setMovement(0, 0);
            }
        });
        Table rightButton = new Table();
        setIcon(rightButton, "rightarrow", 0.4f);
        dpad.add(rightButton).width(70).height(70).expand().right();
        rightButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setMovement(1, 0);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setMovement(0, 0);
            }
        });
        dpad.row();
        Table downButton = new Table();
        setIcon(downButton, "downarrow", 0.4f);
        dpad.add(downButton).width(70).height(70).expand().colspan(2).center();
        downButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setMovement(0, 1);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setMovement(0, 0);
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

    public void dispose() {
        stage.addActor(playerMenu);
        stage.addActor(inventoryMenu);
        stage.addActor(optionsMenu);
        stage.dispose();
    }

    public void returnToMainMenu() {
        optionsMenu.remove();
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

    public void changeHealth(int amount) {
        health.setValue(health.getValue() + amount);
    }

    private void setIcon(Table table, String name, float alpha) {
        Image icon = new Image(new TextureAtlas.AtlasRegion(ResourceManager.uiAtlas.findRegion(name)));
        icon.setScaling(Scaling.fit);
        icon.setColor(1.0f, 1.0f, 1.0f, alpha);
        table.add(icon).expand().fill().center();
    }

    private ProgressBar createBar(String path, int size) {
        TextureRegionDrawable fill = new TextureRegionDrawable(ResourceManager.uiAtlas.findRegion(path));
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(ResourceManager.frameBG, fill);
        ProgressBar bar = new ProgressBar(0, size, 1, false, barStyle);
        barStyle.knobAfter = fill;
        bar.setValue(0);
        bar.setAnimateDuration(0.2f);
        bar.validate();
        return bar;
    }

    private void menuOperation(Table menu) {
        if (menu.hasParent()) {
            menu.remove();
            game.enableWorldInput();
        } else {
            if (optionsMenu != menu && optionsMenu.hasParent()) {
                optionsMenu.remove();
            } else if (inventoryMenu != menu && inventoryMenu.hasParent()) {
                inventoryMenu.remove();
            } else if (playerMenu != menu && playerMenu.hasParent()) {
                playerMenu.remove();
            }
            stage.addActor(menu);
            game.disableWorldInput();
        }
    }
}
