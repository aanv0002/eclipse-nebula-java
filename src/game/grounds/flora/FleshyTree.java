package game.grounds.flora;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import java.util.Random;

/**
 * Abstract parent class for all fleshy trees
 * @author Amra Anver
 * @version 1.0
 */
public abstract class FleshyTree extends Ground {
    /**
     * Random number generator used for growth and spawning
     */
    protected final Random random = new Random();

    /**
     * Variable which counts turns for growth
     */
    private int turnCounter = 0;
    protected final FleshyTreeType treeType;

    /**
     * Constructor
     * @param displayChar display character
     * @param name ground name
     */
    public FleshyTree(char displayChar, String name, FleshyTreeType treeType) {
        super(displayChar, name);
        this.treeType = treeType;
    }

    /**
     * Handles tree behaviour every tick
     * @param location current location
     */
    @Override
    public void tick(Location location) {
        if (performSpecialAction(location)) {
            return;
        }
        turnCounter++;

        if (turnCounter >= getGrowthInterval()) {
            turnCounter = 0;
            if (random.nextInt(100) < getGrowthChance()) {
                Ground nextStage = getNextStage();
                if(nextStage != null) {
                    location.setGround(nextStage);
                    new Display().println(this + " grows into " + nextStage + " at " + location + ".");
                }
            }
        }
    }


    /**
     * Prevents actors from stepping on trees
     * @param actor actor attempting to enter
     * @return false always
     */
    public boolean canActorEnter(Actor actor) {
        return false;
    }

    /**
     * Performs the tree's special action which is unique to each stage and each tree type
     * @param location current location
     * @return true if action performed
     */
    protected abstract boolean performSpecialAction(Location location);

    /**
     * Returns growth interval
     * @return growth interval
     */
    protected abstract int getGrowthInterval();

    /**
     * Returns growth chance percentage
     * @return growth chance
     */
    protected abstract int getGrowthChance();

    /**
     * Returns next growth stage
     * @return next stage of the fleshy tree
     */
    protected abstract Ground getNextStage();
}
