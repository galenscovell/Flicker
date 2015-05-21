
/**
 * SCREENRESOURCES
 * Handles common resources used within all screens.
 */

package galenscovell.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class ScreenResources {
    public static TextureAtlas uiAtlas = new TextureAtlas(Gdx.files.internal("ui/uiAtlas.pack"));

    public static Label.LabelStyle detailStyle;
    public static Label.LabelStyle titleStyle;

    public static TextureRegionDrawable hudBG;
    public static TextureRegionDrawable buttonDown;

    public static TextButton.TextButtonStyle buttonStyle;


    public ScreenResources() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 8;
        BitmapFont detailFont = fontGenerator.generateFont(parameter);
        parameter.size = 14;
        BitmapFont buttonFont = fontGenerator.generateFont(parameter);
        parameter.size = 48;
        BitmapFont titleFont = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();

        this.detailStyle = new Label.LabelStyle(detailFont, Color.WHITE);
        this.titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        this.hudBG = new TextureRegionDrawable(uiAtlas.findRegion("hudbg"));
        this.buttonDown = new TextureRegionDrawable(uiAtlas.findRegion("buttonDown"));
        this.buttonStyle = new TextButton.TextButtonStyle(hudBG, buttonDown, hudBG, buttonFont);
    }

    public void dispose() {
        uiAtlas.dispose();
    }
}
