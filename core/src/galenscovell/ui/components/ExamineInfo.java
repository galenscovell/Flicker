package galenscovell.ui.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import galenscovell.util.ResourceManager;

public class ExamineInfo extends Table {

    public ExamineInfo(String info, Sprite target) {
        create(info, target);
    }

    public void create(String info, Sprite target) {
        this.setName("examineBox");
        this.setFillParent(true);
        this.setPosition(0, 300);
        Table mainTable = new Table();
        mainTable.setBackground(ResourceManager.frameUp);

        Label examineLabel = new Label(info, ResourceManager.tinyStyle);
        examineLabel.setWrap(true);
        examineLabel.setAlignment(Align.left, Align.center);

        Sprite copySprite = new Sprite(target);
        copySprite.flip(false, true);
        Image spriteImage = new Image(copySprite);

        mainTable.add(spriteImage).width(48).height(48).left();
        mainTable.add(examineLabel).expand().fill().padLeft(5);

        this.add(mainTable).width(300).height(110).expand().center();
    }
}
