package galenscovell.ui.components;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.util.ResourceManager;

public class InteractPopup extends Table {
    private final Stage root;
    private Table mainTable;

    public InteractPopup(Stage root, Inanimate inanimate) {
        this.root = root;
        this.create(inanimate);
    }

    public void create(Inanimate inanimate) {
        this.setFillParent(true);
        this.mainTable = new Table();
        this.mainTable.setBackground(ResourceManager.frameUp);
        Image inanimateSprite = new Image(inanimate.getSprite());
        this.mainTable.add(inanimateSprite).expand().fillY();

        this.add(this.mainTable).width(74).height(80).expand().left();
    }
}
