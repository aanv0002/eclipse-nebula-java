package game.grounds.flora;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/**
 * First growth stage of a fleshy tree
 */
public class WarperSapling extends WarperTree {
    /**
     * Creates a warper sapling
     */
    public WarperSapling() {
        super('w', "Warper Sapling");
    }

    @Override
    protected boolean performSpecialAction(Location location) {
        return false;
    }

    @Override
    protected int getGrowthInterval() {
        return 20;
    }

    @Override
    protected int getGrowthChance() {
        return 25;
    }

    @Override
    protected Ground getNextStage() {
        return new WarperMatureTree();
    }
}