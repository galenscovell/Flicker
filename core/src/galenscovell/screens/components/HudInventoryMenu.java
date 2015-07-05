package galenscovell.screens.components;

import galenscovell.screens.HudStage;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * INVENTORY MENU
 * HUD popup table for player inventory.
 *
 * @author Galen Scovell
 */

public class HudInventoryMenu extends Table {
    private HudStage root;
    private Table mainTable;

    public HudInventoryMenu(HudStage root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        this.mainTable = new Table();
        mainTable.setBackground(ResourceManager.frameBG);


        this.add(mainTable).width(300).height(300).expand().center();
    }
}

