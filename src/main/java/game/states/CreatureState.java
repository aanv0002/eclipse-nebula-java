package game.states;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Represents one deterministic state of a stateful creature.
 *
 * Each state controls:
 * 1. what state comes next,
 * 2. what immediate effect happens when entering the state,
 * 3. what action the creature performs during its turn.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public interface CreatureState {

    /**
     * Determines the next state without randomness.
     *
     * @param actor the stateful creature
     * @param location current location
     * @param context helper for environmental checks
     * @return next state, or this state if no transition occurs
     */
    CreatureState getNextState(Actor actor, Location location, StateContext context);

    /**
     * Returns the action performed in this state.
     *
     * @param actor the stateful creature
     * @param location current location
     * @param context helper for environmental checks
     * @return action to perform, or null to do nothing
     */
    Action getAction(Actor actor, Location location, StateContext context);

    /**
     * Triggers the immediate effect when entering this state.
     *
     * @param actor the stateful creature
     * @param location current location
     */
    void onEnter(Actor actor, Location location);

    /**
     * Returns the state name.
     *
     * @return state name
     */
    String getName();
}