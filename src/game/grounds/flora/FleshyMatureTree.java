package game.grounds.flora;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.spawners.FleshyTreeCreatureSpawner;

/**
 * Final growth stage of a fleshy tree.
 * A mature fleshy tree spawns an Undead when a worker is nearby
 */
public class FleshyMatureTree extends FleshyTree {

    /**
     * Spawner used to spawn Undead when a worker is nearby.
     */
    private final FleshyTreeCreatureSpawner creatureSpawner = new FleshyTreeCreatureSpawner();

    /**
     * Creates a mature fleshy tree.
     */
    public FleshyMatureTree() {
        super('Y', "Fleshy Mature Tree");
    }

    /**
     * Spawns an Undead if a worker is beside the mature tree which is its special action
     *
     * @param location the current location of the mature tree
     * @return true if an Undead was successfully spawned
     */
    @Override
    protected boolean performSpecialAction(Location location) {
        return creatureSpawner.spawnUndeadIfWorkerNearby(location);
    }

    /**
     * Returns the number of turns before growth is attempted
     * Mature trees are the final stage so they do not grow further
     *
     * @return the maximum integer value because this tree does not grow further
     */
    @Override
    protected int getGrowthInterval() {
        return Integer.MAX_VALUE;
    }

    /**
     * Returns the percentage chance of growing.
     *
     * @return 0 because this tree does not grow further
     */
    @Override
    protected int getGrowthChance() {
        return 0;
    }

    /**
     * Returns the next growth stage.
     *
     * @return null because this is the final stage
     */
    @Override
    protected Ground getNextStage() {
        return null;
    }
}