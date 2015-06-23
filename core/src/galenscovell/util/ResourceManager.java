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

/**
 * RESOURCE MANAGER
 * Stores common resources throughout application for easy usage/disposal.
 *
 * @author Galen Scovell
 */

public class ResourceManager {
    public static TextureAtlas uiAtlas = new TextureAtlas(Gdx.files.internal("ui/uiAtlas.pack"));

    public static Label.LabelStyle detailStyle;
    public static Label.LabelStyle mediumStyle;
    public static Label.LabelStyle menuStyle;
    public static Label.LabelStyle titleStyle;

    public static NinePatchDrawable hudBG;
    public static NinePatchDrawable buttonDown;
    public static NinePatchDrawable colorButtonBG;
    public static NinePatchDrawable colorButtonDown;
    public static NinePatchDrawable frameBG;
    public static NinePatchDrawable frameLit;

    public static TextButton.TextButtonStyle buttonStyle;
    public static TextButton.TextButtonStyle colorButtonStyle;
    public static TextButton.TextButtonStyle frameStyle;
    public static TextButton.TextButtonStyle frameCheckedStyle;


    public static void load() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 9;
        BitmapFont smallFont = fontGenerator.generateFont(parameter);
        parameter.size = 16;
        BitmapFont mediumFont = fontGenerator.generateFont(parameter);
        parameter.size = 24;
        BitmapFont largeFont = fontGenerator.generateFont(parameter);
        parameter.size = 48;
        BitmapFont extraLargeFont = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();

        detailStyle = new Label.LabelStyle(smallFont, Color.WHITE);
        mediumStyle = new Label.LabelStyle(mediumFont, Color.WHITE);
        menuStyle = new Label.LabelStyle(largeFont, Color.WHITE);
        titleStyle = new Label.LabelStyle(extraLargeFont, Color.WHITE);

        hudBG = new NinePatchDrawable(uiAtlas.createPatch("buttonbg"));
        buttonDown = new NinePatchDrawable(uiAtlas.createPatch("buttondown"));
        buttonStyle = new TextButton.TextButtonStyle(hudBG, buttonDown, hudBG, mediumFont);
        buttonStyle.pressedOffsetX = 1;
        buttonStyle.pressedOffsetY = -1;

        colorButtonBG = new NinePatchDrawable(uiAtlas.createPatch("tealbuttonbg"));
        colorButtonDown = new NinePatchDrawable(uiAtlas.createPatch("tealbuttondown"));
        colorButtonStyle = new TextButton.TextButtonStyle(colorButtonBG, colorButtonDown, colorButtonBG, mediumFont);
        colorButtonStyle.pressedOffsetX = 1;
        colorButtonStyle.pressedOffsetY = -1;

        frameBG = new NinePatchDrawable(uiAtlas.createPatch("framedbg"));
        frameLit = new NinePatchDrawable(uiAtlas.createPatch("framedlit"));
        frameStyle = new TextButton.TextButtonStyle(frameBG, frameLit, frameBG, mediumFont);
        frameStyle.pressedOffsetX = 1;
        frameStyle.pressedOffsetY = -1;

        frameCheckedStyle = new TextButton.TextButtonStyle(frameBG, frameLit, frameLit, mediumFont);
        frameCheckedStyle.pressedOffsetX = 1;
        frameCheckedStyle.pressedOffsetY = -1;
    }

    public static void dispose() {
        uiAtlas.dispose();
    }
}
