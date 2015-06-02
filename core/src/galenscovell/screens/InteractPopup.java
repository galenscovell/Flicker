
/**
 * INTERACT POPUP
 * Dynamic display showing available interactive objects/entities in range.
 */

package galenscovell.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import galenscovell.util.ResourceManager;


public class InteractPopup extends Table {
    private HudDisplay root;
    private Table mainTable;


    public InteractPopup(HudDisplay root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        this.mainTable = new Table();
        mainTable.setBackground(ResourceManager.frameBG);

        this.add(mainTable).width(60).height(60).expand().right();
    }
}
