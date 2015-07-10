package galenscovell.screens;

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
    private Table attackPopup, examinePopup, infoPopup, inventoryMenu, optionsMenu;

    public HudStage(GameScreen game,  SpriteBatch spriteBatch) {
        super(new FitViewport(480, 800), spriteBatch);
        this.game = game;
        create();
    }

    public void create() {
        this.attackPopup = new AttackModePopup(this);
        this.examinePopup = new ExamineModePopup(this);
        this.inventoryMenu = new HudInventoryMenu(this);
        this.optionsMenu = new OptionsMenu(this);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        /**********************************
         * TOP TABLE                      *
         **********************************/
        Table topTable = new Table();

        // Player stats section
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
        statsTable.add(barTable).width(260).height(60).expand().fill().top().left();
        topTable.add(statsTable).expand().fill().left();

        // Options section
        Table topRight = new Table();
        Table optionsButton = new Table();
        optionsButton.setTouchable(Touchable.enabled);
        setIcon(optionsButton, "hexagonal-nut", 32);
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(optionsMenu);
            }
        });
        topRight.add(optionsButton).width(48).height(48).expand().fill().top().right();
        topTable.add(topRight).expand().fill().top().right();

        mainTable.add(topTable).expand().fill().top();
        mainTable.row();

        /**********************************
         * BOTTOM TABLE                   *
         **********************************/
        Table bottomTable = new Table();

        // Bottom section
        Table actionTable = new Table();
        Table actionButtons = new Table();
        Button inventoryButton = new Button(ResourceManager.buttonStyle);
        setIcon(inventoryButton, "processor", 64);
        inventoryButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(inventoryMenu);
            }
        });
        Button examineButton = new Button(ResourceManager.buttonStyle);
        setIcon(examineButton, "radar-sweep", 64);
        examineButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                modeChange(1);
            }
        });
        Button attackButton = new Button(ResourceManager.buttonStyle);
        setIcon(attackButton, "target", 64);
        attackButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                modeChange(0);
            }
        });
        actionButtons.add(inventoryButton).height(68).width(100);
        actionButtons.add(examineButton).height(68).width(100).padLeft(4).padRight(4);
        actionButtons.add(attackButton).height(68).width(100);
        actionTable.add(actionButtons).width(312).center();
        bottomTable.add(actionTable).expand().center();

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
        if (optionsMenu.hasParent()) {
            optionsMenu.remove();
        }
        if (inventoryMenu.hasParent()) {
            inventoryMenu.remove();
        }
        if (mode == 1) {
            game.toggleMode(mode);
            if (examinePopup.hasParent()) {
                examinePopup.remove();
            } else {
                this.addActor(examinePopup);
                removeExamineInfo();
            }
            if (attackPopup.hasParent()) {
                attackPopup.remove();
            }
        } else {
            game.toggleMode(mode);
            if (attackPopup.hasParent()) {
                attackPopup.remove();
            } else {
                this.addActor(attackPopup);
                removeExamineInfo();
            }
            if (examinePopup.hasParent()) {
                examinePopup.remove();
            }
        }
    }

    public void displayExamineInfo(String info, Sprite sprite) {
        Sprite flippedTarget = new Sprite(sprite);
        flippedTarget.flip(false, true);
        this.infoPopup = new ExamineInfoPopup(this, info, flippedTarget);
        this.addActor(infoPopup);
    }

    public boolean clearMenus() {
        if (optionsMenu.hasParent()) {
            optionsMenu.remove();
            return false;
        } else if (inventoryMenu.hasParent()) {
            inventoryMenu.remove();
            return false;
        } else if (infoPopup != null && infoPopup.hasParent()) {
            infoPopup.remove();
            infoPopup = null;
            return false;
        } else {
            return true;
        }
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

    private void removeExamineInfo() {
        if (infoPopup != null && infoPopup.hasParent()) {
            infoPopup.remove();
            infoPopup = null;
        }
    }

    private void menuOperation(Table menu) {
        removeExamineInfo();
        if (menu.hasParent()) {
            menu.remove();
        } else {
            if (optionsMenu != menu && optionsMenu.hasParent()) {
                optionsMenu.remove();
            } else if (inventoryMenu != menu && inventoryMenu.hasParent()) {
                inventoryMenu.remove();
            }
            this.addActor(menu);
        }
    }
}
