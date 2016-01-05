package galenscovell.flicker.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import galenscovell.flicker.ui.HudStage;
import galenscovell.flicker.util.ResourceManager;

public class InventoryMenu extends Table {
    private final HudStage root;
    private Table mainTable;

    public InventoryMenu(HudStage root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        this.mainTable = new Table();
        mainTable.setBackground(ResourceManager.frameUpDec);


        this.add(mainTable).width(300).height(400).expand().center();
    }
}

