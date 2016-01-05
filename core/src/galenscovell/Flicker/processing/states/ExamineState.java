package galenscovell.flicker.processing.states;

import com.badlogic.gdx.scenes.scene2d.Actor;
import galenscovell.flicker.processing.Repository;
import galenscovell.flicker.things.entities.*;
import galenscovell.flicker.things.inanimates.Inanimate;
import galenscovell.flicker.ui.components.ExamineInfo;
import galenscovell.flicker.ui.screens.GameScreen;
import galenscovell.flicker.util.Constants;
import galenscovell.flicker.world.Tile;

public class ExamineState implements State {
    private final GameScreen gameScreen;
    private final Hero hero;
    private final Repository repo;

    public ExamineState(GameScreen gameScreen, Hero hero, Repository repo) {
        this.gameScreen = gameScreen;
        this.hero = hero;
        this.repo = repo;
    }

    @Override
    public StateType getStateType() {
        return StateType.EXAMINE;
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {
        clearExamineBox();
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
            gameScreen.getStage().addActor(infoBox);
        }
    }

    @Override
    public void handleInterfaceEvent(int moveType) {

    }

    private void clearExamineBox() {
        Actor box = gameScreen.getStage().getRoot().findActor("examineInfo");
        if (box != null) {
            box.remove();
        }
    }
}
