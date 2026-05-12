package game.states;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.FollowNearestWorkerBehaviour;
import game.effects.PoisonSurroundingsEffect;

/**
 * Stalking state.
 *
 * Deterministic transitions:
 * - If a worker becomes adjacent, transition to BerserkState.
 * - If no worker is detected within 3 tiles and an item is detected within 3 tiles, transition to ActiveState.
 * - If no worker is detected within 3 tiles and no item are nearby, transition to IdleState.
 * - Otherwise, remain StalkingState.
 *
 * Regular behaviour:
 * - Uses FollowNearestWorkerBehaviour to move toward the nearest worker.
 *
 * Immediate transition effect:
 * - Poisons all adjacent actors for 3 turns.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class StalkingState implements CreatureState {

    private final FollowNearestWorkerBehaviour followBehaviour = new FollowNearestWorkerBehaviour();

    @Override
    public CreatureState getNextState(Actor actor, Location location, StateContext context) {
        int adjacentWorkers = context.countAdjacentWorkers(location);
        int nearbyWorkers = context.countWorkersWithin(location, 3);

        if (adjacentWorkers >= 1) {
            return new BerserkState();
        }

        if (nearbyWorkers == 0 && context.hasItemWithin(location, 3)) {
            return new ActiveState();
        }

        if (nearbyWorkers == 0) {
            return new IdleState();
        }

        return this;
    }

    @Override
    public Action getAction(Actor actor, Location location, StateContext context) {
        return followBehaviour.operate(actor, location);
    }

    @Override
    public void onEnter(Actor actor, Location location) {
        new PoisonSurroundingsEffect().apply(actor, location);
    }

    @Override
    public String getName() {
        return "Stalking";
    }
}