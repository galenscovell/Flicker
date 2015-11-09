package galenscovell.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import galenscovell.processing.states.StateType;
import galenscovell.ui.components.*;
import galenscovell.ui.screens.GameScreen;
import galenscovell.util.ResourceManager;

public class HudStage extends Stage {
    private final GameScreen game;
    private ProgressBar health;
    public Table examinePopup, inventoryMenu, optionsMenu, skillMenu;

    public HudStage(GameScreen game, SpriteBatch spriteBatch) {
        super(new FitViewport(480, 800), spriteBatch);
        this.game = game;
        this.create();
    }

    public void create() {
        this.examinePopup = new ExaminePopup(this);
        this.inventoryMenu = new InventoryMenu(this);
        this.optionsMenu = new OptionMenu(this);
        this.skillMenu = new SkillMenu(this);

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
        this.setIcon(optionsButton, "scroll", 32, 0.5f);
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (HudStage.this.optionsMenu.hasParent()) {
                    HudStage.this.game.changeState(StateType.ACTION);
                } else {
                    HudStage.this.game.changeState(StateType.MENU);
                }
                HudStage.this.menuOperation(HudStage.this.optionsMenu);
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
        this.setIcon(inventoryButton, "inventory", 48, 1);
        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (HudStage.this.inventoryMenu.hasParent()) {
                    HudStage.this.game.changeState(StateType.ACTION);
                } else {
                    HudStage.this.game.changeState(StateType.MENU);
                }
                HudStage.this.menuOperation(HudStage.this.inventoryMenu);
            }
        });
        Button examineButton = new Button(ResourceManager.panelStyle);
        this.setIcon(examineButton, "examine", 48, 1);
        examineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (HudStage.this.examinePopup.hasParent()) {
                    HudStage.this.game.changeState(StateType.ACTION);
                } else {
                    HudStage.this.game.changeState(StateType.MENU);
                }
                HudStage.this.menuOperation(HudStage.this.examinePopup);
            }
        });
        bottomLeft.add(inventoryButton).width(80).height(64).expand().left().padRight(4);
        bottomLeft.add(examineButton).width(80).height(64).expand().left();
        bottomTable.add(bottomLeft).expand().bottom().left();

        // Bottom right
        Table bottomRight = new Table();
        Button attackButton = new Button(ResourceManager.panelStyle);
        this.setIcon(attackButton, "tome", 48, 1);
        attackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (HudStage.this.skillMenu.hasParent()) {
                    HudStage.this.game.changeState(StateType.ACTION);
                } else {
                    HudStage.this.game.changeState(StateType.MENU);
                }
                HudStage.this.menuOperation(HudStage.this.skillMenu);
            }
        });
        bottomRight.add(attackButton).width(80).height(64).expand().right();
        bottomTable.add(bottomRight).expand().bottom().right();

        mainTable.add(bottomTable).fill().bottom();

        mainTable.pack();
        this.addActor(mainTable);
    }

    @Override
    public void dispose() {
        this.addActor(this.examinePopup);
        this.addActor(this.inventoryMenu);
        this.addActor(this.optionsMenu);
        this.addActor(this.skillMenu);
    }

    public void returnToMainMenu() {
        this.optionsMenu.remove();
        this.game.toMainMenu();
    }

    public void selectAttackMove(int moveType) {
        this.game.passInterfaceEventToState(moveType);
    }

    public void updateHealth(int val) {
        this.health.setValue(this.health.getValue() + val);
    }

    public void setIcon(Table table, String name, int height, float opacity) {
        Image icon = new Image(new AtlasRegion(ResourceManager.uiAtlas.findRegion(name)));
        icon.setScaling(Scaling.fillY);
        icon.setColor(1, 1, 1, opacity);
        table.add(icon).height(height).expand().fill().center();
    }

    private ProgressBar createBar(String path) {
        TextureRegionDrawable fill = new TextureRegionDrawable(ResourceManager.uiAtlas.findRegion(path));
        TextureRegionDrawable empty = new TextureRegionDrawable(ResourceManager.uiAtlas.findRegion("barEmpty"));
        ProgressBarStyle barStyle = new ProgressBarStyle(empty, fill);
        ProgressBar bar = new ProgressBar(0, 260, 1, false, barStyle);
        barStyle.knobBefore = fill;
        bar.setValue(260);
        bar.setAnimateDuration(0.1f);
        return bar;
    }

    private void menuOperation(Table menu) {
        if (menu.hasParent()) {
            menu.remove();
        } else {
            if (this.optionsMenu != menu && this.optionsMenu.hasParent()) {
                this.optionsMenu.remove();
            } else if (this.inventoryMenu != menu && this.inventoryMenu.hasParent()) {
                this.inventoryMenu.remove();
            } else if (this.skillMenu != menu && this.skillMenu.hasParent()) {
                this.skillMenu.remove();
            }
            this.addActor(menu);
        }
    }
}
