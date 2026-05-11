package game.spawners;

import edu.monash.fit2099.engine.positions.Ground;
import game.grounds.flora.WarperSapling;

/**
 * Spawns warper tree saplings
 */

public class WarperTreeSpawner implements TreeSpawner {
    /**
     * Spawns a warper sapling
     * @return warper sapling
     */

    @Override
    public Ground spawnTree() {
        return new WarperSapling();
    }
}