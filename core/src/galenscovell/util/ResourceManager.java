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
    public static Label.LabelStyle detailStyle, mediumStyle, menuStyle, titleStyle;
    public static NinePatchDrawable buttonUp, buttonDown, panelUp, panelDown, frameUp, frameDown, frameUpDec;
    public static TextButton.TextButtonStyle buttonStyle, panelStyle, frameStyle, toggleButtonStyle;
    public static Sprite highlight;
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

        FreetypeFontLoader.FreeTypeFontLoaderParameter smallParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        smallParams.fontFileName = "ui/nevis.ttf";
        smallParams.fontParameters.size = 18;
        smallParams.fontParameters.magFilter = Texture.TextureFilter.Linear;
        smallParams.fontParameters.minFilter = Texture.TextureFilter.Linear;
        smallParams.fontParameters.color = Color.DARK_GRAY;
        assetManager.load("smallFont.ttf", BitmapFont.class, smallParams);

        FreetypeFontLoader.FreeTypeFontLoaderParameter mediumParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        mediumParams.fontFileName = "ui/nevis.ttf";
        mediumParams.fontParameters.size = 26;
        mediumParams.fontParameters.color = new Color(0.9f, 0.7f, 0.41f, 1);
        mediumParams.fontParameters.magFilter = Texture.TextureFilter.Linear;
        mediumParams.fontParameters.minFilter = Texture.TextureFilter.Linear;
        assetManager.load("mediumFont.ttf", BitmapFont.class, mediumParams);

        FreetypeFontLoader.FreeTypeFontLoaderParameter largeParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        largeParams.fontFileName = "ui/nevis.ttf";
        largeParams.fontParameters.size = 48;
        largeParams.fontParameters.magFilter = Texture.TextureFilter.Linear;
        largeParams.fontParameters.minFilter = Texture.TextureFilter.Linear;
        assetManager.load("largeFont.ttf", BitmapFont.class, largeParams);

        FreetypeFontLoader.FreeTypeFontLoaderParameter extraLargeParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        extraLargeParams.fontFileName = "ui/nevis.ttf";
        extraLargeParams.fontParameters.size = 72;
        extraLargeParams.fontParameters.borderWidth = 4;
        extraLargeParams.fontParameters.borderColor = new Color(0.35f, 0.28f, 0.16f, 1);
        extraLargeParams.fontParameters.color = new Color(0.9f, 0.7f, 0.41f, 1);
        assetManager.load("extraLargeFont.ttf", BitmapFont.class, extraLargeParams);
    }

    public static void done() {
        uiAtlas = assetManager.get("atlas/uiAtlas.pack", TextureAtlas.class);
        tileAtlas = assetManager.get("atlas/tileAtlas.pack", TextureAtlas.class);
        organicAtlas = assetManager.get("atlas/organicAtlas.pack", TextureAtlas.class);
        inanimateAtlas = assetManager.get("atlas/inanimateAtlas.pack", TextureAtlas.class);

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

        highlight = new Sprite(uiAtlas.createSprite("highlight"));
        highlight.flip(false, true);

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
        // plantAtlas.dispose();
    }
}
