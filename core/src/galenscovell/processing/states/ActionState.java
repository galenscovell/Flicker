package galenscovell.processing.states;

import galenscovell.graphics.Lighting;
import galenscovell.processing.Repository;
import galenscovell.processing.actions.*;
import galenscovell.things.entities.*;
import galenscovell.things.inanimates.Inanimate;
import galenscovell.ui.components.*;
import galenscovell.ui.screens.GameScreen;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class ActionState implements State {
    private final GameScreen gameScreen;
    private final Lighting lighting;
    private final Hero hero;
    private final Repository repo;
    private final List<Entity> seenEntities;
    private final List<Inanimate> adjacentThings;
    private boolean entityEnteredSight;

    public ActionState(GameScreen gameScreen, Lighting lighting, Hero hero, Repository repo) {
        this.gameScreen = gameScreen;
        this.lighting = lighting;
        this.hero = hero;
        this.repo = repo;
        this.seenEntities = new ArrayList<Entity>();
        this.adjacentThings = new ArrayList<Inanimate>();
        this.entityEnteredSight = false;
    }

    @Override
    public StateType getStateType() {
        return StateType.ACTION;
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {
        clearInanimateBoxes();
    }

    @Override
    public void update(float delta) {
        if (repo.actionsEmpty()) {
            return;
        } else {
            gameScreen.setCameraFollow(true);
            if (!adjacentThings.isEmpty()) {
                adjacentThings.clear();
                clearInanimateBoxes();
            }
            Stack<Action> finishedActions = new Stack<Action>();
            // Hero action is always first in action list
            Action heroAction = repo.getFirstAction();
            if (!entityEnteredSight && heroAction.act()) {
                npcTurn();
                // Iterate through each action and step one act forward
                for (Action action : repo.getActions()) {
                    if (action == heroAction) {
                        continue;
                    }
                    // If action is unable to act, resolve it and add it to finished actions
                    if (!action.act()) {
                        action.resolve();
                        finishedActions.push(action);
                    }
                }
                // Remove all finished actions
                while (!finishedActions.isEmpty()) {
                    repo.removeAction(finishedActions.pop());
                }
            } else {
                // Once heroAction is unable to act or entity entered sight, resolve and clear action list
                heroAction.resolve();
                repo.clearActions();
                heroAdjacentCheck();
                entityEnteredSight = false;
                gameScreen.setCameraFollow(false);
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
                        displayInanimateBox(thing, thingTile);
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
        for (Entity entity : repo.getEntities()) {
            // Check if entity entered player sight this turn
            if (seenEntities.contains(entity) && !isIlluminated(entity)) {
                seenEntities.remove(entity);
            }
            if (!seenEntities.contains(entity) && isIlluminated(entity)) {
                seenEntities.add(entity);
                entityEnteredSight = true;
            }
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

    private boolean isIlluminated(Entity entity) {
        boolean illuminated = lighting.isInLight(entity.getX(), entity.getY());
        // System.out.println(entity + " illuminated: " + illuminated);
        return illuminated;
    }

    private void displayInanimateBox(Inanimate inanimate, Tile tile) {
        InteractionVerticalGroup interactionGroup = gameScreen.getStage().getRoot().findActor("interactionGroup");
        if (interactionGroup != null) {
            interactionGroup.addActor(new InteractButton(gameScreen, inanimate, tile));
        }
    }

    private void clearInanimateBoxes() {
        InteractionVerticalGroup interactionGroup = gameScreen.getStage().getRoot().findActor("interactionGroup");
        if (interactionGroup != null) {
            interactionGroup.clear();
        }
    }
}
