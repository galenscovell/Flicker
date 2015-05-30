
/**
 * MAINMENU-NEWGAME
 * MainMenuScreen popup table displaying new game options.
 */

package galenscovell.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.utils.Scaling;
import galenscovell.util.ResourceManager;


public class MainMenuNewGame extends Table {
    private MainMenuScreen root;
    private Table mainTable;
    private Button knightButton, explorerButton, mageButton;
    private boolean knightSelected, explorerSelected, mageSelected;


    public MainMenuNewGame(MainMenuScreen root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        this.mainTable = new Table();
        mainTable.pad(4, 4, 4, 4);
        mainTable.setBackground(ResourceManager.frameBG);



        // Class Selection Table
        Table classTable = new Table();
        classTable.setBackground(ResourceManager.hudBG);

        this.knightButton = new Button(ResourceManager.frameCheckedStyle);
        knightButton.padLeft(4);
        Table knightIcon = new Table();
        setIcon(knightIcon, "knight");
        knightButton.add(knightIcon).width(160).height(120).expand().fill().top();
        knightButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (knightSelected) {
                    knightSelected = false;
                } else {
                    knightSelected = true;
                    explorerButton.setChecked(false);
                    explorerSelected = false;
                    mageButton.setChecked(false);
                    mageSelected = false;
                }
            }
        });
        classTable.add(knightButton).width(160).height(300).expand().fill();

        this.explorerButton = new Button(ResourceManager.frameCheckedStyle);
        Table explorerIcon = new Table();
        setIcon(explorerIcon, "explorer");
        explorerButton.add(explorerIcon).width(160).height(120).expand().fill().top();
        explorerButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (explorerSelected) {
                    explorerSelected = false;
                } else {
                    explorerSelected = true;
                    knightButton.setChecked(false);
                    knightSelected = false;
                    mageButton.setChecked(false);
                    mageSelected = false;
                }
            }
        });
        classTable.add(explorerButton).width(160).height(300).expand().fill();

        this.mageButton = new Button(ResourceManager.frameCheckedStyle);
        mageButton.padRight(4);
        Table mageIcon = new Table();
        setIcon(mageIcon, "mage");
        mageButton.add(mageIcon).width(160).height(120).expand().fill().top();
        mageButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (mageSelected) {
                    mageSelected = false;
                } else {
                    mageSelected = true;
                    explorerButton.setChecked(false);
                    explorerSelected = false;
                    knightButton.setChecked(false);
                    knightSelected = false;
                }
            }
        });
        classTable.add(mageButton).width(160).height(300).expand().fill();
        classTable.pack();

        mainTable.add(classTable).width(560).height(320).expand().fill().center();
        mainTable.row();


        // Bottom section
        Table bottomTable = new Table();
        TextButton returnButton = new TextButton("Return", ResourceManager.colorButtonStyle);
        returnButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                returnToMain();
            }
        });
        TextButton startButton = new TextButton("Begin", ResourceManager.colorButtonStyle);
        startButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (knightSelected) {
                    returnToMain();
                    root.startGame("knight");
                } else if (explorerSelected) {
                    returnToMain();
                    root.startGame("explorer");
                } else if (mageSelected) {
                    returnToMain();
                    root.startGame("mage");
                }
            }
        });
        bottomTable.add(returnButton).width(200).height(50).padTop(2).expand().fill();
        bottomTable.add(startButton).width(200).height(50).padTop(2).expand().fill();
        mainTable.add(bottomTable).width(600).height(60).expand().fill();

        this.add(mainTable).width(600).height(400).expand().center();
        this.pack();
    }

    private void returnToMain() {
        knightSelected = false;
        knightButton.setChecked(false);
        explorerSelected = false;
        explorerButton.setChecked(false);
        mageSelected = false;
        mageButton.setChecked(false);
        root.menuOperation(this, 0);
    }

    private void setIcon(Table table, String name) {
        Image icon = new Image(new TextureAtlas.AtlasRegion(ResourceManager.uiAtlas.findRegion(name)));
        icon.setScaling(Scaling.fit);
        table.add(icon).expand().fill().center();
    }
}
