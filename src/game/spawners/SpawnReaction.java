package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A reaction that is applied immediately after a creature successfully enters
 * the map. Different creature types can provide different environmental spawn
 * effects without relying on instanceof checks.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public interface SpawnReaction {

    /**
     * Returns whether this reaction should apply to the spawned creature.
     *
     * @param creature the creature that has just spawned
     * @return true if this reaction supports the creature
     */
    boolean supports(Actor creature);

    /**
     * Applies the spawn reaction at the creature's spawn location.
     *
     * @param creature the creature that has just spawned
     * @param location the location where the creature spawned
     */
    void apply(Actor creature, Location location);
}
