
/**
 * INVENTORYMENU CLASS
 * HUD table for player inventory.
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.utils.Align;
import galenscovell.util.ScreenResources;


public class InventoryMenu extends Table {
    private HudDisplay root;
    private Table mainTable;


    public InventoryMenu(HudDisplay root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        this.mainTable = new Table();
        mainTable.setBackground(ScreenResources.frameBG);


        this.add(mainTable).width(300).height(300).expand().left();
    }
}

