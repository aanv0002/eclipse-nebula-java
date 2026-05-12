package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.ConsumeGroundItemBehaviour;
import game.inventory.BasicInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * A non-hostile slime that wanders randomly and may consume consumable items
 * directly from the ground.
 *
 * In REQ4, the Slime is marked as a creature so that spawn reactions can count
 * it as a nearby creature.
 *
 * @author Tan Hong Sze
 * @author Shrabonti Majumder
 * @version 2.0
 */
public class Slime extends Actor {

    /**
     * Running number for slime naming.
     */
    private static int counter = 1;

    /**
     * Random generator for slime decisions.
     */
    private final Random random = new Random();

    /**
     * Prioritised behaviours of the slime.
     */
    private final Map<Integer, Behaviour<Actor, Action>> behaviours = new TreeMap<>();

    /**
     * Creates a slime.
     */
    public Slime() {
        super("#" + counter++ + " Slime", 's', 25, new BasicInventory());
        this.enableAbility(ActorCapabilities.CREATURE);
        this.enableAbility(ActorCapabilities.SLIME);
        behaviours.put(1, new ConsumeGroundItemBehaviour());
    }

    /**
     * Chooses an action for the slime.
     *
     * @param actions collection of possible Actions for this Actor
     * @param lastAction the Action this Actor took last turn
     * @param map the map containing the Actor
     * @param display the I/O object
     * @return the chosen action
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        Location here = map.locationOf(this);

        Action consumeAction = null;
        for (Behaviour<Actor, Action> behaviour : behaviours.values()) {
            Action action = behaviour.operate(this, here);
            if (action != null) {
                consumeAction = action;
                break;
            }
        }

        Action moveAction = getRandomMoveAction(here);

        if (consumeAction != null && moveAction != null) {
            if (random.nextBoolean()) {
                return consumeAction;
            }
            return moveAction;
        }

        if (consumeAction != null) {
            return consumeAction;
        }

        if (moveAction != null) {
            return moveAction;
        }

        return new DoNothingAction();
    }

    /**
     * Returns a random valid movement action.
     *
     * @param here the current location
     * @return a movement action, or null if no move is possible
     */
    private Action getRandomMoveAction(Location here) {
        List<Action> validMoves = new ArrayList<>();

        for (Exit exit : here.getExits()) {
            Action moveAction = exit.getDestination().getMoveAction(this, exit.getName(), exit.getHotKey());
            if (moveAction != null) {
                validMoves.add(moveAction);
            }
        }

        if (validMoves.isEmpty()) {
            return null;
        }

        return validMoves.get(random.nextInt(validMoves.size()));
    }
}
