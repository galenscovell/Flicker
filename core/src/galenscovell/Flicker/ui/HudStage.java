package galenscovell.flicker.ui;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import galenscovell.flicker.processing.states.StateType;
import galenscovell.flicker.ui.components.*;
import galenscovell.flicker.ui.screens.GameScreen;
import galenscovell.flicker.util.*;

public class HudStage extends Stage {
    private final GameScreen gameScreen;
    private final InteractionVerticalGroup interactionVerticalGroup;
    private ProgressBar health;
    private Table examineNotice, inventoryMenu, optionsMenu;
    private SkillMenu skillMenu;
    private Button inventoryButton, examineButton, attackButton;

    public HudStage(GameScreen gameScreen,  SpriteBatch spriteBatch) {
        super(new FitViewport(Constants.UI_X, Constants.UI_Y), spriteBatch);
        this.gameScreen = gameScreen;
        this.interactionVerticalGroup = new InteractionVerticalGroup();
        this.addActor(interactionVerticalGroup);
        create();
    }

    public void create() {
        this.examineNotice = new ExamineNotice(this);
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
        setIcon(optionsButton, "scroll", 42, 0.5f);
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (optionsMenu.hasParent()) {
                    toggleButtons(false);
                    clearMenus();
                    gameScreen.changeState(StateType.ACTION);
                } else {
                    clearMenus();
                    toggleButtons(true);
                    addToStage(optionsMenu);
                    gameScreen.changeState(StateType.MENU);
                }
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
        this.inventoryButton = new Button(ResourceManager.panelButtonStyle);
        setIcon(inventoryButton, "inventory", 48, 1);
        inventoryButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (inventoryMenu.hasParent()) {
                    clearMenus();
                    gameScreen.changeState(StateType.ACTION);
                } else {
                    clearMenus();
                    addToStage(inventoryMenu);
                    gameScreen.changeState(StateType.MENU);
                }
            }
        });
        this.examineButton = new Button(ResourceManager.panelButtonStyle);
        setIcon(examineButton, "examine", 48, 1);
        examineButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (examineNotice.hasParent()) {
                    clearMenus();
                    gameScreen.changeState(StateType.ACTION);
                } else {
                    clearMenus();
                    addToStage(examineNotice);
                    gameScreen.changeState(StateType.EXAMINE);
                }
            }
        });
        bottomLeft.add(inventoryButton).width(80).height(64).expand().left().padRight(4);
        bottomLeft.add(examineButton).width(80).height(64).expand().left();
        bottomTable.add(bottomLeft).expand().bottom().left();

        // Bottom right
        Table bottomRight = new Table();
        this.attackButton = new Button(ResourceManager.panelButtonStyle);
        setIcon(attackButton, "tome", 48, 1);
        attackButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (skillMenu.hasParent()) {
                    clearMenus();
                    gameScreen.changeState(StateType.ACTION);
                } else {
                    clearMenus();
                    addToStage(skillMenu);
                    gameScreen.changeState(StateType.MENU);
                }
            }
        });
        bottomRight.add(attackButton).width(80).height(64).expand().right();
        bottomTable.add(bottomRight).expand().bottom().right();

        mainTable.add(bottomTable).fill().bottom();

        mainTable.pack();
        this.addActor(mainTable);
    }

    public void dispose() {
        this.addActor(examineNotice);
        this.addActor(inventoryMenu);
        this.addActor(optionsMenu);
        this.addActor(skillMenu);
    }

    public void returnToMainMenu() {
        optionsMenu.remove();
        gameScreen.toMainMenu();
    }

    public void selectAttackMove(int moveType) {
        gameScreen.passInterfaceEventToState(moveType);
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

    private void addToStage(Table menu) {
        this.addActor(menu);
    }

    private void toggleButtons(boolean on) {
        if (on) {
            inventoryButton.setTouchable(Touchable.disabled);
            examineButton.setTouchable(Touchable.disabled);
            attackButton.setTouchable(Touchable.disabled);
        } else {
            inventoryButton.setTouchable(Touchable.enabled);
            examineButton.setTouchable(Touchable.enabled);
            attackButton.setTouchable(Touchable.enabled);
        }
        interactionVerticalGroup.toggle();
    }

    private void clearMenus() {
        if (optionsMenu.hasParent()) {
            optionsMenu.remove();
        }
        if (inventoryMenu.hasParent()) {
            inventoryMenu.remove();
        }
        if (skillMenu.hasParent()) {
            skillMenu.remove();
            skillMenu.clearSkills(-1);
        }
        if (examineNotice.hasParent()) {
            examineNotice.remove();
        }
    }
}
