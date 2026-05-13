package game.grounds.flora;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.spawners.FleshyTreeCreatureSpawner;

/**
 * First growth stage of a fleshy tree.
 */
public class FleshySprout extends FleshyTree {

    /**
     * Spawner used to spawn Slime when a worker is nearby.
     */
    private final FleshyTreeCreatureSpawner creatureSpawner = new FleshyTreeCreatureSpawner();

    /**
     * Creates a fleshy sprout.
     */
    public FleshySprout() {
        super('y', "Fleshy Sprout");
    }

    /**
     * Spawns a Slime if a worker is beside the sprout.
     *
     * @param location the current location of the sprout
     * @return true if the sprout performed an action
     */
    @Override
    protected boolean performSpecialAction(Location location) {
        return creatureSpawner.spawnSlimeIfWorkerNearby(location);
    }

    /**
     * Returns the number of turns before growth is attempted.
     *
     * @return 20 turns
     */
    @Override
    protected int getGrowthInterval() {
        return 20;
    }

    /**
     * Returns the percentage chance of growing.
     *
     * @return 25 percent
     */
    @Override
    protected int getGrowthChance() {
        return 25;
    }

    /**
     * Returns the next growth stage.
     *
     * @return a fleshy sapling
     */
    @Override
    protected Ground getNextStage() {
        return new FleshySapling();
    }
}