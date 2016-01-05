package galenscovell.flicker.util;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class ResourceManager {
    public static AssetManager assetManager;
    public static TextureAtlas uiAtlas, numberAtlas, tileAtlas, organicAtlas, inanimateAtlas;
    public static NinePatchDrawable buttonUp, buttonDown, panelUp, panelDown, frameUp, frameDown, frameUpDec;

    public static LabelStyle tinyStyle, detailStyle, mediumStyle, menuStyle, titleStyle;
    public static TextButtonStyle fullButtonStyle, panelButtonStyle, frameButtonStyle, toggleButtonStyle;

    public static Sprite highlightBlue, highlightOrange;
    public static Sprite[] numbers;
    public static TextureRegion mainMenuBG;

    public static Preferences prefs;

    public static void create() {
        assetManager = new AssetManager();
        load();
    }

    public static void load() {
        assetManager.load("atlas/uiAtlas.pack", TextureAtlas.class);
        assetManager.load("atlas/numberAtlas.pack", TextureAtlas.class);
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

    public static void done() {
        uiAtlas = assetManager.get("atlas/uiAtlas.pack", TextureAtlas.class);
        numberAtlas = assetManager.get("atlas/numberAtlas.pack", TextureAtlas.class);
        tileAtlas = assetManager.get("atlas/tileAtlas.pack", TextureAtlas.class);
        organicAtlas = assetManager.get("atlas/organicAtlas.pack", TextureAtlas.class);
        inanimateAtlas = assetManager.get("atlas/inanimateAtlas.pack", TextureAtlas.class);

        loadNinepatches();
        loadLabelStyles();
        loadButtonStyles();
        loadSprites();

        // Load user preferences
        prefs = Gdx.app.getPreferences("flicker_settings");
        prefs.putBoolean("sfx", true);
        prefs.putBoolean("music", true);
        prefs.flush();
    }

    public static void dispose() {
        assetManager.dispose();
        uiAtlas.dispose();
        numberAtlas.dispose();
        tileAtlas.dispose();
        organicAtlas.dispose();
        inanimateAtlas.dispose();
    }


    /***************************************************
     * Font and Resource Generation
     */
    private static void generateFont(String fontName, int size, int borderWidth, Color fontColor, Color borderColor, String outName) {
        FreeTypeFontLoaderParameter params = new FreeTypeFontLoaderParameter();
        params.fontFileName = fontName;
        params.fontParameters.size = size;
        params.fontParameters.borderWidth = borderWidth;
        params.fontParameters.borderColor = borderColor;
        params.fontParameters.color = fontColor;
        params.fontParameters.magFilter = TextureFilter.Linear;
        params.fontParameters.minFilter = TextureFilter.Linear;
        assetManager.load(outName, BitmapFont.class, params);
    }

    private static void loadNinepatches() {
        frameUp = new NinePatchDrawable(uiAtlas.createPatch("frameup_brown"));
        frameDown = new NinePatchDrawable(uiAtlas.createPatch("framedown_brown"));

        panelUp = new NinePatchDrawable(uiAtlas.createPatch("paneldown_brown"));
        panelDown = new NinePatchDrawable(uiAtlas.createPatch("paneldown_brown"));

        buttonUp = new NinePatchDrawable(uiAtlas.createPatch("buttonup_brown"));
        buttonDown = new NinePatchDrawable(uiAtlas.createPatch("buttondown_brown"));

        frameUpDec = new NinePatchDrawable(uiAtlas.createPatch("frameup_dec_brown"));
    }

    private static void loadLabelStyles() {
        tinyStyle = new LabelStyle(assetManager.get("tinyFont.ttf", BitmapFont.class), Color.WHITE);
        detailStyle = new LabelStyle(assetManager.get("smallFont.ttf", BitmapFont.class), Color.WHITE);
        mediumStyle = new LabelStyle(assetManager.get("mediumFont.ttf", BitmapFont.class), Color.WHITE);
        menuStyle = new LabelStyle(assetManager.get("largeFont.ttf", BitmapFont.class), Color.WHITE);
        titleStyle = new LabelStyle(assetManager.get("extraLargeFont.ttf", BitmapFont.class), Color.WHITE);
    }

    private static void loadButtonStyles() {
        fullButtonStyle = new TextButtonStyle(buttonUp, buttonDown, buttonUp, assetManager.get("mediumFont.ttf", BitmapFont.class));
        fullButtonStyle.pressedOffsetY = -2;

        toggleButtonStyle = new TextButtonStyle(frameUp, frameDown, frameDown, assetManager.get("mediumFont.ttf", BitmapFont.class));

        frameButtonStyle = new TextButtonStyle(frameUp, frameDown, frameUp, assetManager.get("mediumFont.ttf", BitmapFont.class));
        frameButtonStyle.pressedOffsetY = -1;

        panelButtonStyle = new TextButtonStyle(panelUp, panelDown, panelUp, assetManager.get("mediumFont.ttf", BitmapFont.class));
        panelButtonStyle.pressedOffsetY = -2;
    }

    private static void loadSprites() {
        highlightBlue = new Sprite(uiAtlas.createSprite("highlight_blue"));
        highlightBlue.flip(false, true);
        highlightOrange = new Sprite(uiAtlas.createSprite("highlight_orange"));
        highlightOrange.flip(false, true);

        mainMenuBG = new TextureRegion(uiAtlas.findRegion("clouds_bg"));

        numbers = new Sprite[10];
        for (int i = 0; i <= 9; i++) {
            numbers[i] = new Sprite(numberAtlas.createSprite(String.valueOf(i)));
            numbers[i].flip(false, true);
        }
    }
}
