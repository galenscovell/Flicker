
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;

import galenscovell.util.Constants;


public class HudDisplay {
    private Stage stage;
    private Label eventLog;
    private int eventLines = 1;
    private final int width = Gdx.graphics.getWidth();
    private final int height = Gdx.graphics.getHeight();


    public HudDisplay() {
        this.stage = new Stage();

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/SDS_8x8.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10;
        BitmapFont customFont = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();

        Label.LabelStyle customStyle = new Label.LabelStyle(customFont, Color.WHITE);

        NinePatchDrawable hudBg = new NinePatchDrawable(getNinePatch("ui/hudbg.png"));
        NinePatchDrawable barBg = new NinePatchDrawable(getNinePatch("ui/barbg.png"));
        NinePatchDrawable buttonDownBg = new NinePatchDrawable(getNinePatch("ui/buttondownbg.png"));

        // Init main HUD layout (fills screen)
        Table mainTable = new Table();
        mainTable.pad(2, 2, 2, 2);
        mainTable.setFillParent(true);
        // mainTable.setDebug(true);



        // Top right table
        Table topRightTable = new Table();

        // Player Stats table
        Table playerTable = new Table();
        // Player image within player stats table
        Table playerImage = new Table();
        playerImage.setBackground(hudBg);
        playerImage.pack();
        // Player health within player stats table
        Table playerHealth = new Table();
        playerHealth.setBackground(barBg);
        playerHealth.pack();

        playerTable.add(playerHealth).height(height / 18).width(width / 5).expand().top();
        playerTable.add(playerImage).height(height / 6).width(width / 10).expand().right();
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
        Button examineButton = new Button(hudBg, buttonDownBg);
        Image examineIcon = createIcon("icons/examine.png");
        examineButton.add(examineIcon).center().fill().expand();
        examineButton.pack();
        // Inventory table
        Button inventoryButton = new Button(hudBg, buttonDownBg);
        Image inventoryIcon = createIcon("icons/inventory.png");
        inventoryButton.add(inventoryIcon).center().fill().expand();
        inventoryButton.pack();
        // Options table
        Button optionsButton = new Button(hudBg, buttonDownBg);
        Image optionsIcon = createIcon("icons/options.png");
        optionsButton.add(optionsIcon).center().fill().expand();
        optionsButton.pack();
        optionsButton.toggle();

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
        return new NinePatch(new TextureRegion(t, 1, 1, t.getWidth() - 1, t.getWidth() - 1), 8, 8, 8, 8);
    }

    private Image createIcon(String path) {
        Image icon = new Image();
        icon.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(path)))));
        icon.setScaling(Scaling.fill);
        return icon;
    }
}
