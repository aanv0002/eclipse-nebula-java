package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A behaviour for a stateful creature that remains still while scanning its
 * surroundings.
 *
 * This behaviour intentionally returns DoNothingAction because IdleState should
 * freeze the creature in place while transition logic decides whether the
 * surroundings justify a state change.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class IdleScanBehaviour implements Behaviour<Actor, Action> {

    /**
     * Keeps the actor still for this turn.
     *
     * @param actor the actor performing the behaviour
     * @param location the actor's current location
     * @return a DoNothingAction
     */
    @Override
    public Action operate(Actor actor, Location location) {
        return new DoNothingAction();
    }
}