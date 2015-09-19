package galenscovell.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import galenscovell.ui.HudStage;
import galenscovell.util.ResourceManager;

public class ExaminePopup extends Table {
    private HudStage root;

    public ExaminePopup(HudStage root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        Table popup = new Table();
        this.padBottom(90);
        popup.setBackground(ResourceManager.frameUp);
        Label examineLabel = new Label("Examine mode", ResourceManager.detailStyle);
        examineLabel.setAlignment(Align.center);
        popup.add(examineLabel).width(170).height(60).expand().fill().center();
        this.add(popup).width(180).height(70).expand().bottom();
    }
}
