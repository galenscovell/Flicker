package galenscovell.processing.states;

import com.badlogic.gdx.scenes.scene2d.*;
import galenscovell.processing.Repository;
import galenscovell.things.entities.*;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.ui.components.ExamineInfo;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

public class ExamineState implements State {
    private final Stage stage;
    private final Hero hero;
    private final Repository repo;

    public ExamineState(Stage stage, Hero hero, Repository repo) {
        this.stage = stage;
        this.hero = hero;
        this.repo = repo;
    }

    @Override
    public StateType getStateType() {
        return StateType.EXAMINE;
    }

    @Override
    public void enter() {
        // System.out.println("\tEntering EXAMINE state");
    }

    @Override
    public void exit() {
        clearExamineBox();
        // System.out.println("\tLeaving EXAMINE state\n");
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void handleInput(float x, float y) {
        clearExamineBox();
        int convertX = (int) (x / Constants.TILESIZE);
        int convertY = (int) (y / Constants.TILESIZE);
        ExamineInfo infoBox = null;
        Entity entity = repo.findEntity(convertX, convertY);
        if (entity == null) {
            Inanimate inanimate = repo.findInanimate(convertX, convertY);
            if (inanimate == null) {
                Tile tile = repo.findTile(convertX, convertY);
                if (tile != null) {
                    infoBox = new ExamineInfo(tile.examine(), tile.getSprite());
                }
            } else {
                infoBox = new ExamineInfo(inanimate.examine(), inanimate.getSprite());
            }
        } else {
            infoBox = new ExamineInfo(entity.examine(), entity.getSprite());
        }
        if (infoBox != null) {
            stage.addActor(infoBox);
        }
    }

    @Override
    public void handleInterfaceEvent(int moveType) {

    }

    private void clearExamineBox() {
        Actor box = stage.getRoot().findActor("examineInfo");
        if (box != null) {
            box.remove();
        }
    }
}
