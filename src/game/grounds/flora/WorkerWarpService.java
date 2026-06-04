package game.grounds.flora;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Amra Anver
 * @version 1.0
 */

public class WorkerWarpService {

    protected final Random random = new Random();
    /**
     * Warps a nearby worker to a random location
     * @param treeLocation tree location
     * @return true if worker is warped
     */
    protected boolean warpNearbyWorker(Location treeLocation) {
        for (Exit exit : treeLocation.getExits()) {
            Location adjacent = exit.getDestination();

            if (adjacent.containsAnActor()
                    && adjacent.getActor().hasAbility(ActorCapabilities.WORKER)) {

                Actor worker = adjacent.getActor();
                Location randomLocation = getRandomEmptyLocation(treeLocation.map(), worker);

                if (randomLocation != null) {
                    treeLocation.map().moveActor(worker, randomLocation);
                    new Display().println(this + " warps " + worker + " to " + randomLocation + ".");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finds a random empty location
     * @param map current map
     * @param actor actor to move
     * @return random valid location
     */
    private Location getRandomEmptyLocation(GameMap map, Actor actor) {
        List<Location> validLocations = new ArrayList<>();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location location = map.at(x, y);

                if (!location.containsAnActor()
                        && location.canActorEnter(actor)) {
                    validLocations.add(location);
                }
            }
        }

        if (validLocations.isEmpty()) {
            return null;
        }

        return validLocations.get(random.nextInt(validLocations.size()));
    }
}