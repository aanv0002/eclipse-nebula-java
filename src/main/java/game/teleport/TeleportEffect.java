package game.teleport;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Represents a side effect caused by teleportation.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public interface TeleportEffect {

    /**
     * Applies the teleport side effect.
     *
     * @param actor the actor teleporting
     * @param source source location
     * @param destination final destination location
     */
    void apply(Actor actor, Location source, Location destination);
}