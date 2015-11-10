package galenscovell.ui.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import galenscovell.flicker.FlickerMain;
import galenscovell.graphics.Lighting;
import galenscovell.processing.*;
import galenscovell.processing.controls.*;
import galenscovell.processing.states.*;
import galenscovell.things.entities.Hero;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.ui.HudStage;
import galenscovell.ui.components.InteractPopup;
import galenscovell.world.Level;

public class GameScreen extends AbstractScreen {
    private final int timestep = 20;
    private Renderer renderer;
    private State state, actionState, menuState;
    private InputMultiplexer input;
    private int accumulator;

    public GameScreen(FlickerMain root) {
        super(root);
        create();
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

    public void changeState(StateType stateType) {
        this.state.exit();
        if (stateType == StateType.ACTION) {
            state = actionState;
        } else {
            state = menuState;
        }
        state.enter();
    }

    public void passInputToState(float x, float y) {
        state.handleInput(x, y);
    }

    public void passInterfaceEventToState(int moveType) {
        state.handleInterfaceEvent(moveType);
    }

    public void displayInanimateBox(Inanimate inanimate) {
        InteractPopup box = new InteractPopup(this.stage, inanimate);
        box.setName("eventBox");
        stage.addActor(box);
    }

    public void clearInanimateBoxes() {
        stage.getRoot().findActor("eventBox").remove();
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

    public void closeSkillMenu() {
        stage.getRoot().findActor("skillMenu").remove();
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
        repo.addActors(level.getEntities(), level.getInanimates());
        lighting.placeTorch(20, 10, 20, 0.5f, 1.0f, 0.5f, 1);
        lighting.placeTorch(40, 20, 20, 1.0f, 0.5f, 0.5f, 1);
        lighting.placeTorch(60, 10, 20, 0.5f, 0.5f, 1.0f, 1);
        lighting.placeTorch(80, 20, 20, 0.98f, 0.9f, 0.9f, 1);

        this.renderer = new Renderer(hero, lighting, root.spriteBatch, repo);
        this.actionState = new ActionState(this, hero, repo);
        this.menuState = new MenuState(this, hero, repo);
        this.state = actionState;

        setupInputProcessor();
    }
}
