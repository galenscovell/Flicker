package galenscovell.screens.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import galenscovell.screens.HudStage;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.scenes.scene2d.ui.Table;


/**
 * MOVE PANEL
 * Displays current available moves to player.
 *
 * @author Galen Scovell
 */

public class MovePanel extends Table {
    private HudStage root;
    private Table mainTable;

    public MovePanel(HudStage root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        this.mainTable = new Table();
        Button lungeButton = new Button(ResourceManager.frameStyle);
        lungeButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                root.selectAttackMove("lunge");
            }
        });
        Button rollButton = new Button(ResourceManager.frameStyle);
        rollButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                root.selectAttackMove("roll");
            }
        });
        Button bashButton = new Button(ResourceManager.frameStyle);
        bashButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                root.selectAttackMove("bash");
            }
        });
        Button leapButton = new Button(ResourceManager.frameStyle);
        leapButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                root.selectAttackMove("leap");
            }
        });
        mainTable.add(lungeButton).width(60).height(70).expand().fill().center();
        mainTable.row();
        mainTable.add(rollButton).width(60).height(70).expand().fill().center();
        mainTable.row();
        mainTable.add(bashButton).width(60).height(70).expand().fill().center();
        mainTable.row();
        mainTable.add(leapButton).width(60).height(70).expand().fill().center();

        this.add(mainTable).width(80).height(300).expand().bottom().right().padBottom(70);
    }
}
