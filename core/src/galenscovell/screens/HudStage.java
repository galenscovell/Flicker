package galenscovell.screens;

import galenscovell.entities.Player;
import galenscovell.screens.components.*;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * HUD STAGE
 * Creates HUD and handles HUD updates.
 * HUD camera uses pixel dimensions (800, 480) for pixel-perfect placement.
 *
 * @author Galen Scovell
 */

public class HudStage extends Stage {
    private GameScreen game;
    private int eventLines = 1;
    private Label eventLog;
    private ProgressBar health, mana;
    private Table playerMenu, inventoryMenu, optionsMenu, examineMenu;
    private Button playerButton, examineButton, inventoryButton, optionsButton;

    public HudStage(GameScreen game, Player player, SpriteBatch spriteBatch) {
        super(new FitViewport(800, 480), spriteBatch);
        this.game = game;
        create(player);
    }

    public void create(Player player) {
        this.playerMenu = new HudPlayerMenu(this);
        this.examineMenu = new HudExamineMenu(this);
        this.inventoryMenu = new HudInventoryMenu(this);
        this.optionsMenu = new HudOptionsMenu(this);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        /**********************************
         * TOP TABLE                      *
         **********************************/
        Table topTable = new Table();


        Table topLeft = new Table();
        topLeft.pad(8, 8, 8, 8);
        this.eventLog = new Label("Events displayed here.", ResourceManager.detailStyle);
        eventLog.setAlignment(Align.topLeft, Align.topLeft);
        eventLog.setWrap(true);
        topLeft.add(eventLog).expand().fill().top().left();
        topTable.add(topLeft).height(120).width(400).expand().top().left();


        Table topRight = new Table();
        this.playerButton = new Button(ResourceManager.frameStyle);
        setIcon(playerButton, player.getClassType(), 0.9f);
        playerButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(playerMenu);
            }
        });
        // Player health and mana bar table
        Table playerBars = new Table();
        playerBars.padLeft(30);
        this.health = createBar("healthfill", player.getStat("vit") * 5);
        this.mana = createBar("manafill", player.getStat("int") * 5);
        playerBars.add(health).width(player.getStat("vit") * 10).right();
        playerBars.row();
        playerBars.add(mana).width(player.getStat("int") * 10).right();
        topRight.add(playerBars).expand().top().right();
        topRight.add(playerButton).width(80).height(80).top().right();
        topTable.add(topRight).height(120).width(400).expand().top().right();

        mainTable.add(topTable).expand().fill().top();
        mainTable.row();

        /**********************************
         * BOTTOM TABLE                   *
         **********************************/
        Table bottomTable = new Table();


        Table bottomLeft = new Table();
        this.examineButton = new Button(ResourceManager.buttonStyle);
        setIcon(examineButton, "examine", 0.9f);
        examineButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                examine();
                game.toggleTileSelection();
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

        mainTable.add(bottomTable).bottom().fill();

        mainTable.pack();
        this.addActor(mainTable);
    }

    public void dispose() {
        this.addActor(examineButton);
        this.addActor(playerMenu);
        this.addActor(inventoryMenu);
        this.addActor(optionsMenu);
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

    public void examine() {
        if (examineMenu.hasParent()) {
            examineMenu.remove();
            playerButton.setTouchable(Touchable.enabled);
            inventoryButton.setTouchable(Touchable.enabled);
            optionsButton.setTouchable(Touchable.enabled);
        } else {
            this.addActor(examineMenu);
            playerButton.setTouchable(Touchable.disabled);
            inventoryButton.setTouchable(Touchable.disabled);
            optionsButton.setTouchable(Touchable.disabled);
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
        bar.setAnimateDuration(0.4f);
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
            this.addActor(menu);
            game.disableWorldInput();
        }
    }
}
