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
 * HUD camera uses exact pixel dimensions (480, 800).
 *
 * @author Galen Scovell
 */

public class HudStage extends Stage {
    private GameScreen game;
    private ProgressBar health;
    private Table examinePopup, infoPopup, inventoryMenu, optionsMenu, movePanel;

    public HudStage(GameScreen game,  SpriteBatch spriteBatch) {
        super(new FitViewport(480, 800), spriteBatch);
        this.game = game;
        create();
    }

    public void create() {
        this.examinePopup = new ExamineModePopup(this);
        this.inventoryMenu = new HudInventoryMenu(this);
        this.optionsMenu = new OptionsMenu(this);
        this.movePanel = new MovePanel(this);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        /**********************************
         * TOP TABLE                      *
         **********************************/
        Table topTable = new Table();

        // Player stats section
        Table statsTable = new Table();
        Table barTable = new Table();
        barTable.setBackground(new TextureRegionDrawable(ResourceManager.uiAtlas.findRegion("bar_brown_empty")));

        statsTable.add(barTable).width(136).height(41).expand().fill().top().left();
        topTable.add(statsTable).expand().fill().left();

        // Options section
        Table topRight = new Table();
        Table optionsButton = new Table();
        optionsButton.setTouchable(Touchable.enabled);
        setIcon(optionsButton, "scroll", 32, 0.5f);
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

        // Bottom left
        Table bottomLeft = new Table();
        Button inventoryButton = new Button(ResourceManager.panelStyle);
        setIcon(inventoryButton, "inventory", 48, 1);
        inventoryButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(inventoryMenu);
            }
        });
        Button examineButton = new Button(ResourceManager.panelStyle);
        setIcon(examineButton, "examine", 48, 1);
        examineButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        bottomLeft.add(inventoryButton).width(80).height(64).expand().left().padRight(4);
        bottomLeft.add(examineButton).width(80).height(64).expand().left();
        bottomTable.add(bottomLeft).expand().bottom().left();

        // Bottom right
        Table bottomRight = new Table();
        Button attackButton = new Button(ResourceManager.panelStyle);
        setIcon(attackButton, "tome", 48, 1);
        attackButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(movePanel);
            }
        });
        bottomRight.add(attackButton).width(80).height(64).expand().right();
        bottomTable.add(bottomRight).expand().bottom().right();

        mainTable.add(bottomTable).fill().bottom();

        mainTable.pack();
        this.addActor(mainTable);
    }

    public void dispose() {
        removeExamineInfo();
        this.addActor(examinePopup);
        this.addActor(inventoryMenu);
        this.addActor(optionsMenu);
    }

    public void returnToMainMenu() {
        optionsMenu.remove();
        game.toMainMenu();
    }

    public void selectAttackMove(String move) {
        game.setAttackMode(move);
    }

    public void displayExamineInfo(String info, Sprite sprite) {
        Sprite flippedTarget = new Sprite(sprite);
        flippedTarget.flip(false, true);
        this.infoPopup = new ExamineInfoPopup(this, info, flippedTarget);
        this.addActor(infoPopup);
    }

    public boolean restrictMovement() {
        return optionsMenu.hasParent() || inventoryMenu.hasParent() || movePanel.hasParent();
    }

    public boolean clearMenus() {
        if (optionsMenu.hasParent()) {
            optionsMenu.remove();
            return false;
        } else if (inventoryMenu.hasParent()) {
            inventoryMenu.remove();
            return false;
        } else if (movePanel.hasParent()) {
            movePanel.remove();
            return false;
        } else if (infoPopup != null && infoPopup.hasParent()) {
            infoPopup.remove();
            infoPopup = null;
            return false;
        } else {
            return true;
        }
    }

    public void updateHealth(int val) {
        health.setValue(health.getValue() + val);
    }

    public void setIcon(Table table, String name, int height, float opacity) {
        Image icon = new Image(new TextureAtlas.AtlasRegion(ResourceManager.uiAtlas.findRegion(name)));
        icon.setScaling(Scaling.fillY);
        icon.setColor(1, 1, 1, opacity);
        table.add(icon).height(height).expand().fill().center();
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
            } else if (movePanel != menu && movePanel.hasParent()) {
                movePanel.remove();
            }
            this.addActor(menu);
        }
    }
}
