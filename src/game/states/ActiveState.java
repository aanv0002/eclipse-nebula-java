package game.states;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.CollectGroundItemsBehaviour;
import game.effects.ScatterNearbyItemsEffect;

/**
 * Active state.
 *
 * Deterministic transitions:
 * - If a worker is adjacent, transition to BerserkState.
 * - If a worker is detected within 3 tiles but not adjacent, transition to StalkingState.
 * - If no item is detected within 3 tiles, transition to IdleState.
 * - Otherwise, remain Active state.
 *
 * Regular behaviour:
 * - Uses CollectGroundItemsBehaviour to collect ground items or move toward ground items.
 *
 * Immediate transition effect:
 * - Scatters nearby ground items to adjacent tiles.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class ActiveState implements CreatureState {

    private final CollectGroundItemsBehaviour collectBehaviour = new CollectGroundItemsBehaviour();

    @Override
    public CreatureState getNextState(Actor actor, Location location, StateContext context) {
        int adjacentWorkers = context.countAdjacentWorkers(location);
        int nearbyWorkers = context.countWorkersWithin(location, 3);

        if (adjacentWorkers >= 1) {
            return new BerserkState();
        }

        if (nearbyWorkers > 0) {
            return new StalkingState();
        }

        if (!context.hasItemAt(location) && !context.hasItemWithin(location, 3)) {
            return new IdleState();
        }

        return this;
    }

    @Override
    public Action getAction(Actor actor, Location location, StateContext context) {
        return collectBehaviour.operate(actor, location);
    }

    @Override
    public void onEnter(Actor actor, Location location) {
        new ScatterNearbyItemsEffect().apply(actor, location);
    }

    @Override
    public String getName() {
        return "Active";
    }
}