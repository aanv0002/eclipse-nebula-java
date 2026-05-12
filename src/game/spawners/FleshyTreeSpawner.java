package game.spawners;

import edu.monash.fit2099.engine.positions.Ground;
import game.grounds.flora.FleshySprout;

/**
 * Spawns fleshy tree sprouts
 */

public class FleshyTreeSpawner implements TreeSpawner {
    /**
     * Spawns a fleshy sprout
     * @return fleshy sprout
     */

    @Override
    public Ground spawnTree() {
        return new FleshySprout();
    }
}