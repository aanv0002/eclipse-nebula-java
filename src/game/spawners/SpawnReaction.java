package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Reaction that runs when a creature enters the map.
 */
public interface SpawnReaction {

    boolean supports(Actor creature);

    void apply(Actor creature, Location location);
}
