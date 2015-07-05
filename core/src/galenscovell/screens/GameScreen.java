package galenscovell.screens;

import galenscovell.entities.Player;
import galenscovell.flicker.FlickerMain;
import galenscovell.logic.Renderer;
import galenscovell.logic.Updater;
import galenscovell.logic.Level;
import galenscovell.util.Constants;
import galenscovell.util.GestureHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;

/**
 * GAME SCREEN
 * Main gameplay screen.
 * Renderer handles graphics and level setup, Updater handles core logic.
 *
 * @author Galen Scovell
 */

public class GameScreen extends AbstractScreen {
    private Player player;
    private Renderer renderer;
    private Updater updater;
    private InputMultiplexer fullInput;

    private boolean attackMode, examineMode, moving;
    private final int timestep = 20;
    private int accumulator = 0;
    private int[] destination;

    public GameScreen(FlickerMain root) {
        super(root);
        create();
    }

    protected void create() {
        // GLProfiler.enable();
        this.player = new Player();
        this.stage = new HudStage(this, player, root.spriteBatch);
        createNewLevel();
    }

    public void update(float delta) {
        if (accumulator > timestep) {
            accumulator = 0;
            if (moving && !updater.update(destination)) {
                moving = false;
            }
            if (updater.descend()) {
                renderer.dispose();
                createNewLevel();
            }
        }
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render((double) accumulator / timestep);
        stage.draw();
        accumulator++;
        // System.out.println("Draw calls: " + GLProfiler.drawCalls + ", Texture binds: " + GLProfiler.textureBindings);
        // GLProfiler.reset();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        enableWorldInput();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        stage.dispose();
    }

    public void playerMove(float x, float y) {
        moving = true;
        destination[0] = (int) x;
        destination[1] = (int) y;
        if (destination[0] / Constants.TILESIZE == player.getX() / Constants.TILESIZE && destination[1] / Constants.TILESIZE == player.getY() / Constants.TILESIZE) {
            renderer.toggleLight();
        }
    }

    public void screenZoom(float zoom) {
        renderer.zoom(zoom);
    }

    public void toMainMenu() {
        root.setScreen(root.mainMenuScreen);
    }

    public void disableWorldInput() {
        Gdx.input.setInputProcessor(stage);
    }

    public void enableWorldInput() {
        Gdx.input.setInputProcessor(fullInput);
    }

    public boolean examineMode() {
        return examineMode;
    }

    public boolean attackMode() {
        return attackMode;
    }

    public void toggleMode(int mode) {
        if (mode == 0) {
            attackMode = !attackMode;
            if (examineMode) {
                examineMode = false;
            }
        } else {
            examineMode = !examineMode;
            if (attackMode) {
                attackMode = false;
            }
        }
    }

    public void examine(float x, float y) {
        updater.examine(x, y);
    }

    public void attack(float x, float y) {
        updater.attack(x, y);
    }

    private void createNewLevel() {
        Level level = new Level();
        // Modify smoothing passes here
        for (int i = 0; i < 6; i++) {
            level.update();
        }
        level.optimize();
        this.renderer = new Renderer(level.getTiles(), root.spriteBatch);
        this.updater = new Updater(player, level.getTiles());
        renderer.assembleLevel(player);
        updater.setHud((HudStage) stage);
        updater.setLists(renderer.getEntityList(), renderer.getInanimateList());

        this.fullInput = new InputMultiplexer();
        fullInput.addProcessor(stage);
        fullInput.addProcessor(new GestureDetector(new GestureHandler(this, renderer.getCamera())));
        enableWorldInput();

        this.destination = new int[2];
        this.moving = false;
    }
}
