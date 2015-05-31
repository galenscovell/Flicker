
/**
 * MAINMENU-OPTIONS
 * MainMenu popup table for setting tweaking.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import galenscovell.util.ResourceManager;


public class MainMenuOptions extends Table {
    private MainMenuScreen root;
    private Table mainTable;


    public MainMenuOptions(MainMenuScreen root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        this.mainTable = new Table();
        mainTable.setBackground(ResourceManager.frameBG);

        TextButton soundButton = new TextButton("Sound Effects", ResourceManager.colorButtonStyle);
        soundButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Audio settings
            }
        });
        TextButton musicButton = new TextButton("Music", ResourceManager.colorButtonStyle);
        musicButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Graphics settings
            }
        });
        TextButton brightnessButton = new TextButton("Brightness", ResourceManager.colorButtonStyle);
        brightnessButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Graphics settings
            }
        });
        TextButton returnButton = new TextButton("Return", ResourceManager.colorButtonStyle);
        returnButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                returnToMain();
            }
        });

        mainTable.add(soundButton).width(280).height(60).padBottom(4).expand().fill();
        mainTable.row();
        mainTable.add(musicButton).width(280).height(60).padBottom(4).expand().fill();
        mainTable.row();
        mainTable.add(brightnessButton).width(280).height(60).padBottom(4).expand().fill();
        mainTable.row();
        mainTable.add(returnButton).width(280).height(60).expand().fill();

        this.add(mainTable).width(300).height(300).expand().center();
    }

    private void returnToMain() {
        root.menuOperation(this, 0);
    }
}

