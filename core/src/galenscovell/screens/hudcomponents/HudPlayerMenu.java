package galenscovell.screens.hudcomponents;

import galenscovell.screens.HudDisplay;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * PLAYER MENU
 * HUD popup table for player attributes/info.
 *
 * @author Galen Scovell
 */

public class HudPlayerMenu extends Table {
    private HudDisplay root;
    private Table mainTable;

    public HudPlayerMenu(HudDisplay root) {
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
