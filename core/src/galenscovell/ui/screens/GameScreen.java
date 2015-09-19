package galenscovell.ui.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import galenscovell.flicker.FlickerMain;
import galenscovell.processing.Renderer;
import galenscovell.processing.controls.*;
import galenscovell.processing.states.*;
import galenscovell.things.entities.Hero;
import galenscovell.ui.HudStage;
import galenscovell.world.*;

import java.util.Map;

public class GameScreen extends AbstractScreen {
    private Hero hero;
    private Renderer renderer;
    private State state, movementState, combatState, menuState;
    private InputMultiplexer input;
    private Map<Integer, Tile> tiles;

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
        state.update(delta);
        stage.act(delta);
        accumulator++;

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
        renderer.dispose();
        stage.dispose();
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

    private void createNewLevel() {
        Level level = new Level();
        level.optimize();
        this.tiles = level.getTiles();

        this.renderer = new Renderer(tiles, root.spriteBatch);
        renderer.assembleLevel(hero);
        // level.testPrint();

        this.movementState = new MovementState(tiles);
        this.combatState = new CombatState(tiles);
        this.menuState = new MenuState();
        this.state = movementState;

        this.input = new InputMultiplexer();
        input.addProcessor(stage);
        input.addProcessor(new InputHandler(this, renderer.getCamera()));
        input.addProcessor(new GestureDetector(new GestureHandler(this)));
        Gdx.input.setInputProcessor(input);
    }
}
