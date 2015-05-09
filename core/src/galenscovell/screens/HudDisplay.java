
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import galenscovell.util.Constants;


public class HudDisplay {
    private Stage stage;


    public HudDisplay() {
        this.stage = new Stage(new ExtendViewport(Constants.WINDOW_X, Constants.HUD_HEIGHT, Constants.WINDOW_X, Constants.HUD_HEIGHT));

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/SDS_8x8.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        BitmapFont retroFont = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();
        Label.LabelStyle retroStyle = new Label.LabelStyle(retroFont, Color.WHITE);
        NinePatchDrawable bgImage = new NinePatchDrawable(getNinePatch("ui/hudBack.9.png"));
        NinePatchDrawable barBg = new NinePatchDrawable(getNinePatch("ui/bar.9.png"));

        // Init main HUD layout
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(10, 10, 10, 10);
        mainTable.setBackground(bgImage);



        // Health section of HUD (top)
        Table healthTable = new Table();
        healthTable.setBackground(bgImage);

        Label healthLabel = new Label("HEALTH", retroStyle);
        healthLabel.setAlignment(Align.right);
        healthTable.add(healthLabel).top().fill();
        healthTable.row();

        Table healthBar = new Table();
        healthBar.setBackground(barBg);
        healthTable.add(healthBar).width(Constants.WINDOW_X / 4).height(20);

        healthTable.row();
        healthTable.pack();
        mainTable.add(healthTable).left().height(120).expand();



        // Events section of HUD (middle)
        Table eventTable = new Table();
        eventTable.setBackground(bgImage);

        Label eventLabel = new Label("EVENT LOG", retroStyle);
        eventLabel.setAlignment(Align.right);
        eventTable.add(eventLabel).top().fill();
        eventTable.pack();
        mainTable.add(eventTable).width(Constants.WINDOW_X / 2).expand();



        // Options section of HUD (bottom)
        Table optionsTable = new Table();
        optionsTable.setBackground(bgImage);

        Label optionsLabel = new Label("OPTIONS", retroStyle);
        optionsLabel.setAlignment(Align.right);
        optionsTable.add(optionsLabel).top().fill();
        optionsTable.pack();
        mainTable.add(optionsTable).right().width(Constants.WINDOW_X / 5).expand();



        // Finalize HUD
        mainTable.pack();
        stage.addActor(mainTable);
    }

    public void render() {
        stage.draw();
    }

    private NinePatch getNinePatch(String fname) {
        final Texture t = new Texture(Gdx.files.internal(fname));
        return new NinePatch(new TextureRegion(t, 1, 1, t.getWidth() - 3, t.getWidth() - 3), 10, 10, 10, 10);
    }
}
