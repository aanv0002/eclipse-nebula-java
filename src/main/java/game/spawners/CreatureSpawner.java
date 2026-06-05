package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * A contract for classes that can spawn creatures.
 * Spawners may also create a copy of themselves so newly grown holes can
 * inherit the same spawning capabilities as their parent hole.
 *
 * @author Tan Hong Sze
 * @author Shrabonti Majumder
 * @version 2.0
 */
public interface CreatureSpawner {

    /**
     * Spawns a creature.
     *
     * @return the spawned actor
     */
    Actor spawnCreature();

    /**
     * Creates another spawner with the same spawn capabilities.
     *
     * @return a copied spawner
     */
    CreatureSpawner copy();
}
