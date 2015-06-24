package galenscovell.screens.components;

import galenscovell.screens.HudDisplay;
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

public class HudOptionsMenu extends Table {
    private HudDisplay root;
    private Table mainTable;

    public HudOptionsMenu(HudDisplay root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        this.mainTable = new Table();
        mainTable.setBackground(ResourceManager.frameBG);

        TextButton settingsButton = new TextButton("Settings", ResourceManager.colorButtonStyle);
        settingsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // TODO: In-game settings
            }
        });
        TextButton mainMenuButton = new TextButton("Main Menu", ResourceManager.colorButtonStyle);
        mainMenuButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                root.returnToMainMenu();
            }
        });
        TextButton quitButton = new TextButton("Exit Flicker", ResourceManager.colorButtonStyle);
        quitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        mainTable.add(settingsButton).width(280).height(60).padBottom(4).expand().fill();
        mainTable.row();
        mainTable.add(mainMenuButton).width(280).height(60).padBottom(4).expand().fill();
        mainTable.row();
        mainTable.add(quitButton).width(280).height(60).expand().fill();

        this.add(mainTable).width(300).height(200).expand().center();
    }
}
