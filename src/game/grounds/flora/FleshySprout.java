package game.grounds.flora;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.spawners.FleshyTreeCreatureSpawner;

/**
 * First growth stage of a fleshy tree.
 * @author Amra Anver
 * @version 2.0
 */
public class FleshySprout extends FleshyTree {

    /**
     * Spawner used to spawn Slime when a worker is nearby.
     */
    private final FleshyTreeCreatureSpawner creatureSpawner = new FleshyTreeCreatureSpawner();

    /**
     * Creates a fleshy sprout.
     */
    public FleshySprout(FleshyTreeType treeType) {
        super('y', "Fleshy Sprout", treeType);
    }

    /**
     * Spawns a Slime if a worker is beside the sprout.
     *
     * @param location the current location of the sprout
     * @return true if the sprout performed an action
     */
    @Override
    protected boolean performSpecialAction(Location location) {

        if (treeType == FleshyTreeType.OVERFLOW) {
            return creatureSpawner.spawnSlimeIfWorkerNearby(location);
        }

        return creatureSpawner.spawnUndeadIfWorkerNearby(location);
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
     * @return a fleshy mature tree
     */
    @Override
    protected Ground getNextStage() {

        if (treeType == FleshyTreeType.OVERFLOW) {
            return new FleshySapling(treeType);
        }

        return new FleshyMatureTree(FleshyTreeType.DEPRECATED);
    }
}