package galenscovell.screens;

import galenscovell.entities.Player;
import galenscovell.screens.components.*;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;
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
    private int eventLines = 1;
    private Label eventLog;
    private ProgressBar chassis, power, matter;
    private Table attackPopup, examinePopup, inventoryMenu, optionsMenu;
    private Button attackButton, examineButton, inventoryButton, optionsButton;

    public HudStage(GameScreen game, Player player, SpriteBatch spriteBatch) {
        super(new FitViewport(800, 480), spriteBatch);
        this.game = game;
        create(player);
    }

    public void create(Player player) {
        this.attackPopup = new AttackPopup(this);
        this.examinePopup = new ExaminePopup(this);
        this.inventoryMenu = new HudInventoryMenu(this);
        this.optionsMenu = new HudOptionsMenu(this);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        /**********************************
         * TOP TABLE                      *
         **********************************/
        Table topTable = new Table();

        Table topRight = new Table();
        topRight.pad(8, 0, 0, 8);
        this.eventLog = new Label("Events displayed here.", ResourceManager.detailStyle);
        eventLog.setAlignment(Align.topRight, Align.topRight);
        eventLog.setWrap(true);
        topRight.add(eventLog).expand().fill().top().right();
        topTable.add(topRight).height(180).width(400).expand().fill().top().right();

        mainTable.add(topTable).expand().fill().top();
        mainTable.row();

        /**********************************
         * MIDDLE TABLE                   *
         **********************************/
        Table middleTable = new Table();

        Table left = new Table();
        this.attackButton = new Button(ResourceManager.buttonStyle);
        setIcon(attackButton, "sensor");
        attackButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                modeChange(attackButton);
                game.toggleMode(0);
            }
        });
        this.examineButton = new Button(ResourceManager.buttonStyle);
        setIcon(examineButton, "sensor");
        examineButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                modeChange(examineButton);
                game.toggleMode(1);
            }
        });
        this.inventoryButton = new Button(ResourceManager.buttonStyle);
        setIcon(inventoryButton, "microchip");
        inventoryButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(inventoryMenu);
            }
        });
        this.optionsButton = new Button(ResourceManager.buttonStyle);
        setIcon(optionsButton, "network");
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(optionsMenu);
            }
        });
        left.add(optionsButton).height(80).width(80).padBottom(4);
        left.row();
        left.add(inventoryButton).height(80).width(80).padBottom(4);
        left.row();
        left.add(examineButton).height(80).width(80).padBottom(4);
        left.row();
        left.add(attackButton).height(80).width(80).padBottom(8);
        middleTable.add(left).expand().left();

        mainTable.add(middleTable).fill();
        mainTable.row();

        /**********************************
         * BOTTOM TABLE                   *
         **********************************/
        Table bottomTable = new Table();

        // Bottom left
        Table bottomLeft = new Table();
        Table barTable = new Table();
        this.chassis = createBar("chassisFill");
        this.power = createBar("powerFill");
        this.matter = createBar("matterFill");
        barTable.add(chassis).width(260).height(20);
        barTable.row();
        barTable.add(power).width(260).height(20);
        barTable.row();
        barTable.add(matter).width(260).height(20);
        bottomLeft.add(barTable).width(260).height(60).expand().fill().bottom().left();
        bottomTable.add(bottomLeft).expand().fill().left();

        mainTable.add(bottomTable).bottom().fill();

        mainTable.pack();
        this.addActor(mainTable);
    }

    public void dispose() {
        this.addActor(attackPopup);
        this.addActor(examinePopup);
        this.addActor(inventoryMenu);
        this.addActor(optionsMenu);
    }

    public void returnToMainMenu() {
        optionsMenu.remove();
        game.toMainMenu();
    }

    public void addToLog(String text) {
        if (eventLines == 5) {
            eventLog.setText(text);
            eventLines = 1;
        } else {
            StringBuilder currentText = eventLog.getText();
            String newText = currentText + "\n" + text;
            eventLog.setText(newText);
            eventLines++;
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

    private void setIcon(Table table, String name) {
        Image icon = new Image(new TextureAtlas.AtlasRegion(ResourceManager.uiAtlas.findRegion(name)));
        icon.setScaling(Scaling.fit);
        table.add(icon).expand().fill().center();
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

    private void modeChange(Button button) {
        if (button == examineButton) {
            if (examinePopup.hasParent()) {
                examinePopup.remove();
                inventoryButton.setTouchable(Touchable.enabled);
                optionsButton.setTouchable(Touchable.enabled);
            } else {
                this.addActor(examinePopup);
                inventoryButton.setTouchable(Touchable.disabled);
                optionsButton.setTouchable(Touchable.disabled);
            }
            if (attackPopup.hasParent()) {
                attackPopup.remove();
            }
        } else {
            if (attackPopup.hasParent()) {
                attackPopup.remove();
                inventoryButton.setTouchable(Touchable.enabled);
                optionsButton.setTouchable(Touchable.enabled);
            } else {
                this.addActor(attackPopup);
                inventoryButton.setTouchable(Touchable.disabled);
                optionsButton.setTouchable(Touchable.disabled);
            }
            if (examinePopup.hasParent()) {
                examinePopup.remove();
            }
        }
    }

    private void menuOperation(Table menu) {
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
