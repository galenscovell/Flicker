
/**
 * OPTIONSMENU CLASS
 * Stage which renders in-game options for saving/quitting.
 */

package galenscovell.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.utils.Align;
import galenscovell.util.ScreenResources;


public class OptionsMenu extends Table {
    private HudDisplay root;
    private Table mainTable;


    public OptionsMenu(HudDisplay root) {
        this.root = root;
        create();
    }

    public void create() {
        this.setFillParent(true);
        this.mainTable = new Table();
        mainTable.setBackground(ScreenResources.frameBG);

        /**********************************
         * TOP TABLE                      *
         **********************************/
        Table topTable = new Table();
        Label titleLabel = new Label("OPTIONS", ScreenResources.menuStyle);
        titleLabel.setAlignment(Align.top, Align.center);
        topTable.add(titleLabel).width(400).expand().fill();


        /**********************************
         * BOTTOM TABLE                   *
         **********************************/
        Table bottomTable = new Table();

        TextButton saveButton = new TextButton("Save", ScreenResources.buttonStyle);
        saveButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        TextButton quitButton = new TextButton("Quit", ScreenResources.buttonStyle);
        quitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                root.returnToMainMenu();
            }
        });

        bottomTable.add(saveButton).width(200).height(60).padBottom(10).expand().fill();
        bottomTable.row();
        bottomTable.add(quitButton).width(200).height(60).expand().fill();


        mainTable.add(topTable).expand().top().center();
        mainTable.row();
        mainTable.add(bottomTable).expand().top().center();

        this.add(mainTable).width(300).height(240).expand().center();
    }
}
