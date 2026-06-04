package game.grounds.flora;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Final growth stage of a fleshy tree.
 * @author Amra Anver
 * @version 1.0
 */

public class FleshyMonolith extends FleshyTree{

    private final WorkerWarpService warpService = new WorkerWarpService();

    public FleshyMonolith(FleshyTreeType treeType) {
        super('H', "Fleshy Monolith", treeType);
    }

    @Override
    protected boolean performSpecialAction(Location location) {
        return warpService.warpNearbyWorker(location);
    }

    /**
     * Returns the number of turns before growth is attempted
     * Monoliths are the final stage so they do not grow further
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
