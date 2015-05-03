
/**
 * HUDDISPLAY CLASS
 *
 */

package galenscovell.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;


public class HudDisplay {
    private Stage stage;
    private Table table;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));


    public HudDisplay() {
        this.stage = new Stage();

        this.table = new Table();
        Label testLabel = new Label("Testing", skin);
        table.add(testLabel);
        stage.addActor(table);
    }

    public void render() {
        stage.draw();
    }
}
