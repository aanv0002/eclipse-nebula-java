package game.states;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.BerserkAttackAndDropBehaviour;
import game.effects.IgniteSurroundingsEffect;

/**
 * Berserk state.
 *
 * Deterministic transitions:
 * - If no worker is detected within 3 tiles radius and an item is detected within 3 tiles, transition to ActiveState.
 * - If no worker and no item are detected within 3 tiles radius, transition to IdleState.
 * - If a worker is detected within 3 tiles radius but not adjacent, transition to StalkingState.
 * - Otherwise, remain BerserkState.
 *
 * Regular behaviour:
 * - Uses BerserkAttackAndDropBehaviour.
 * - If an adjacent worker is found, the mannequin attacks the worker with a hitrate of 80% and forces
 *   the worker to drop all inventory items in the same action.
 *
 * Immediate transition effect:
 * - Ignites adjacent tiles with Fire.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
public class BerserkState implements CreatureState {

    /**
     * Behaviour used while berserk.
     */
    private final BerserkAttackAndDropBehaviour berserkBehaviour = new BerserkAttackAndDropBehaviour();

    /**
     * Determines the next state from BerserkState.
     *
     * @param actor the stateful creature
     * @param location current location
     * @param context helper for environmental checks
     * @return next state, or this state if no transition condition is met
     */
    @Override
    public CreatureState getNextState(Actor actor, Location location, StateContext context) {
        int adjacentWorkers = context.countAdjacentWorkers(location);
        int nearbyWorkers = context.countWorkersWithin(location, 3);

        if (nearbyWorkers == 0 && context.hasItemWithin(location, 3)) {
            return new ActiveState();
        }

        if (nearbyWorkers == 0) {
            return new IdleState();
        }

        if (nearbyWorkers > 0 && adjacentWorkers == 0) {
            return new StalkingState();
        }

        return this;
    }

    /**
     * Returns the berserk attack-and-drop behaviour action.
     *
     * @param actor the stateful creature
     * @param location current location
     * @param context helper for environmental checks
     * @return attack-and-drop action, or null if no adjacent worker exists
     */
    @Override
    public Action getAction(Actor actor, Location location, StateContext context) {
        return berserkBehaviour.operate(actor, location);
    }

    /**
     * Applies the immediate transition effect for entering BerserkState.
     *
     * @param actor the stateful creature
     * @param location current location
     */
    @Override
    public void onEnter(Actor actor, Location location) {
        new IgniteSurroundingsEffect().apply(actor, location);
    }

    /**
     * Returns the state name.
     *
     * @return Berserk
     */
    @Override
    public String getName() {
        return "Berserk";
    }
}