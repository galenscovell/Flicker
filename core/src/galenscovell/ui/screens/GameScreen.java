package galenscovell.ui.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import galenscovell.flicker.FlickerMain;
import galenscovell.graphics.Lighting;
import galenscovell.processing.*;
import galenscovell.processing.controls.*;
import galenscovell.processing.states.*;
import galenscovell.things.entities.Hero;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.ui.HudStage;
import galenscovell.ui.components.*;
import galenscovell.world.*;

public class GameScreen extends AbstractScreen {
    private final int timestep = 20;
    private int accumulator;
    private State state, actionState, menuState, examineState;
    private Renderer renderer;
    private InputMultiplexer input;
    private InteractionVerticalGroup interactionVerticalGroup;

    public GameScreen(FlickerMain root) {
        super(root);
        create();
        this.interactionVerticalGroup = new InteractionVerticalGroup();
        stage.addActor(interactionVerticalGroup);
    }

    @Override
    public void create() {
        this.stage = new HudStage(this, this.root.spriteBatch);
        createNewLevel();
    }

    @Override
    public void render(float delta) {
        // Update
        if (accumulator > this.timestep) {
            accumulator = 0;
            state.update(delta);
        }
        stage.act(delta);
        accumulator++;
        // Render
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render((double) accumulator / timestep);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void dispose() {
        stage.dispose();
        renderer.dispose();
    }

    public StateType getState() {
        return state.getStateType();
    }

    public void changeState(StateType stateType) {
        state.exit();
        if (stateType == StateType.ACTION) {
            state = actionState;
        } else if (stateType == StateType.MENU) {
            state = menuState;
        } else {
            state = examineState;
        }
        state.enter();
    }

    public void passInputToState(float x, float y) {
        state.handleInput(x, y);
    }

    public void passInterfaceEventToState(int moveType) {
        state.handleInterfaceEvent(moveType);
    }

    public void toggleInteractionBoxes() {
        interactionVerticalGroup.toggle();
    }

    public void displaySkillInfo(String[] info) {
        SkillInfo infoBox = new SkillInfo(info);
        stage.addActor(infoBox);
    }

    public void clearSkillInfo() {
        Actor skillInfo = stage.getRoot().findActor("skillInfo");
        if (skillInfo != null) {
            skillInfo.remove();
        }
    }

    public void displayInanimateBox(Inanimate inanimate, Tile tile) {
        interactionVerticalGroup.addActor(new InteractButton(this, inanimate, tile));
    }

    public void clearInanimateBoxes() {
        interactionVerticalGroup.clear();
    }

    public void clearStageSkillMenu() {
        Actor skillMenu = stage.getRoot().findActor("skillMenu");
        if (skillMenu != null) {
            skillMenu.remove();
        }
    }

    public void screenZoom(float zoom) {
        renderer.zoom(zoom);
    }

    public void screenPan(float dx, float dy) {
        renderer.pan(dx, dy);
    }

    public void toMainMenu() {
        root.setScreen(root.mainMenuScreen);
    }

    private void setupInputProcessor() {
        this.input = new InputMultiplexer();
        input.addProcessor(stage);
        input.addProcessor(new InputHandler(this, renderer.getCamera()));
        input.addProcessor(new GestureDetector(new GestureHandler(this)));
        Gdx.input.setInputProcessor(input);
    }

    private void createNewLevel() {
        Hero hero = new Hero();
        Level level = new Level();
        // level.testPrint();

        Repository repo = new Repository(level.getTiles());
        Lighting lighting = new Lighting(repo);
        level.placeInanimates(lighting);
        level.placeEntities(hero);
        repo.updateResistanceMap();
        repo.setActors(level.getEntities(), level.getInanimates());

        this.renderer = new Renderer(hero, lighting, root.spriteBatch, repo);
        this.actionState = new ActionState(this, hero, repo);
        this.menuState = new MenuState(this, hero, repo);
        this.examineState = new ExamineState(stage, hero, repo);
        this.state = actionState;

        setupInputProcessor();
    }
}
