package galenscovell.ui.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import galenscovell.ui.HudStage;
import galenscovell.util.*;

public class SkillMenu extends Table {
    private final HudStage root;
    private Table mainTable;

    public SkillMenu(HudStage root) {
        this.root = root;
        this.create();
    }

    public void create() {
        this.setName("skillMenu");
        this.setFillParent(true);
        this.mainTable = new Table();
        Button lungeButton = new Button(ResourceManager.frameStyle);
        this.root.setIcon(lungeButton, "spear", 48, 1);
        lungeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SkillMenu.this.root.selectAttackMove(Constants.LUNGE_TYPE);
            }
        });
        Button rollButton = new Button(ResourceManager.frameStyle);
        this.root.setIcon(rollButton, "horn", 48, 1);
        rollButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SkillMenu.this.root.selectAttackMove(Constants.ROLL_TYPE);
            }
        });
        Button bashButton = new Button(ResourceManager.frameStyle);
        this.root.setIcon(bashButton, "shield", 48, 1);
        bashButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SkillMenu.this.root.selectAttackMove(Constants.BASH_TYPE);
            }
        });
        Button leapButton = new Button(ResourceManager.frameStyle);
        this.root.setIcon(leapButton, "boot", 48, 1);
        leapButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SkillMenu.this.root.selectAttackMove(Constants.LEAP_TYPE);
            }
        });
        this.mainTable.add(lungeButton).width(74).height(80).expand().fill().center();
        this.mainTable.row();
        this.mainTable.add(rollButton).width(74).height(80).expand().fill().center();
        this.mainTable.row();
        this.mainTable.add(bashButton).width(74).height(80).expand().fill().center();
        this.mainTable.row();
        this.mainTable.add(leapButton).width(74).height(80).expand().fill().center();

        this.add(this.mainTable).width(80).height(320).expand().bottom().right().padBottom(70);
    }
}
