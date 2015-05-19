
/**
 * HUDDISPLAY CLASS
 * Creates HUD and handles HUD updates.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;

import galenscovell.util.Constants;


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
        this.stage = new Stage();

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/SDS_8x8.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 9;
        BitmapFont customFont = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();

        Label.LabelStyle customStyle = new Label.LabelStyle(customFont, Color.WHITE);
        NinePatchDrawable hudBg = new NinePatchDrawable(getNinePatch("ui/hudbg.png"));

        // Init main HUD layout (fills screen)
        Table mainTable = new Table();
        mainTable.pad(2, 2, 2, 2);
        mainTable.setFillParent(true);



        /**********************************
         * TOP TABLE
         **********************************/
        Table topRightTable = new Table();


        // Player Stats table
        Table playerTable = new Table();

        // Player image
        Table playerImage = new Table();
        playerImage.setBackground(hudBg);
        Image playerIcon = createIcon("icons/explorer.png");
        playerImage.add(playerIcon).center().fill().expand();
        playerImage.pack();

        // Player health and mana bar table
        Table playerBars = new Table();
        playerBars.padLeft(width / 24);
        playerBars.pack();
        // Player health
        this.health = createBar("ui/healthfill.png", "ui/barempty.png");
        health.pack();
        // Player mana
        this.mana = createBar("ui/manafill.png", "ui/barempty.png");
        mana.pack();

        playerBars.add(health).height(height / 22).width(width / 4).right();
        playerBars.row();
        playerBars.add(mana).height(height / 22).width(width / 8).right();

        playerTable.add(playerBars).top().right();
        playerTable.add(playerImage).height(height / 7).width(width / 11).expand().top().right();
        playerTable.pack();

        topRightTable.add(playerTable).height(height / 6).width(width / 3);
        topRightTable.row();


        // Event log table
        Table eventTable = new Table();
        eventTable.pad(4, 0, 4, 0);
        this.eventLog = new Label("Events displayed here.", customStyle);
        eventLog.setAlignment(Align.top, Align.right);
        eventLog.setWrap(true);
        eventTable.add(eventLog).height(height / 6).width(width / 4);
        eventTable.pack();

        topRightTable.add(eventTable).right();
        topRightTable.pack();
        mainTable.add(topRightTable).expand().top().right();
        mainTable.row();




        /**********************************
         * BOTTOM TABLE
         **********************************/
        Table bottomTable = new Table();


        // Bottom left section
        Table bottomLeft = new Table();

        Button examineButton = new Button(hudBg);
        Image examineIcon = createIcon("icons/examine.png");
        examineButton.add(examineIcon).center().fill().expand();
        examineButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Examine button clicked");
            }
        });
        Button inventoryButton = new Button(hudBg);
        Image inventoryIcon = createIcon("icons/inventory.png");
        inventoryButton.add(inventoryIcon).center().fill().expand();
        inventoryButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Inventory button clicked");
            }
        });
        Button optionsButton = new Button(hudBg);
        Image optionsIcon = createIcon("icons/options.png");
        optionsButton.add(optionsIcon).center().fill().expand();
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
        Image upImage = createIcon("ui/uparrow.png");
        upButton.add(upImage).center().fill().expand();
        dpad.add(upButton).width(width / 12).height(height / 8).expand().colspan(2).center();
        upButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(0, -1);
            }
        });
        dpad.row();

        Button leftButton = new Button(hudBg);
        Image leftImage = createIcon("ui/leftarrow.png");
        leftButton.add(leftImage).center().fill().expand();
        dpad.add(leftButton).width(width / 12).height(height / 8).expand().left();
        leftButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(-1, 0);
            }
        });

        Button rightButton = new Button(hudBg);
        Image rightImage = createIcon("ui/rightarrow.png");
        rightButton.add(rightImage).center().fill().expand();
        dpad.add(rightButton).width(width / 12).height(height / 8).expand().right();
        rightButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(1, 0);
            }
        });
        dpad.row();

        Button downButton = new Button(hudBg);
        Image downImage = createIcon("ui/downarrow.png");
        downButton.add(downImage).center().fill().expand();
        dpad.add(downButton).width(width / 12).height(height / 8).expand().colspan(2).center();
        downButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                moveEvent(0, 1);
            }
        });
        dpad.pack();

        bottomTable.add(dpad).height(height / 3).width(width / 3).right().expand();
        mainTable.add(bottomTable).bottom().fill();


        // Finalize HUD
        mainTable.pack();
        stage.addActor(mainTable);
    }

    public void render() {
        stage.draw();
        health.act(Gdx.graphics.getDeltaTime());
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

    private NinePatch getNinePatch(String path) {
        Texture t = new Texture(Gdx.files.internal(path));
        return new NinePatch(new TextureRegion(t, 1, 1, t.getWidth() - 2, t.getWidth() - 2), 10, 10, 10, 10);
    }

    private Image createIcon(String path) {
        Image icon = new Image();
        icon.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(path)))));
        icon.setScaling(Scaling.fill);
        return icon;
    }

    private ProgressBar createBar(String path1, String path2) {
        TextureRegionDrawable fill = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(path1))));
        TextureRegionDrawable empty = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(path2))));
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(fill, empty);
        ProgressBar bar = new ProgressBar(0, 50, 1, false, barStyle);
        barStyle.knobBefore = empty;
        bar.setValue(0);
        bar.setAnimateDuration(1);
        return bar;
    }

    public void changeHealth(int amount) {
        health.setValue(health.getValue() + amount);
    }
}
