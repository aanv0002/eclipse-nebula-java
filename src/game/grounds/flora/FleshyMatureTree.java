package game.grounds.flora;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.Undead;

/**
 * Final growth stage of a fleshy tree
 */
public class FleshyMatureTree extends FleshyTree {
    /**
     * Creates a fleshy mature tree
     */
    public FleshyMatureTree() {
        super('Y', "Fleshy Mature Tree");
    }

    @Override
    protected boolean performSpecialAction(Location location) {
        if (isWorkerNearby(location)) {
            return spawnActorNearby(location, new Undead());
        }
        return false;
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