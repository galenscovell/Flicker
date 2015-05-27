
/**
 * INVENTORYMENU CLASS
 * HUD table for player inventory.
 */

package galenscovell.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import galenscovell.util.ResourceManager;


public class InventoryMenu extends Table {
    private HudDisplay root;
    private Table mainTable;


    public InventoryMenu(HudDisplay root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        this.mainTable = new Table();
        mainTable.setBackground(ResourceManager.frameBG);


        this.add(mainTable).width(300).height(300).expand().left();
    }
}

