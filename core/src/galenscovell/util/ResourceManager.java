package galenscovell.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
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
    public static TextureAtlas uiAtlas, tileAtlas, organicAtlas, inanimateAtlas;
    public static Label.LabelStyle detailStyle, mediumStyle, menuStyle, titleStyle;
    public static NinePatchDrawable buttonUp, buttonDown, frameBG, frameLit;
    public static TextButton.TextButtonStyle buttonStyle, frameStyle, toggleButtonStyle;
    public static Sprite destinationMarker;
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
        smallParams.fontFileName = "ui/kenpixel_mini_square.ttf";
        smallParams.fontParameters.size = 12;
        assetManager.load("smallFont.ttf", BitmapFont.class, smallParams);

        FreetypeFontLoader.FreeTypeFontLoaderParameter mediumParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        mediumParams.fontFileName = "ui/kenpixel_square.ttf";
        mediumParams.fontParameters.size = 18;
        assetManager.load("mediumFont.ttf", BitmapFont.class, mediumParams);

        FreetypeFontLoader.FreeTypeFontLoaderParameter largeParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        largeParams.fontFileName = "ui/kenpixel_square.ttf";
        largeParams.fontParameters.size = 24;
        assetManager.load("largeFont.ttf", BitmapFont.class, largeParams);

        FreetypeFontLoader.FreeTypeFontLoaderParameter extraLargeParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        extraLargeParams.fontFileName = "ui/kenpixel_blocks.ttf";
        extraLargeParams.fontParameters.size = 48;
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

        buttonUp = new NinePatchDrawable(uiAtlas.createPatch("button_up"));
        buttonDown = new NinePatchDrawable(uiAtlas.createPatch("button_down"));
        buttonStyle = new TextButton.TextButtonStyle(buttonUp, buttonDown, buttonUp, assetManager.get("mediumFont.ttf", BitmapFont.class));

        toggleButtonStyle = new TextButton.TextButtonStyle(frameLit, frameBG, frameLit, assetManager.get("mediumFont.ttf", BitmapFont.class));

        frameBG = new NinePatchDrawable(uiAtlas.createPatch("framedbg"));
        frameLit = new NinePatchDrawable(uiAtlas.createPatch("framedlit"));
        frameStyle = new TextButton.TextButtonStyle(frameBG, frameLit, frameBG, assetManager.get("mediumFont.ttf", BitmapFont.class));
        frameStyle.pressedOffsetX = 1;
        frameStyle.pressedOffsetY = -1;

        destinationMarker = new Sprite(uiAtlas.createSprite("destinationMarker"));
        destinationMarker.flip(false, true);

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
