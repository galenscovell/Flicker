
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

        TextButton audioButton = new TextButton("Audio", ResourceManager.colorButtonStyle);
        audioButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Audio settings
            }
        });
        TextButton graphicsButton = new TextButton("Graphics", ResourceManager.colorButtonStyle);
        graphicsButton.addListener(new ClickListener() {
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

        mainTable.add(audioButton).width(280).height(60).padBottom(4).expand().fill();
        mainTable.row();
        mainTable.add(graphicsButton).width(280).height(60).padBottom(4).expand().fill();
        mainTable.row();
        mainTable.add(returnButton).width(280).height(60).expand().fill();

        this.add(mainTable).width(300).height(200).expand().center();
    }

    private void returnToMain() {
        root.menuOperation(this, 0);
    }
}

