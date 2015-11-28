package galenscovell.ui.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import galenscovell.ui.HudStage;
import galenscovell.util.*;

public class SkillMenu extends Table {
    private final HudStage root;
    private Table mainTable;
    private Button skillBtnOne, skillBtnTwo, skillBtnThree, skillBtnFour;

    public SkillMenu(HudStage root) {
        this.root = root;
        create();
    }

    public Button setSkill(final int skillNumber, final int skillConstant, String iconName) {
        final Button skillBtn = new Button(ResourceManager.toggleButtonStyle);
        root.setIcon(skillBtn, iconName, 48, 1);
        skillBtn.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                root.selectAttackMove(skillConstant);
                clearSkills(skillNumber);
                skillBtn.setChecked(true);
            }
        });
        return skillBtn;
    }

    public void create() {
        this.setName("skillMenu");
        this.setFillParent(true);
        this.mainTable = new Table();
        this.skillBtnOne = setSkill(0, Constants.LUNGE_TYPE, "spear");
        this.skillBtnTwo = setSkill(1, Constants.BASH_TYPE, "shield");
        this.skillBtnThree = setSkill(2, Constants.ROLL_TYPE, "horn");
        this.skillBtnFour = setSkill(3, Constants.LEAP_TYPE, "boot");

        mainTable.add(skillBtnOne).width(74).height(80).expand().fill().center();
        mainTable.row();
        mainTable.add(skillBtnTwo).width(74).height(80).expand().fill().center();
        mainTable.row();
        mainTable.add(skillBtnThree).width(74).height(80).expand().fill().center();
        mainTable.row();
        mainTable.add(skillBtnFour).width(74).height(80).expand().fill().center();

        this.add(mainTable).width(80).height(320).expand().bottom().right().padBottom(70);
    }

    public void clearSkills(int thisBtn) {
        if (thisBtn != 0) {
            skillBtnOne.setChecked(false);
        }
        if (thisBtn != 1) {
            skillBtnTwo.setChecked(false);
        }
        if (thisBtn != 2) {
            skillBtnThree.setChecked(false);
        }
        if (thisBtn != 3) {
            skillBtnFour.setChecked(false);
        }
    }
}
