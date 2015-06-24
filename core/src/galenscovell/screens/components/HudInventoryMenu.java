package galenscovell.screens.components;

import galenscovell.screens.HudDisplay;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * INVENTORY MENU
 * HUD popup table for player inventory.
 *
 * @author Galen Scovell
 */

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

