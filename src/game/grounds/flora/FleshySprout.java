package game.grounds.flora;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.Slime;

/**
 * First growth stage of a fleshy tree
 */
public class FleshySprout extends FleshyTree{
    /**
     * Creates a fleshy sprout
     */
    public FleshySprout() {
        super('y', "Fleshy Sprout");
    }
    @Override
    protected boolean performSpecialAction(Location location) {
        if (isWorkerNearby(location)) {
            return spawnActorNearby(location, new Slime());
        }
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
        return new FleshySapling();
    }
}
