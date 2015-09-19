package galenscovell.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import galenscovell.ui.HudStage;
import galenscovell.util.ResourceManager;

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
        mainTable.setBackground(ResourceManager.frameUp);

        this.add(mainTable).width(60).height(60).expand().right();
    }
}
