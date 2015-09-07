package galenscovell.screens.components;

import galenscovell.screens.HudStage;
import galenscovell.screens.MainMenuScreen;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * OPTIONS MENU
 * HUD popup table for saving/quitting.
 *
 * @author Galen Scovell
 */

public class OptionsMenu extends Table {
    private HudStage root;
    private MainMenuScreen mainMenu;
    private Table mainTable;

    public OptionsMenu(HudStage root) {
        this.root = root;
        createGameVersion();
    }

    public OptionsMenu(MainMenuScreen mainMenu) {
        this.mainMenu = mainMenu;
        createMainVersion();
    }

    public void createGameVersion() {
        this.setFillParent(true);
        this.mainTable = new Table();
        mainTable.setBackground(ResourceManager.frameUpDec);

        TextButton sfxButton = new TextButton("Sound", ResourceManager.toggleButtonStyle);
        sfxButton.setChecked(ResourceManager.prefs.getBoolean("sfx"));
        sfxButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (ResourceManager.prefs.getBoolean("sfx")) {
                    ResourceManager.prefs.putBoolean("sfx", false);
                } else {
                    ResourceManager.prefs.putBoolean("sfx", true);
                }
            }
        });
        TextButton musicButton = new TextButton("Music", ResourceManager.toggleButtonStyle);
        musicButton.setChecked(ResourceManager.prefs.getBoolean("music"));
        musicButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (ResourceManager.prefs.getBoolean("music")) {
                    ResourceManager.prefs.putBoolean("music", false);
                } else {
                    ResourceManager.prefs.putBoolean("music", true);
                }
            }
        });
        TextButton mainMenuButton = new TextButton("Main Menu", ResourceManager.buttonStyle);
        mainMenuButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                root.returnToMainMenu();
            }
        });
        TextButton quitButton = new TextButton("Exit Flicker", ResourceManager.buttonStyle);
        quitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        mainTable.add(sfxButton).width(260).height(80).padBottom(8).expand().fill();
        mainTable.row();
        mainTable.add(musicButton).width(260).height(80).padBottom(8).expand().fill();
        mainTable.row();
        mainTable.add(mainMenuButton).width(260).height(80).padBottom(8).expand().fill();
        mainTable.row();
        mainTable.add(quitButton).width(260).height(80).expand().fill();

        this.add(mainTable).width(340).height(400).expand().center();
    }

    public void createMainVersion() {
        this.setFillParent(true);
        this.mainTable = new Table();
        mainTable.setBackground(ResourceManager.frameUpDec);

        TextButton sfxButton = new TextButton("Sound", ResourceManager.toggleButtonStyle);
        sfxButton.setChecked(ResourceManager.prefs.getBoolean("sfx"));
        sfxButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (ResourceManager.prefs.getBoolean("sfx")) {
                    ResourceManager.prefs.putBoolean("sfx", false);
                } else {
                    ResourceManager.prefs.putBoolean("sfx", true);
                }
            }
        });
        TextButton musicButton = new TextButton("Music", ResourceManager.toggleButtonStyle);
        musicButton.setChecked(ResourceManager.prefs.getBoolean("music"));
        musicButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (ResourceManager.prefs.getBoolean("music")) {
                    ResourceManager.prefs.putBoolean("music", false);
                } else {
                    ResourceManager.prefs.putBoolean("music", true);
                }
            }
        });
        TextButton returnButton = new TextButton("Return", ResourceManager.buttonStyle);
        returnButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                mainMenu.closeOptions();
            }
        });

        mainTable.add(sfxButton).width(260).height(80).padBottom(8).expand().fill();
        mainTable.row();
        mainTable.add(musicButton).width(260).height(80).padBottom(8).expand().fill();
        mainTable.row();
        mainTable.add(returnButton).width(260).height(80).expand().fill();

        this.add(mainTable).width(340).height(360).expand().center();
    }
}
