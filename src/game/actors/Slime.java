package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.GameMap;
import game.behaviours.ConsumeGroundItemBehaviour;
import game.inventory.BasicInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * A non-hostile slime that wanders randomly and may consume consumable items directly from the ground.
 *
 * @author Tan Hong Sze
 * @version 1.0
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
        super("#" + counter++ + " Slime", '⍾', 25, new BasicInventory());
        behaviours.put(1, new ConsumeGroundItemBehaviour());
    }

    /**
     * Chooses an action for the slime.
     *
     * If the slime is standing on a consumable item, it randomly decides whether
     * to consume the item or move away. This means it is not forced to stay on
     * the same tile until a multi-use consumable such as Cookies is fully depleted.
     *
     * @param actions collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn
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

        // If the slime is on a consumable item, randomly decide to eat or move.
        if (consumeAction != null && !(moveAction instanceof DoNothingAction)) {
            if (random.nextBoolean()) {
                return consumeAction;
            }
            return moveAction;
        }

        // If there is something to consume and movement is not possible, consume it.
        if (consumeAction != null) {
            return consumeAction;
        }

        return moveAction;
    }

    /**
     * Returns a random valid movement action.
     *
     * @param here the current location
     * @return a movement action, or do nothing if no move is possible
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
            return new DoNothingAction();
        }

        return validMoves.get(random.nextInt(validMoves.size()));
    }
}