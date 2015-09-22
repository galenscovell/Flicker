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
import galenscovell.ui.HudStage;
import galenscovell.world.Level;

public class GameScreen extends AbstractScreen {
    private Hero hero;
    private Renderer renderer;
    private State state, movementState, combatState, menuState;
    private InputMultiplexer input;

    private final int timestep = 20;
    private int accumulator = 0;

    public GameScreen(FlickerMain root) {
        super(root);
        create();
    }

    @Override
    public void create() {
        this.hero = new Hero();
        this.stage = new HudStage(this, root.spriteBatch);
        createNewLevel();
    }

    @Override
    public void render(float delta) {
        // Update
        if (accumulator > timestep) {
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
        // Call from HUD stage
        state.exit();
        switch (stateType) {
            case MOVEMENT:
                state = movementState;
                break;
            case COMBAT:
                state = combatState;
                break;
            case MENU:
                state = menuState;
                break;
        }
        state.enter();
    }

    public void passInputToState(float x, float y) {
        state.handleInput(x, y);
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
        Level level = new Level();
        level.assembleLevel(hero);
        // level.testPrint();

        Repository.create(level.getTiles(), level.getEntities(), level.getInanimates());
        Lighting lighting = new Lighting();
        level.placeInanimates(lighting);

        this.renderer = new Renderer(hero, lighting, root.spriteBatch);
        this.movementState = new MovementState(hero);
        this.combatState = new CombatState(hero);
        this.menuState = new MenuState();
        this.state = movementState;

        setupInputProcessor();
    }
}
