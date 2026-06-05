package game.grounds.flora;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

import java.util.Random;

/**
 * Abstract parent class for all warper trees
 * @author Amra Anver
 * @version 2.0
 */
public abstract class WarperTree extends Ground {
    /**
     * Random number generator used for growth and warping
     */
    protected final Random random = new Random();

    /**
     * Variable which counts turns for growth
     */
    private int turnCounter = 0;

    /**
     * Constructor
     * @param displayChar display character
     * @param name ground name
     */
    public WarperTree(char displayChar, String name) {
        super(displayChar, name);
    }

    /**
     * Handles warper tree behaviour every tick
     * @param location current location
     **/
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

                if (nextStage != null) {
                    location.setGround(nextStage);
                    new Display().println(this + " grows into " + nextStage + " at " + location + ".");
                }
            }
        }
    }

    /**
     * Prevents actors from stepping on trees
     * @param actor the actor attempting to enter
     * @return false always
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }

    /**
     * Performs special tree action which is unique to each stage of each tree type
     * @param location current location
     * @return true if performed
     */
    protected abstract boolean performSpecialAction(Location location);

    /**
     * Returns growth interval
     * @return growth interval
     */
    protected abstract int getGrowthInterval();

    /**
     * Returns growth chance
     * @return growth chance
     */
    protected abstract int getGrowthChance();

    /**
     * Returns next stage of the warper tree
     * @return next stage ground
     */
    protected abstract Ground getNextStage();
}