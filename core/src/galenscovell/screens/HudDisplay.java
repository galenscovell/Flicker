
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.utils.viewport.ExtendViewport;
import galenscovell.util.Constants;


public class HudDisplay {
    private Stage stage;


    public HudDisplay() {
        this.stage = new Stage(new ExtendViewport(Constants.HUD_WIDTH, Constants.WINDOW_Y, Constants.HUD_WIDTH, Constants.WINDOW_Y));

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/SDS_8x8.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        BitmapFont retroFont = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();
        Label.LabelStyle retroStyle = new Label.LabelStyle(retroFont, Color.WHITE);

        Table mainTable = new Table();
        mainTable.pad(30, 30, 30, 30);

        Table healthTable = new Table();
        healthTable.pad(20, 0, 20, 0);

        Table inventoryTable = new Table();
        inventoryTable.pad(20, 0, 20, 0);

        Label healthLabel = new Label("HEALTH", retroStyle);
        healthTable.add(healthLabel);
        healthTable.pack();
        mainTable.add(healthTable).top();
        mainTable.row();

        Label inventoryLabel = new Label("INVENTORY", retroStyle);
        inventoryTable.add(inventoryLabel);
        inventoryTable.pack();
        mainTable.add(inventoryTable).bottom();

        mainTable.pack();
        stage.addActor(mainTable);
    }

    public void render() {
        stage.draw();
    }
}
