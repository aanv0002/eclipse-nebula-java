package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.inventory.BasicInventory;
import game.states.CreatureState;
import game.states.IdleState;
import game.states.StateContext;

/**
 * A stateful HD creature that changes behaviour depending on its surroundings.
 *
 * The mannequin has four deterministic states:
 * Idle, Stalking, Active, and Berserk. Its next state is never random.
 * Instead, transitions are based on surrounding workers and nearby ground items.
 *
 * Each state transition triggers an immediate effect through the state's
 * onEnter method, separate from the creature's normal turn action.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class AnomalousMannequin extends Actor {

    /**
     * Current state of the mannequin.
     */
    private CreatureState currentState;

    /**
     * Shared helper object used by states to inspect the environment.
     */
    private final StateContext context;

    /**
     * Creates an anomalous mannequin.
     */
    public AnomalousMannequin() {
        super("Anomalous Mannequin", 'A', 40, new BasicInventory());
        this.currentState = new IdleState();
        this.context = new StateContext();
        this.setIntrinsicWeapon(new IntrinsicWeapon(3, "strikes", 80, "mannequin fist") {});
    }

    /**
     * Chooses the mannequin's turn action.
     *
     * First, the current state deterministically decides whether the mannequin
     * should transition to another state. If the state changes, the new state's
     * immediate transition effect is triggered. Then, the current state chooses
     * the mannequin's normal turn action.
     *
     * @param actions possible actions
     * @param lastAction previous action
     * @param map current map
     * @param display display object
     * @return chosen action
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        Location location = map.locationOf(this);

        CreatureState nextState = currentState.getNextState(this, location, context);

        if (!currentState.getName().equals(nextState.getName())) {
            String oldStateName = currentState.getName();

            currentState = nextState;

            display.println(this + " transitions from " + oldStateName
                    + " state to " + currentState.getName() + " state.");

            currentState.onEnter(this, location);
        }

        Action action = currentState.getAction(this, location, context);

        if (action == null) {
            return new DoNothingAction();
        }

        return action;
    }
}