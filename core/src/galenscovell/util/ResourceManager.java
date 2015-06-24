package galenscovell.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * RESOURCE MANAGER
 * Handles loading and disposal of game assets via AssetManager.
 *
 * @author Galen Scovell
 */

public class ResourceManager {
    public static AssetManager assetManager;
    public static TextureAtlas uiAtlas;

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

    public static void create() {
        assetManager = new AssetManager();
        load();
    }

    public static void load() {
        assetManager.load("ui/uiAtlas.pack", TextureAtlas.class);

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter smallParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        smallParams.fontFileName = "ui/PressStart2P.ttf";
        smallParams.fontParameters.size = 9;
        assetManager.load("smallFont.ttf", BitmapFont.class, smallParams);

        FreetypeFontLoader.FreeTypeFontLoaderParameter mediumParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        mediumParams.fontFileName = "ui/PressStart2P.ttf";
        mediumParams.fontParameters.size = 16;
        assetManager.load("mediumFont.ttf", BitmapFont.class, mediumParams);

        FreetypeFontLoader.FreeTypeFontLoaderParameter largeParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        largeParams.fontFileName = "ui/PressStart2P.ttf";
        largeParams.fontParameters.size = 24;
        assetManager.load("largeFont.ttf", BitmapFont.class, largeParams);

        FreetypeFontLoader.FreeTypeFontLoaderParameter extraLargeParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        extraLargeParams.fontFileName = "ui/PressStart2P.ttf";
        extraLargeParams.fontParameters.size = 48;
        assetManager.load("extraLargeFont.ttf", BitmapFont.class, extraLargeParams);
    }

    public static void done() {
        uiAtlas = assetManager.get("ui/uiAtlas.pack", TextureAtlas.class);

        detailStyle = new Label.LabelStyle(assetManager.get("smallFont.ttf", BitmapFont.class), Color.WHITE);
        mediumStyle = new Label.LabelStyle(assetManager.get("mediumFont.ttf", BitmapFont.class), Color.WHITE);
        menuStyle = new Label.LabelStyle(assetManager.get("largeFont.ttf", BitmapFont.class), Color.WHITE);
        titleStyle = new Label.LabelStyle(assetManager.get("extraLargeFont.ttf", BitmapFont.class), Color.WHITE);

        hudBG = new NinePatchDrawable(uiAtlas.createPatch("buttonbg"));
        buttonDown = new NinePatchDrawable(uiAtlas.createPatch("buttondown"));
        buttonStyle = new TextButton.TextButtonStyle(hudBG, buttonDown, hudBG, assetManager.get("mediumFont.ttf", BitmapFont.class));
        buttonStyle.pressedOffsetX = 1;
        buttonStyle.pressedOffsetY = -1;

        colorButtonBG = new NinePatchDrawable(uiAtlas.createPatch("tealbuttonbg"));
        colorButtonDown = new NinePatchDrawable(uiAtlas.createPatch("tealbuttondown"));
        colorButtonStyle = new TextButton.TextButtonStyle(colorButtonBG, colorButtonDown, colorButtonBG, assetManager.get("mediumFont.ttf", BitmapFont.class));
        colorButtonStyle.pressedOffsetX = 1;
        colorButtonStyle.pressedOffsetY = -1;

        frameBG = new NinePatchDrawable(uiAtlas.createPatch("framedbg"));
        frameLit = new NinePatchDrawable(uiAtlas.createPatch("framedlit"));
        frameStyle = new TextButton.TextButtonStyle(frameBG, frameLit, frameBG, assetManager.get("mediumFont.ttf", BitmapFont.class));
        frameStyle.pressedOffsetX = 1;
        frameStyle.pressedOffsetY = -1;

        frameCheckedStyle = new TextButton.TextButtonStyle(frameBG, frameLit, frameLit, assetManager.get("mediumFont.ttf", BitmapFont.class));
        frameCheckedStyle.pressedOffsetX = 1;
        frameCheckedStyle.pressedOffsetY = -1;

    }

    public static void dispose() {
        assetManager.dispose();
        uiAtlas.dispose();
    }
}
