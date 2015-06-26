package galenscovell.screens.components;

import galenscovell.screens.HudStage;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * INTERACT POPUP
 * Dynamic display showing available interactive objects/entities in range.
 *
 * @author Galen Scovell
 */

public class InteractPopup extends Table {
    private HudStage root;
    private Table mainTable;

    public InteractPopup(HudStage root) {
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
