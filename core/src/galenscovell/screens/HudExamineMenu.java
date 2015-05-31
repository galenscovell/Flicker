
/**
 * HUDEXAMINE-MENU
 * HUD popup table for examine action.
 */

package galenscovell.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import galenscovell.util.ResourceManager;


public class HudExamineMenu extends Table {
    private HudDisplay root;
    private Table mainTable;


    public HudExamineMenu(HudDisplay root) {
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
