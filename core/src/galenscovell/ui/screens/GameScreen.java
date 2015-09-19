package galenscovell.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import galenscovell.flicker.FlickerMain;
import galenscovell.processing.Renderer;
import galenscovell.processing.Updater;
import galenscovell.things.entities.Hero;
import galenscovell.ui.HudStage;
import galenscovell.util.GestureHandler;
import galenscovell.util.InputHandler;
import galenscovell.world.Level;

public class GameScreen extends AbstractScreen {
    private Hero hero;
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

    @Override
    public void create() {
        // GLProfiler.enable();
        this.hero = new Hero();
        this.stage = new HudStage(this, root.spriteBatch);
        createNewLevel();
    }

    public void update(float delta) {
        if (accumulator > timestep) {
            accumulator = 0;
            if (moving && !updater.update(destination)) {
                moving = false;
            }
        }
        stage.act(delta);
        accumulator++;
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render((double) accumulator / timestep);
        stage.draw();
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
        Gdx.input.setInputProcessor(fullInput);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        stage.dispose();
    }

    public void zoom(float zoom) {
        renderer.zoom(zoom);
    }

    public void screenPan(float dx, float dy) {
        renderer.pan(dx, dy);
    }

    public void toMainMenu() {
        root.setScreen(root.mainMenuScreen);
    }

    public void playerMove(float x, float y) {
        moving = true;
        destination[0] = (int) x;
        destination[1] = (int) y;
    }

    public boolean isAttackMode() {
        return attackMode;
    }

    public void playerAttack(float x, float y) {
        updater.attack(x, y);
    }

    public void startAttackMode(String move) {
        attackMode = true;
        updater.displayAttackRange(move);
    }

    public void endAttackMode() {
        attackMode = false;
    }

    public boolean isExamineMode() {
        return examineMode;
    }

//  public void playerExamine(float x, float y) {
//        updater.examine(x, y);
//  }

//  public void startExamineMode(String move) {
//      examineMode = true;
//      updater.startExamineMode(move);
//  }
//
//  public void endExamineMode() {
//      examineMode = false;
//  }

    private void createNewLevel() {
        Level level = new Level();
        level.optimize();

        this.renderer = new Renderer(level.getTiles(), root.spriteBatch);
        this.updater = new Updater(this, hero, level.getTiles());
        renderer.assembleLevel(hero);
        // level.testPrint();
        updater.setHud((HudStage) stage);
        updater.setLists(renderer.getEntityList(), renderer.getInanimateList());

        this.fullInput = new InputMultiplexer();
        fullInput.addProcessor(stage);
        fullInput.addProcessor(new InputHandler(this, renderer.getCamera()));
        fullInput.addProcessor(new GestureDetector(new GestureHandler(this)));
        Gdx.input.setInputProcessor(fullInput);

        this.destination = new int[2];
        this.moving = false;
    }
}
