package galenscovell.processing;

import galenscovell.processing.actions.AttackAction;
import galenscovell.processing.actions.MoveAction;
import galenscovell.things.entities.Entity;
import galenscovell.things.entities.Hero;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.ui.HudStage;
import galenscovell.ui.screens.GameScreen;
import galenscovell.world.Tile;

import java.util.List;
import java.util.Map;

public class Updater {
    private GameScreen game;
    private HudStage hud;
    private Map<Integer, Tile> tiles;
    private List<Entity> entities;
    private List<Inanimate> inanimates;
    private Hero hero;
    private Pathfinder pathfinder;
    private AttackAction attackAction;
    private MoveAction moveAction;

    public Updater(GameScreen game, Hero hero, Map<Integer, Tile> tiles) {
        this.game = game;
        this.hero = hero;
        this.tiles = tiles;
        this.pathfinder = new Pathfinder();
        this.attackAction = new AttackAction(this, tiles);
        this.moveAction = new MoveAction(this, tiles);
    }

    public void setHud(HudStage hud) {
        this.hud = hud;
    }

    public void setLists(List<Entity> entities, List<Inanimate> inanimates) {
        this.entities = entities;
        this.inanimates = inanimates;
    }

    public boolean update(int[] destination) {
        if (hud.restrictMovement() || !hud.clearMenus()) {
            return false;
        }
        return moveAction.updateMovement(destination);
    }

    public void displayAttackRange(String move) {
        attackAction.setRange(hero, move);
    }

    public void attack(float x, float y) {
        hud.clearMenus();
        int targetX = (int) x / tileSize;
        int targetY = (int) y / tileSize;
        Entity targetEntity = findEntity(targetX, targetY);
        Tile targetTile = findTile(targetX, targetY);
        attackAction.finalizeMove(hero, targetEntity, targetTile);
        game.endAttackMode();
    }

    private Inanimate findInanimate(int x, int y) {
        Inanimate inanimate = null;
        for (Inanimate object : inanimates) {
            if (object.getX() == x && object.getY() == y) {
                inanimate = object;
            }
        }
        return inanimate;
    }

    private Entity findEntity(int x, int y) {
        Entity target = null;
        for (Entity entity : entities) {
            if ((entity.getX() / tileSize) == x && (entity.getY() / tileSize) == y) {
                target = entity;
            }
        }
        return target;
    }
}