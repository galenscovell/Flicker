package galenscovell.screens.components;

import galenscovell.screens.HudStage;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

/**
 * EXAMINE MENU
 * HUD popup table for examine mode.
 *
 * @author Galen Scovell
 */

public class ExaminePopup extends Table {
    private HudStage root;
    private Table mainTable;

    public ExaminePopup(HudStage root) {
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
        mainTable.add(examineLabel).width(170).height(50).expand().fill().center();
        this.add(mainTable).width(180).height(60).expand().bottom();
    }
}
