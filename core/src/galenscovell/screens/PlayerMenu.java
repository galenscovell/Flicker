
/**
 * PLAYERMENU CLASS
 * HUD table for player attributes/info.
 */

package galenscovell.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import galenscovell.util.ResourceManager;


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
        mainTable.setBackground(ResourceManager.frameBG);

        this.add(mainTable).width(300).height(300).expand().right();
    }

    public void updateStats() {
        // Update all displayed player stats when called.
    }
}
