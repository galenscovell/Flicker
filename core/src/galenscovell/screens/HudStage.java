package galenscovell.screens;

import galenscovell.entities.Player;
import galenscovell.screens.components.*;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * HUD STAGE
 * Creates HUD and handles HUD updates.
 * HUD camera uses exact pixel dimensions (800, 480).
 *
 * @author Galen Scovell
 */

public class HudStage extends Stage {
    private GameScreen game;
    private ProgressBar chassis, power, matter;
    private Table attackPopup, examinePopup, infoPopup, inventoryMenu, optionsMenu, optionsButton;
    private Button attackButton, examineButton, inventoryButton;

    public HudStage(GameScreen game, Player player, SpriteBatch spriteBatch) {
        super(new FitViewport(800, 480), spriteBatch);
        this.game = game;
        create(player);
    }

    public void create(Player player) {
        this.attackPopup = new AttackModePopup(this);
        this.examinePopup = new ExamineModePopup(this);
        this.inventoryMenu = new HudInventoryMenu(this);
        this.optionsMenu = new HudOptionsMenu(this);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        /**********************************
         * TOP TABLE                      *
         **********************************/
        Table topTable = new Table();

        // Options section
        Table topLeft = new Table();
        this.optionsButton = new Table();
        optionsButton.setTouchable(Touchable.enabled);
        setIcon(optionsButton, "hexagonal-nut", 32);
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(optionsMenu);
            }
        });
        topLeft.add(optionsButton).width(48).height(48).expand().fill().top().left();
        topTable.add(topLeft).expand().fill().top().left();

        mainTable.add(topTable).expand().fill().top();
        mainTable.row();

        /**********************************
         * BOTTOM TABLE                   *
         **********************************/
        Table bottomTable = new Table();

        // Bottom left section
        Table statsTable = new Table();
        Table barTable = new Table();
        this.chassis = createBar("chassisFill");
        this.power = createBar("powerFill");
        this.matter = createBar("matterFill");
        barTable.add(chassis).width(260).height(20);
        barTable.row();
        barTable.add(power).width(260).height(20);
        barTable.row();
        barTable.add(matter).width(260).height(20);
        statsTable.add(barTable).width(260).height(60).expand().fill().bottom().left();
        bottomTable.add(statsTable).expand().fill().left();

        // Bottom right section
        Table actionTable = new Table();
        Table actionButtons = new Table();
        this.inventoryButton = new Button(ResourceManager.buttonStyle);
        setIcon(inventoryButton, "processor", 64);
        inventoryButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(inventoryMenu);
            }
        });
        this.examineButton = new Button(ResourceManager.buttonStyle);
        setIcon(examineButton, "radar-sweep", 64);
        examineButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                modeChange(1);
            }
        });
        this.attackButton = new Button(ResourceManager.buttonStyle);
        setIcon(attackButton, "target", 64);
        attackButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                modeChange(0);
            }
        });
        actionButtons.add(inventoryButton).height(68).width(100);
        actionButtons.add(examineButton).height(68).width(100).padLeft(4).padRight(4);
        actionButtons.add(attackButton).height(68).width(100);
        actionTable.add(actionButtons).width(312).right();
        bottomTable.add(actionTable).expand().right();

        mainTable.add(bottomTable).fill();

        mainTable.pack();
        this.addActor(mainTable);
    }

    public void dispose() {
        removeExamineInfo();
        this.addActor(attackPopup);
        this.addActor(examinePopup);
        this.addActor(inventoryMenu);
        this.addActor(optionsMenu);
    }

    public void returnToMainMenu() {
        optionsMenu.remove();
        game.toMainMenu();
    }

    public void modeChange(int mode) {
        if (mode == 1) {
            game.toggleMode(mode);
            if (examinePopup.hasParent()) {
                examinePopup.remove();
                inventoryButton.setTouchable(Touchable.enabled);
                optionsButton.setTouchable(Touchable.enabled);
            } else {
                this.addActor(examinePopup);
                removeExamineInfo();
                inventoryButton.setTouchable(Touchable.disabled);
                optionsButton.setTouchable(Touchable.disabled);
            }
            if (attackPopup.hasParent()) {
                attackPopup.remove();
            }
        } else {
            game.toggleMode(mode);
            if (attackPopup.hasParent()) {
                attackPopup.remove();
                inventoryButton.setTouchable(Touchable.enabled);
                optionsButton.setTouchable(Touchable.enabled);
            } else {
                this.addActor(attackPopup);
                removeExamineInfo();
                inventoryButton.setTouchable(Touchable.disabled);
                optionsButton.setTouchable(Touchable.disabled);
            }
            if (examinePopup.hasParent()) {
                examinePopup.remove();
            }
        }
    }

    public void removeExamineInfo() {
        if (infoPopup != null && infoPopup.hasParent()) {
            infoPopup.remove();
            infoPopup = null;
        }
    }

    public void displayExamineInfo(String info, Sprite sprite) {
        Sprite flippedTarget = new Sprite(sprite);
        flippedTarget.flip(false, true);
        this.infoPopup = new ExamineInfoPopup(this, info, flippedTarget);
        this.addActor(infoPopup);
    }

    public void updateChassis(int val) {
        chassis.setValue(chassis.getValue() + val);
    }

    public void updatePower(int val) {
        power.setValue(power.getValue() + val);
    }

    public void updateMatter(int val) {
        matter.setValue(matter.getValue() + val);
    }

    private void setIcon(Table table, String name, int height) {
        Image icon = new Image(new TextureAtlas.AtlasRegion(ResourceManager.uiAtlas.findRegion(name)));
        icon.setScaling(Scaling.fillX);
        table.add(icon).height(height).expand().center();
    }

    private ProgressBar createBar(String path) {
        TextureRegionDrawable fill = new TextureRegionDrawable(ResourceManager.uiAtlas.findRegion(path));
        TextureRegionDrawable empty = new TextureRegionDrawable(ResourceManager.uiAtlas.findRegion("barEmpty"));
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(empty, fill);
        ProgressBar bar = new ProgressBar(0, 260, 1, false, barStyle);
        barStyle.knobBefore = fill;
        bar.setValue(260);
        bar.setAnimateDuration(0.1f);
        return bar;
    }

    private void menuOperation(Table menu) {
        removeExamineInfo();
        if (menu.hasParent()) {
            menu.remove();
            game.enableWorldInput();
        } else {
            if (optionsMenu != menu && optionsMenu.hasParent()) {
                optionsMenu.remove();
            } else if (inventoryMenu != menu && inventoryMenu.hasParent()) {
                inventoryMenu.remove();
            }
            this.addActor(menu);
            game.disableWorldInput();
        }
    }
}
