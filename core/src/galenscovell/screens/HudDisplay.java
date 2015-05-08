
/**
 * HUDDISPLAY CLASS
 *
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.utils.Align;
import galenscovell.util.Constants;


public class HudDisplay {
    private Stage stage;


    public HudDisplay() {
        this.stage = new Stage();

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/SDS_8x8.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        BitmapFont retroFont = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();
        Label.LabelStyle retroStyle = new Label.LabelStyle(retroFont, Color.WHITE);

        Table mainTable = new Table();
        mainTable.pad(30, 30, 600, 30);

        Table healthTable = new Table();
        healthTable.pad(0, 0, 0, 330);

        Table inventoryTable = new Table();
        inventoryTable.pad(0, 330, 0, 0);

        Label healthLabel = new Label("HEALTH", retroStyle);
        healthTable.add(healthLabel);
        healthTable.pack();
        mainTable.add(healthTable);

        Label inventoryLabel = new Label("INVENTORY", retroStyle);
        inventoryTable.add(inventoryLabel);
        inventoryTable.pack();
        mainTable.add(inventoryTable);

        mainTable.pack();
        stage.addActor(mainTable);
    }

    public void render() {
        stage.draw();
    }
}
