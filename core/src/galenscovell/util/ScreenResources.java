
/**
 * SCREENRESOURCES
 * Handles common resources used within all screens.
 */

package galenscovell.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;


public class ScreenResources {
    public static TextureAtlas uiAtlas = new TextureAtlas(Gdx.files.internal("ui/uiAtlas.pack"));

    public static Label.LabelStyle detailStyle;
    public static Label.LabelStyle menuStyle;
    public static Label.LabelStyle titleStyle;

    public static NinePatchDrawable hudBG;
    public static NinePatchDrawable buttonDown;
    public static NinePatchDrawable frameBG;
    public static NinePatchDrawable frameLit;

    public static TextButton.TextButtonStyle buttonStyle;
    public static TextButton.TextButtonStyle frameStyle;


    public ScreenResources() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 8;
        BitmapFont detailFont = fontGenerator.generateFont(parameter);
        parameter.size = 14;
        BitmapFont buttonFont = fontGenerator.generateFont(parameter);
        parameter.size = 24;
        BitmapFont menuFont = fontGenerator.generateFont(parameter);
        parameter.size = 48;
        BitmapFont titleFont = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();

        this.detailStyle = new Label.LabelStyle(detailFont, Color.WHITE);
        this.menuStyle = new Label.LabelStyle(menuFont, Color.WHITE);
        this.titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);

        this.hudBG = new NinePatchDrawable(uiAtlas.createPatch("buttonbg"));
        this.buttonDown = new NinePatchDrawable(uiAtlas.createPatch("buttondown"));
        this.buttonStyle = new TextButton.TextButtonStyle(hudBG, buttonDown, hudBG, buttonFont);

        this.frameBG = new NinePatchDrawable(uiAtlas.createPatch("framedbg"));
        this.frameLit = new NinePatchDrawable(uiAtlas.createPatch("framedlit"));
        this.frameStyle = new TextButton.TextButtonStyle(frameBG, frameLit, frameBG, buttonFont);
    }

    public void dispose() {
        uiAtlas.dispose();
    }
}
