package game.states;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.IdleScanBehaviour;
import game.effects.CorruptSurroundingsEffect;

/**
 * Idle state.
 *
 * Deterministic transitions:
 * - If two or more adjacent workers are detected, transition to BerserkState.
 * - If exactly one worker is detected within 3 tiles, transition to StalkingState.
 * - If no worker is detected within 3 tiles but an item is detected within 3 tiles, transition to ActiveState.
 * - Otherwise, remain IdleState.
 *
 * Regular behaviour:
 * - Uses IdleScanBehaviour to stay still while scanning.
 *
 * Immediate transition effect:
 * - Corrupts surrounding ground tiles into ToxicWaste.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
public class IdleState implements CreatureState {

    /**
     * Behaviour used while the mannequin is idle.
     */
    private final IdleScanBehaviour idleBehaviour = new IdleScanBehaviour();

    /**
     * Determines the next state from IdleState.
     *
     * @param actor the stateful creature
     * @param location current location
     * @param context helper for environmental checks
     * @return next state, or this state if conditions are not met
     */
    @Override
    public CreatureState getNextState(Actor actor, Location location, StateContext context) {
        int adjacentWorkers = context.countAdjacentWorkers(location);
        int nearbyWorkers = context.countWorkersWithin(location, 3);

        if (adjacentWorkers >= 2) {
            return new BerserkState();
        }

        if (nearbyWorkers == 1) {
            return new StalkingState();
        }

        if (nearbyWorkers == 0 && context.hasItemWithin(location, 3)) {
            return new ActiveState();
        }

        return this;
    }

    /**
     * Returns the idle behaviour action.
     *
     * @param actor the stateful creature
     * @param location current location
     * @param context helper for environmental checks
     * @return DoNothingAction through IdleScanBehaviour
     */
    @Override
    public Action getAction(Actor actor, Location location, StateContext context) {
        return idleBehaviour.operate(actor, location);
    }

    /**
     * Applies the immediate transition effect for entering IdleState.
     *
     * @param actor the stateful creature
     * @param location current location
     */
    @Override
    public void onEnter(Actor actor, Location location) {
        new CorruptSurroundingsEffect().apply(actor, location);
    }

    /**
     * Returns the state name.
     *
     * @return Idle
     */
    @Override
    public String getName() {
        return "Idle";
    }
}