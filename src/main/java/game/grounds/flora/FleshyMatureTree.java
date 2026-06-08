package game.grounds.flora;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.spawners.FleshyTreeCreatureSpawner;

/**
 * Middle growth stage of a fleshy tree on the 99-Deprecated Moon
 * Final growth stage of a fleshy tree on the 20-Overflow Moon
 * A mature fleshy tree spawns a scrap snatcher when a worker is nearby
 * @author Amra Anver
 * @version 2.0
 */
public class FleshyMatureTree extends FleshyTree {

    /**
     * Spawner used to spawn Scrap Snatcher when a worker is nearby.
     */
    private final FleshyTreeCreatureSpawner creatureSpawner = new FleshyTreeCreatureSpawner();

    /**
     * Creates a mature fleshy tree.
     */
    public FleshyMatureTree(FleshyTreeType treeType) {
        super('Y', "Fleshy Mature Tree", treeType);
    }

    /**
     * Spawns an Undead if a worker is beside the mature tree which is its special action
     *
     * @param location the current location of the mature tree
     * @return true if an Undead was successfully spawned
     */
    @Override
    protected boolean performSpecialAction(Location location) {

        if (treeType == FleshyTreeType.OVERFLOW) {
            return creatureSpawner.spawnUndeadIfWorkerNearby(location);
        }

        return creatureSpawner.spawnScrapSnatcherIfWorkerNearby(location);
    }

    /**
     * Returns the number of turns before growth is attempted
     * Mature trees are the final stage so they do not grow further
     *
     * @return 35 turns
     */
    @Override
    protected int getGrowthInterval() {

        if (treeType == FleshyTreeType.OVERFLOW) {
            return Integer.MAX_VALUE;
        }

        return 35;
    }

    /**
     * Returns the percentage chance of growing.
     *
     * @return 50 percent
     */
    @Override
    protected int getGrowthChance() {
        if (treeType == FleshyTreeType.OVERFLOW) {
        return 0;
    }

    return 50;
    }

    /**
     * Returns the next growth stage.
     *
     * @return a fleshy monolith
     */
    @Override
    protected Ground getNextStage() {

        if (treeType == FleshyTreeType.OVERFLOW) {
            return null;
        }

        return new FleshyMonolith(treeType);
    }
}