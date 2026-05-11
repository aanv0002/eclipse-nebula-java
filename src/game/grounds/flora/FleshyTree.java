package game.grounds.flora;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstract parent class for all fleshy trees
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

    /**
     * Constructor
     * @param displayChar display character
     * @param name ground name
     */
    public FleshyTree(char displayChar, String name) {
        super(displayChar, name);
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
     * Checks whether at least one worker is adjacent
     * @param location tree location
     * @return true if worker is nearby
     */
    protected boolean isWorkerNearby(Location location) {
        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            if(destination.containsAnActor() && destination.getActor().hasAbility(ActorCapabilities.WORKER)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Spawns an actor in a nearby valid location
     * @param location current location
     * @param actor the actor that gets spawned
     * @return true if successfully spawned
     */
    protected boolean spawnActorNearby(Location location, Actor actor) {
        List<Location> validLocations = new ArrayList<>();

        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            if (!destination.containsAnActor() && destination.canActorEnter(actor)) {
                validLocations.add(destination);
            }
        }
        if (validLocations.isEmpty()) {
            return false;
        }
        Location spawnLocation = validLocations.get(random.nextInt(validLocations.size()));
        try {
            spawnLocation.addActor(actor);
            new Display().println(actor + " is spawned by " + this + " at " + spawnLocation + ".");
            return true;
        } catch (GameEngineException ignored) {
            return false;
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
