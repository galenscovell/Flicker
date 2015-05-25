
/**
 * PLAYERMENU CLASS
 * HUD table for player attributes/info.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.utils.Align;
import galenscovell.util.ScreenResources;


public class PlayerMenu extends Table {
    private HudDisplay root;
    private Table mainTable;


    public PlayerMenu(HudDisplay root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        this.mainTable = new Table();
        mainTable.setBackground(ScreenResources.frameBG);

        TextButton returnButton = new TextButton("Return to Game", ScreenResources.colorButtonStyle);
        returnButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        mainTable.add(returnButton).width(280).height(60).expand().fill();

        this.add(mainTable).width(300).height(200).expand().center();
    }
}
