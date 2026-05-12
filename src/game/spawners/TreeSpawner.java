package game.spawners;

import edu.monash.fit2099.engine.positions.Ground;

/**
 * Interface for spawning tree grounds
 */

public interface TreeSpawner {
    /**
     * Creates and returns the initial stage of a tree ground
     * @return spawned tree ground
     */
    Ground spawnTree();
}