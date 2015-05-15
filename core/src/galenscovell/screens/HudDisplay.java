
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

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
        BitmapFont retroFontLarge = fontGenerator.generateFont(parameter);
        parameter.size = 8;
        BitmapFont retroFontSmall = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();

        Label.LabelStyle retroStyleLarge = new Label.LabelStyle(retroFontLarge, Color.WHITE);
        Label.LabelStyle retroStyleSmall = new Label.LabelStyle(retroFontSmall, Color.WHITE);

        NinePatchDrawable bgImage = new NinePatchDrawable(getNinePatch("ui/hudBack.9.png"));
        NinePatchDrawable barBg = new NinePatchDrawable(getNinePatch("ui/bar.9.png"));

        // Init main HUD layout (fills screen)
        Table mainTable = new Table();
        mainTable.pad(2, 2, 2, 2);
        mainTable.setFillParent(true);
        mainTable.setDebug(true);


        // Top right table
        Table topRightTable = new Table();

        Table playerTable = new Table();
        playerTable.setBackground(bgImage);
        topRightTable.add(playerTable).height(height / 5).width(width / 4);
        topRightTable.row();

        Table eventTable = new Table();
        eventTable.pad(4, 0, 4, 0);
        this.eventLog = new Label("Events will be displayed here.", retroStyleSmall);
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

        Table optionsTable = new Table();
        Label optionsLabel = new Label("OPTIONS", retroStyleLarge);
        optionsLabel.setAlignment(Align.right);
        optionsTable.add(optionsLabel).top().fill();
        optionsTable.pack();

        bottomLeftTable.add(optionsTable).height(height / 8).width(width / 4);
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

    private NinePatch getNinePatch(String fname) {
        final Texture t = new Texture(Gdx.files.internal(fname));
        return new NinePatch(new TextureRegion(t, 1, 1, t.getWidth() - 3, t.getWidth() - 3), 10, 10, 10, 10);
    }
}
