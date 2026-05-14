package game.grounds.flora;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Middle growth stage of a fleshy tree
 * @author Amra Anver
 * @version 1.0
 */
public class FleshySapling extends FleshyTree {
    /**
     * creates a fleshy sapling
     */
    public FleshySapling() {
        super('v', "Fleshy Sapling");
    }

    @Override
    protected boolean performSpecialAction(Location location) {
        return false;
    }

    @Override
    protected int getGrowthInterval() {
        return 25;
    }

    @Override
    protected int getGrowthChance() {
        return 50;
    }

    @Override
    protected Ground getNextStage() {
        return new FleshyMatureTree();
    }
}
