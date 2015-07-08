package galenscovell.screens.components;

import galenscovell.screens.HudStage;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

/**
 * EXAMINEINFO POPUP
 * Displays examined entity/object info, focused on entity/object location.
 *
 * @author Galen Scovell
 */

public class ExamineInfoPopup extends Table {
    private HudStage root;

    public ExamineInfoPopup(HudStage root, String info, Sprite target) {
        this.root = root;
        create(info, target);
    }

    public void create(String info, Sprite target) {
        this.setFillParent(true);
        Table popup = new Table();
        popup.setBackground(ResourceManager.frameBG);
        Label examineLabel = new Label(info, ResourceManager.detailStyle);
        examineLabel.setWrap(true);
        examineLabel.setAlignment(Align.left);
        Image spriteImage = new Image(target);
        popup.add(spriteImage).width(48).height(48).padRight(10);
        popup.add(examineLabel).width(140).height(90);
        this.add(popup).width(220).height(100).expand().center();
    }
}
