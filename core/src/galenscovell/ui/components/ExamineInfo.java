package galenscovell.ui.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import galenscovell.ui.HudStage;
import galenscovell.util.ResourceManager;

public class ExamineInfo extends Table {
    private HudStage root;

    public ExamineInfo(HudStage root, String info, Sprite target) {
        this.root = root;
        create(info, target);
    }

    public void create(String info, Sprite target) {
        this.setFillParent(true);
        Table popup = new Table();
        popup.setBackground(ResourceManager.frameUp);
        Label examineLabel = new Label(info, ResourceManager.detailStyle);
        examineLabel.setWrap(true);
        examineLabel.setAlignment(Align.left);
        Image spriteImage = new Image(target);
        popup.add(spriteImage).width(48).height(48).padRight(10);
        popup.add(examineLabel).width(140).height(90);
        this.add(popup).width(220).height(100).expand().center();
    }
}
