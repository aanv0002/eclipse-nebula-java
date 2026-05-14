package game.grounds.flora;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Final stage of a warper tree
 * @author Amra Anver
 * @version 1.0
 */
public class WarperMatureTree extends WarperTree {
    /**
     * Creates a warper mature tree
     */
    public WarperMatureTree() {
        super('W', "Warper Mature Tree");
    }

    @Override
    protected boolean performSpecialAction(Location location) {
        return warpNearbyWorker(location);
    }

    @Override
    protected int getGrowthInterval() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected int getGrowthChance() {
        return 0;
    }

    @Override
    protected Ground getNextStage() {
        return null;
    }
}