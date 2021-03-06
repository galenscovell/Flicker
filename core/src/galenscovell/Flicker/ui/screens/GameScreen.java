package galenscovell.flicker.ui.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import galenscovell.flicker.FlickerMain;
import galenscovell.flicker.graphics.Lighting;
import galenscovell.flicker.processing.*;
import galenscovell.flicker.processing.controls.*;
import galenscovell.flicker.processing.states.*;
import galenscovell.flicker.things.entities.Hero;
import galenscovell.flicker.ui.HudStage;
import galenscovell.flicker.world.Level;

public class GameScreen extends AbstractScreen {
    private final int timestep = 15;
    private int accumulator;
    private State state, actionState, menuState, examineState;
    private Renderer renderer;
    private InputMultiplexer input;

    public GameScreen(FlickerMain root) {
        super(root);
        this.stage = new HudStage(this, this.root.spriteBatch);
        create();
    }

    @Override
    public void create() {
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

    public Stage getStage() {
        return stage;
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
        } else if (stateType == StateType.EXAMINE) {
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

    public void screenZoom(float zoom) {
        renderer.zoom(zoom);
    }

    public void screenPan(float dx, float dy) {
        renderer.pan(dx, dy);
    }

    public void setCameraFollow(boolean setting) {
        renderer.setCameraFollow(setting);
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
        level.placeInanimates(lighting, repo);
        level.placeEntities(hero);
        repo.setActors(level.getEntities(), level.getInanimates());

        this.renderer = new Renderer(hero, lighting, root.spriteBatch, repo);
        this.actionState = new ActionState(this, lighting, hero, repo);
        this.menuState = new MenuState(this, hero, repo);
        this.examineState = new ExamineState(this, hero, repo);
        this.state = actionState;

        setupInputProcessor();
    }
}
