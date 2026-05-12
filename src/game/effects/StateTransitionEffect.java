package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Represents an immediate effect triggered when a creature enters a state.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public interface StateTransitionEffect {

    /**
     * Applies the transition effect.
     *
     * @param actor the stateful creature
     * @param location creature location
     */
    void apply(Actor actor, Location location);
}