
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
    public Stage stage;
    private Label eventLog;
    private ProgressBar health, mana;
    public Button examineButton, inventoryButton, optionsButton;
    private int eventLines = 1;
    private final int width = Gdx.graphics.getWidth();
    private final int height = Gdx.graphics.getHeight();


    public HudDisplay() {
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
        // mainTable.setDebug(true);



        // Top right table
        Table topRightTable = new Table();
        // Player Stats table
        Table playerTable = new Table();
        // Player image within player table
        Table playerImage = new Table();
        playerImage.setBackground(hudBg);
        Image playerIcon = createIcon("icons/explorer.png");
        playerImage.add(playerIcon).center().fill().expand();
        playerImage.pack();
        // Player health and mana bar table
        Table playerBars = new Table();
        playerBars.padLeft(width / 24);
        playerBars.pack();
        // Player health within player table
        this.health = createBar("ui/healthfill.png", "ui/barempty.png");
        health.pack();
        // Table playerHealth = new Table();
        // playerHealth.setBackground(barBg);
        // playerHealth.pack();
        // Player mana within player table
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



        // Bottom left table
        Table bottomLeftTable = new Table();
        // Examine table
        this.examineButton = new Button(hudBg);
        Image examineIcon = createIcon("icons/examine.png");
        examineButton.add(examineIcon).center().fill().expand();
        examineButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Examine button clicked");
            }
        });
        // Inventory table
        this.inventoryButton = new Button(hudBg);
        Image inventoryIcon = createIcon("icons/inventory.png");
        inventoryButton.add(inventoryIcon).center().fill().expand();
        inventoryButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Inventory button clicked");
            }
        });
        // Options table
        this.optionsButton = new Button(hudBg);
        Image optionsIcon = createIcon("icons/options.png");
        optionsButton.add(optionsIcon).center().fill().expand();
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Options button clicked");
            }
        });

        bottomLeftTable.add(examineButton).height(height / 7).width(width / 11);
        bottomLeftTable.add(inventoryButton).height(height / 7).width(width / 11);
        bottomLeftTable.add(optionsButton).height(height / 7).width(width / 11).fill();
        mainTable.add(bottomLeftTable).expand().bottom().left();



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
