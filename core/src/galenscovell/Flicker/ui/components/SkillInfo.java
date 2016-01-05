package galenscovell.flicker.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import galenscovell.flicker.util.*;

public class SkillInfo extends Table {

    public SkillInfo(String[] info) {
        create(info);
    }

    public void create(String[] info) {
        this.setName("skillInfo");
        this.setFillParent(true);
        this.setPosition(0, 0.4f * Constants.UI_Y);

        Table mainTable = new Table();
        mainTable.setBackground(ResourceManager.frameUp);

        Label skillTitle = new Label(info[0], ResourceManager.mediumStyle);
        skillTitle.setAlignment(Align.center, Align.center);

        Label skillLabel = new Label(info[1], ResourceManager.tinyStyle);
        skillLabel.setWrap(true);
        skillLabel.setAlignment(Align.center, Align.center);

        mainTable.add(skillTitle).width(80).padLeft(5).padRight(5);
        mainTable.add(skillLabel).expand().fill().padLeft(5).padRight(5);

        this.add(mainTable).width(300).height(110).expand().center();
    }
}

