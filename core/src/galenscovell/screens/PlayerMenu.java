
/**
 * PLAYERMENU CLASS
 * HUD table for player attributes/info.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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

        this.add(mainTable).width(300).height(300).expand().right();
    }

    public void updateStats() {
        // Update all displayed player stats when called.
    }
}
