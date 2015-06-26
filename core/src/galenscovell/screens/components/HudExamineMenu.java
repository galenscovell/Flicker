package galenscovell.screens.components;

import galenscovell.screens.HudStage;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

/**
 * EXAMINE MENU
 * HUD popup table for examine action.
 *
 * @author Galen Scovell
 */

public class HudExamineMenu extends Table {
    private HudStage root;
    private Table mainTable;

    public HudExamineMenu(HudStage root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        this.mainTable = new Table();
        this.padBottom(20);
        mainTable.setBackground(ResourceManager.frameBG);
        Label examineLabel = new Label("Examine mode.", ResourceManager.detailStyle);
        examineLabel.setAlignment(Align.center);
        mainTable.add(examineLabel).width(160).height(40).expand().fill().center();
        this.add(mainTable).width(160).height(50).expand().bottom();
    }
}
