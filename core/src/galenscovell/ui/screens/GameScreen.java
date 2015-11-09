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
    private Renderer renderer;
    private State state, actionState, menuState;
    private InputMultiplexer input;

    private final int timestep = 20;
    private int accumulator;

    public GameScreen(FlickerMain root) {
        super(root);
        this.create();
    }

    @Override
    public void create() {
        this.stage = new HudStage(this, this.root.spriteBatch);
        this.createNewLevel();
    }

    @Override
    public void render(float delta) {
        // Update
        if (this.accumulator > this.timestep) {
            this.accumulator = 0;
            this.state.update(delta);
        }
        this.stage.act(delta);
        this.accumulator++;
        // Render
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.renderer.render((double) this.accumulator / this.timestep);
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.renderer.resize(width, height);
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.input);
    }

    @Override
    public void dispose() {
        this.stage.dispose();
        this.renderer.dispose();
    }

    public void changeState(StateType stateType) {
        this.state.exit();
        if (stateType == StateType.ACTION) {
            this.state = this.actionState;
        } else {
            this.state = this.menuState;
        }
        this.state.enter();
    }

    public void passInputToState(float x, float y) {
        this.state.handleInput(x, y);
    }

    public void passInterfaceEventToState(int moveType) {
        this.state.handleInterfaceEvent(moveType);
    }

    public void displayInanimateBox(Inanimate inanimate) {
        InteractPopup box = new InteractPopup(this.stage, inanimate);
        box.setName("eventBox");
        this.stage.addActor(box);
    }

    public void clearInanimateBoxes() {
        this.stage.getRoot().findActor("eventBox").remove();
    }

    public void screenZoom(float zoom) {
        this.renderer.zoom(zoom);
    }

    public void screenPan(float dx, float dy) {
        this.renderer.pan(dx, dy);
    }

    public void toMainMenu() {
        this.root.setScreen(this.root.mainMenuScreen);
    }

    public void closeSkillMenu() {
        this.stage.getRoot().findActor("skillMenu").remove();
    }

    private void setupInputProcessor() {
        this.input = new InputMultiplexer();
        this.input.addProcessor(this.stage);
        this.input.addProcessor(new InputHandler(this, this.renderer.getCamera()));
        this.input.addProcessor(new GestureDetector(new GestureHandler(this)));
        Gdx.input.setInputProcessor(this.input);
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

        this.renderer = new Renderer(hero, lighting, this.root.spriteBatch, repo);
        this.actionState = new ActionState(this, hero, repo);
        this.menuState = new MenuState(this, hero, repo);
        this.state = this.actionState;

        this.setupInputProcessor();
    }
}
