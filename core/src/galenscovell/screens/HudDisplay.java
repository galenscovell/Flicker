
/**
 * HUDDISPLAY CLASS
 *
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
    private final int height = Constants.HUD_HEIGHT;
    private final int width = Constants.WINDOW_X;


    public HudDisplay() {
        this.stage = new Stage(new ExtendViewport(width, height, width, height));

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/SDS_8x8.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        BitmapFont retroFontLarge = fontGenerator.generateFont(parameter);
        parameter.size = 10;
        BitmapFont retroFontSmall = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();

        Label.LabelStyle retroStyleLarge = new Label.LabelStyle(retroFontLarge, Color.WHITE);
        Label.LabelStyle retroStyleSmall = new Label.LabelStyle(retroFontSmall, Color.WHITE);

        NinePatchDrawable bgImage = new NinePatchDrawable(getNinePatch("ui/hudBack.9.png"));
        NinePatchDrawable barBg = new NinePatchDrawable(getNinePatch("ui/bar.9.png"));

        // Init main HUD layout
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(4, 8, 4, 8);
        mainTable.setBackground(bgImage);



        // Health section of HUD (top)
        Table healthTable = new Table();
        healthTable.pad(8, 6, 8, 0);

        Label healthLabel = new Label("HEALTH", retroStyleLarge);
        healthLabel.setAlignment(Align.left);
        healthTable.add(healthLabel).fill();

        Table healthBar = new Table();
        healthBar.setBackground(barBg);
        healthTable.add(healthBar).width(width / 7).height(height / 3).expand();
        healthTable.row();

        Label manaLabel = new Label("MANA", retroStyleLarge);
        manaLabel.setAlignment(Align.left);
        healthTable.add(manaLabel).fill();

        Table manaBar = new Table();
        manaBar.setBackground(barBg);
        healthTable.add(manaBar).width(width / 7).height(height / 3).expand();

        healthTable.pack();
        mainTable.add(healthTable).left().height(height - (height / 6)).width(width / 4).expand();



        // Events section of HUD (middle)
        Table eventTable = new Table();
        eventTable.setBackground(bgImage);

        this.eventLog = new Label("Events will be displayed here.", retroStyleSmall);
        eventLog.setWrap(true);
        eventTable.add(eventLog).height(height - (height / 6)).fill().expand();

        eventTable.pack();
        mainTable.add(eventTable).height(height - (height / 6)).width(width / 2).expand();



        // Options section of HUD (bottom)
        Table optionsTable = new Table();

        Label optionsLabel = new Label("OPTIONS", retroStyleLarge);
        optionsLabel.setAlignment(Align.right);
        optionsTable.add(optionsLabel).top().fill();
        optionsTable.pack();
        mainTable.add(optionsTable).right().height(height).width(width / 5).expand();



        // Finalize HUD
        mainTable.pack();
        stage.addActor(mainTable);
    }

    public void render() {
        stage.draw();
    }

    public void addToLog(String text) {
        if (eventLines == 4) {
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
