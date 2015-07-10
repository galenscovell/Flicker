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

public class ExamineModePopup extends Table {
    private HudStage root;

    public ExamineModePopup(HudStage root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        Table popup = new Table();
        this.padBottom(90);
        popup.setBackground(ResourceManager.buttonDarkDown);
        Label examineLabel = new Label("Examine mode", ResourceManager.detailStyle);
        examineLabel.setAlignment(Align.center);
        popup.add(examineLabel).width(170).height(50).expand().fill().center();
        this.add(popup).width(180).height(60).expand().bottom();
    }
}
