
/**
 * HUDDISPLAY CLASS
 * Creates HUD and handles HUD updates.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;

import galenscovell.util.Constants;


public class HudDisplay {
    private GameScreen game;
    public Stage stage;
    private TextureAtlas uiAtlas;
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
        this.uiAtlas = new TextureAtlas(Gdx.files.internal("ui/uiAtlas.pack"));

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/SDS_8x8.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 9;
        BitmapFont customFont = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();

        Label.LabelStyle customStyle = new Label.LabelStyle(customFont, Color.WHITE);
        TextureRegionDrawable hudBg = new TextureRegionDrawable(uiAtlas.findRegion("hudbg"));

        // Init main HUD layout (fills screen)
        Table mainTable = new Table();
        mainTable.pad(2, 2, 2, 2);
        mainTable.setFillParent(true);



        /**********************************
         * TOP TABLE
         **********************************/
        Table topTable = new Table();


        // Top right section
        Table topRight = new Table();

        Button playerButton = new Button(hudBg);
        setIcon(playerButton, "explorer");
        playerButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Player button clicked");
            }
        });

        // Player health and mana bar table
        Table playerBars = new Table();
        playerBars.padLeft(width / 24);
        // Player health
        this.health = createBar("healthfill", "barempty");
        // Player mana
        this.mana = createBar("manafill", "barempty");

        playerBars.add(health).height(height / 22).width(width / 4).right();
        playerBars.row();
        playerBars.add(mana).height(height / 22).width(width / 8).right();

        topRight.add(playerBars).top().right();
        topRight.add(playerButton).height(height / 7).width(width / 11).expand().top().right();

        topTable.add(topRight).height(height / 6).width(width / 3);
        topTable.row();


        // Event log table
        Table eventTable = new Table();
        eventTable.pad(4, 0, 4, 0);
        this.eventLog = new Label("Events displayed here.", customStyle);
        eventLog.setAlignment(Align.top, Align.right);
        eventLog.setWrap(true);
        eventTable.add(eventLog).height(height / 6).width(width / 4);;

        topTable.add(eventTable).right();
        mainTable.add(topTable).expand().top().right();
        mainTable.row();




        /**********************************
         * BOTTOM TABLE
         **********************************/
        Table bottomTable = new Table();


        // Bottom left section
        Table bottomLeft = new Table();

        Button examineButton = new Button(hudBg);
        setIcon(examineButton, "examine");
        examineButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Examine button clicked");
            }
        });
        Button inventoryButton = new Button(hudBg);
        setIcon(inventoryButton, "inventory");
        inventoryButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Inventory button clicked");
            }
        });
        Button optionsButton = new Button(hudBg);
        setIcon(optionsButton, "options");
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Options button clicked");
            }
        });

        bottomLeft.add(examineButton).height(height / 7).width(width / 11);
        bottomLeft.add(inventoryButton).height(height / 7).width(width / 11);
        bottomLeft.add(optionsButton).height(height / 7).width(width / 11);
        bottomTable.add(bottomLeft).bottom().left().expand();


        // Bottom right section (d-pad)
        Table dpad = new Table();

        Button upButton = new Button(hudBg);
        setIcon(upButton, "uparrow");
        dpad.add(upButton).width(width / 12).height(height / 8).expand().colspan(2).center();
        upButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(0, -1);
            }
        });
        dpad.row();

        Button leftButton = new Button(hudBg);
        setIcon(leftButton, "leftarrow");
        dpad.add(leftButton).width(width / 12).height(height / 8).expand().left();
        leftButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(-1, 0);
            }
        });

        Button rightButton = new Button(hudBg);
        setIcon(rightButton, "rightarrow");
        dpad.add(rightButton).width(width / 12).height(height / 8).expand().right();
        rightButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(1, 0);
            }
        });
        dpad.row();

        Button downButton = new Button(hudBg);
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
        Image icon = new Image(new TextureAtlas.AtlasRegion(uiAtlas.findRegion(name)));
        icon.setScaling(Scaling.fill);
        table.add(icon).width(table.getWidth() * 0.55f).height(table.getHeight() * 0.55f).center();
    }

    private ProgressBar createBar(String path1, String path2) {
        TextureRegionDrawable fill = new TextureRegionDrawable(uiAtlas.findRegion(path1));
        TextureRegionDrawable empty = new TextureRegionDrawable(uiAtlas.findRegion(path1));
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(fill, empty);
        ProgressBar bar = new ProgressBar(0, 50, 1, false, barStyle);
        barStyle.knobBefore = empty;
        bar.setValue(0);
        bar.setAnimateDuration(0.5f);
        return bar;
    }

    public void changeHealth(int amount) {
        health.setValue(health.getValue() + amount);
    }

    public void dispose() {
        stage.dispose();
        uiAtlas.dispose();
    }
}
