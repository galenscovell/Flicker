package galenscovell.processing.states;

import galenscovell.processing.Repository;
import galenscovell.processing.actions.*;
import galenscovell.things.entities.*;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.ui.screens.GameScreen;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class ActionState implements State {
    private final GameScreen root;
    private final Hero hero;
    private final Repository repo;
    private List<Inanimate> adjacentThings;

    public ActionState(GameScreen root, Hero hero, Repository repo) {
        this.root = root;
        this.hero = hero;
        this.repo = repo;
        this.adjacentThings = new ArrayList<Inanimate>();
    }

    @Override
    public StateType getStateType() {
        return StateType.ACTION;
    }

    @Override
    public void enter() {
        System.out.println("\tEntering ACTION state");
    }

    @Override
    public void exit() {
        System.out.println("\tLeaving ACTION state\n");
    }

    @Override
    public void update(float delta) {
        if (repo.actionsEmpty()) {
            return;
        } else {
            if (!adjacentThings.isEmpty()) {
                adjacentThings.clear();
                root.clearInanimateBoxes();
            }
            Stack<Action> finishedActions = new Stack<Action>();
            Action heroAction = repo.getFirstAction();  // Hero action is always first in action list
            if (heroAction.act()) {
                npcTurn();
                // Iterate through each action and step one act forward
                for (Action action : repo.getActions()) {
                    // Skip first action (Hero action has already acted)
                    if (action == heroAction) {
                        continue;
                    }
                    // If an action is unable to act, resolve it and add it to finished action
                    if (!action.act()) {
                        action.resolve();
                        finishedActions.push(action);
                    }
                }
                // Remove all finished actions from action list
                while (!finishedActions.isEmpty()) {
                    repo.removeAction(finishedActions.pop());
                }
            } else {
                // Once heroAction is unable to act, resolve it and clear action list
                heroAction.resolve();
                repo.clearActions();
                heroAdjacentCheck();
            }
        }
    }

    private void heroAdjacentCheck() {
        int heroTileX = hero.getX() / Constants.TILESIZE;
        int heroTileY = hero.getY() / Constants.TILESIZE;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 || dy == 0) {
                    Inanimate thing = repo.findInanimate(heroTileX + dx, heroTileY + dy);
                    if (thing != null) {
                        adjacentThings.add(thing);
                        Tile thingTile = repo.findTile(heroTileX + dx, heroTileY + dy);
                        root.displayInanimateBox(thing, thingTile);
                    }
                }
            }
        }
    }

    @Override
    public void handleInput(float x, float y) {
        int convertX = (int) (x / Constants.TILESIZE);
        int convertY = (int) (y / Constants.TILESIZE);
        Move heroMove = new Move(hero, repo);
        heroMove.setTarget(repo.findTile(convertX, convertY));
        if (heroMove.initialize()) {
            // If action currently being processed, replace old user action with new
            if (!repo.actionsEmpty()) {
                repo.clearActions();
            }
            repo.addAction(heroMove);
        }
    }

    @Override
    public void handleInterfaceEvent(int moveType) {

    }

    private void npcTurn() {
        for (Entity entity : repo.entities) {
            if (entity.movementTimer()) {
                // Check if entity has another action in action list and remove it
                Action previousAction = null;
                for (Action action : repo.getActions()) {
                    if (action.getUser() == entity) {
                        previousAction = action;
                    }
                }
                if (previousAction != null) {
                    repo.removeAction(previousAction);
                }
                // Create new entity action with entity behaviors
                Action npcAction;
                if (entity.isAggressive()) {
                    // TODO: Aggressive behavior depending on entity
                    Tile targetTile = repo.findTile(hero.getX() / Constants.TILESIZE, hero.getY() / Constants.TILESIZE);
                    npcAction = new Move(entity, repo);
                    npcAction.setTarget(targetTile);
                } else {
                    // TODO: Passive behavior depending on entity
                    Tile targetTile = repo.findTile(hero.getX() / Constants.TILESIZE, hero.getY() / Constants.TILESIZE);
                    npcAction = new Move(entity, repo);
                    npcAction.setTarget(targetTile);
                }
                if (npcAction.initialize()) {
                    repo.addAction(npcAction);
                }
            }
        }
    }
}
