package galenscovell.util;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class ResourceManager {
    public static AssetManager assetManager;
    public static TextureAtlas uiAtlas, tileAtlas, organicAtlas, inanimateAtlas;
    public static Label.LabelStyle tinyStyle, detailStyle, mediumStyle, menuStyle, titleStyle;
    public static NinePatchDrawable buttonUp, buttonDown, panelUp, panelDown, frameUp, frameDown, frameUpDec, panelViewport;
    public static TextButton.TextButtonStyle buttonStyle, panelStyle, frameStyle, toggleButtonStyle;
    public static Sprite highlightBlue, highlightOrange;
    public static TextureRegion mainMenuBG;
    public static Preferences prefs;

    public static void create() {
        assetManager = new AssetManager();
        load();
    }

    public static void load() {
        assetManager.load("atlas/uiAtlas.pack", TextureAtlas.class);
        assetManager.load("atlas/tileAtlas.pack", TextureAtlas.class);
        assetManager.load("atlas/organicAtlas.pack", TextureAtlas.class);
        assetManager.load("atlas/inanimateAtlas.pack", TextureAtlas.class);

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        generateFont("ui/nevis.ttf", 14, 0, Color.WHITE, Color.BLACK, "tinyFont.ttf");
        generateFont("ui/nevis.ttf", 18, 0, Color.DARK_GRAY, Color.BLACK, "smallFont.ttf");
        generateFont("ui/nevis.ttf", 26, 0, new Color(0.9f, 0.7f, 0.41f, 1), Color.BLACK, "mediumFont.ttf");
        generateFont("ui/nevis.ttf", 48, 0, Color.WHITE, Color.BLACK, "largeFont.ttf");
        generateFont("ui/nevis.ttf", 72, 4, new Color(0.35f, 0.28f, 0.16f, 1), new Color(0.9f, 0.7f, 0.41f, 1), "extraLargeFont.ttf");
    }

    private static void generateFont(String fontName, int size, int borderWidth, Color fontColor, Color borderColor, String outName) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        params.fontFileName = fontName;
        params.fontParameters.size = size;
        params.fontParameters.borderWidth = borderWidth;
        params.fontParameters.borderColor = borderColor;
        params.fontParameters.color = fontColor;
        params.fontParameters.magFilter = Texture.TextureFilter.Linear;
        params.fontParameters.minFilter = Texture.TextureFilter.Linear;
        assetManager.load(outName, BitmapFont.class, params);
    }

    public static void done() {
        uiAtlas = assetManager.get("atlas/uiAtlas.pack", TextureAtlas.class);
        tileAtlas = assetManager.get("atlas/tileAtlas.pack", TextureAtlas.class);
        organicAtlas = assetManager.get("atlas/organicAtlas.pack", TextureAtlas.class);
        inanimateAtlas = assetManager.get("atlas/inanimateAtlas.pack", TextureAtlas.class);

        tinyStyle = new Label.LabelStyle(assetManager.get("tinyFont.ttf", BitmapFont.class), Color.WHITE);
        detailStyle = new Label.LabelStyle(assetManager.get("smallFont.ttf", BitmapFont.class), Color.WHITE);
        mediumStyle = new Label.LabelStyle(assetManager.get("mediumFont.ttf", BitmapFont.class), Color.WHITE);
        menuStyle = new Label.LabelStyle(assetManager.get("largeFont.ttf", BitmapFont.class), Color.WHITE);
        titleStyle = new Label.LabelStyle(assetManager.get("extraLargeFont.ttf", BitmapFont.class), Color.WHITE);

        buttonUp = new NinePatchDrawable(uiAtlas.createPatch("buttonup_brown"));
        buttonDown = new NinePatchDrawable(uiAtlas.createPatch("buttondown_brown"));
        buttonStyle = new TextButton.TextButtonStyle(buttonUp, buttonDown, buttonUp, assetManager.get("mediumFont.ttf", BitmapFont.class));
        buttonStyle.pressedOffsetY = -2;

        toggleButtonStyle = new TextButton.TextButtonStyle(frameUp, frameDown, frameDown, assetManager.get("mediumFont.ttf", BitmapFont.class));

        frameUp = new NinePatchDrawable(uiAtlas.createPatch("frameup_brown"));
        frameDown = new NinePatchDrawable(uiAtlas.createPatch("framedown_brown"));
        frameStyle = new TextButton.TextButtonStyle(frameUp, frameDown, frameUp, assetManager.get("mediumFont.ttf", BitmapFont.class));
        frameStyle.pressedOffsetY = -1;

        panelUp = new NinePatchDrawable(uiAtlas.createPatch("paneldown_brown"));
        panelDown = new NinePatchDrawable(uiAtlas.createPatch("paneldown_brown"));
        panelStyle = new TextButton.TextButtonStyle(panelUp, panelDown, panelUp, assetManager.get("mediumFont.ttf", BitmapFont.class));
        panelStyle.pressedOffsetY = -2;

        frameUpDec = new NinePatchDrawable(uiAtlas.createPatch("frameup_dec_brown"));

        panelViewport = new NinePatchDrawable(uiAtlas.createPatch("panelViewport"));

        highlightBlue = new Sprite(uiAtlas.createSprite("highlight_blue"));
        highlightBlue.flip(false, true);
        highlightOrange = new Sprite(uiAtlas.createSprite("highlight_orange"));
        highlightOrange.flip(false, true);

        mainMenuBG = new TextureRegion(uiAtlas.findRegion("clouds_bg"));

        // Load user preferences
        prefs = Gdx.app.getPreferences("flicker_settings");
        prefs.putBoolean("sfx", true);
        prefs.putBoolean("music", true);
        prefs.flush();
    }

    public static void dispose() {
        assetManager.dispose();
        uiAtlas.dispose();
        tileAtlas.dispose();
        organicAtlas.dispose();
        inanimateAtlas.dispose();
    }
}
