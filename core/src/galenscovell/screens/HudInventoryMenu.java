
/**
 * HUD-INVENTORYMENU
 * HUD popup table for player inventory.
 */

package galenscovell.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import galenscovell.util.ResourceManager;


public class HudInventoryMenu extends Table {
    private HudDisplay root;
    private Table mainTable;


    public HudInventoryMenu(HudDisplay root) {
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

