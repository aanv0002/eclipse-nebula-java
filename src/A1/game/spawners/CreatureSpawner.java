package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * A contract for classes that can spawn creatures.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public interface CreatureSpawner {

    /**
     * Spawns a creature.
     *
     * @return the spawned actor
     */
    Actor spawnCreature();
}