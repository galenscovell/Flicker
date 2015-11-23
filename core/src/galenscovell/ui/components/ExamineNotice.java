package galenscovell.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import galenscovell.ui.HudStage;
import galenscovell.util.ResourceManager;

public class ExamineNotice extends Table {
    private final HudStage root;

    public ExamineNotice(HudStage root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        this.padBottom(80);

        Table popup = new Table();
        popup.setBackground(ResourceManager.frameUp);

        Label examineLabel = new Label("Examine mode", ResourceManager.tinyStyle);
        examineLabel.setAlignment(Align.center);

        popup.add(examineLabel).width(140).height(50).expand().fill().center();

        this.add(popup).width(150).height(60).expand().bottom();
    }
}
