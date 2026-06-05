package game.states;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;

/**
 * Helper class that provides reusable environmental checks for state transitions.
 *
 * This keeps transition logic clean and avoids duplicating nearby-worker and
 * nearby-item checks across multiple states.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class StateContext {

    /**
     * Counts workers within a given radius.
     *
     * @param location starting location
     * @param radius search radius
     * @return number of nearby workers
     */
    public int countWorkersWithin(Location location, int radius) {
        int count = 0;

        for (Location nearby : location.getNearbyLocations(radius)) {
            if (nearby.containsAnActor()
                    && nearby.getActor().hasAbility(ActorCapabilities.WORKER)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Counts workers directly adjacent to the creature.
     *
     * @param location current location
     * @return number of adjacent workers
     */
    public int countAdjacentWorkers(Location location) {
        int count = 0;

        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            if (destination.containsAnActor()
                    && destination.getActor().hasAbility(ActorCapabilities.WORKER)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Returns whether there is at least one item within a radius.
     *
     * @param location starting location
     * @param radius search radius
     * @return true if an item is nearby
     */
    public boolean hasItemWithin(Location location, int radius) {
        for (Location nearby : location.getNearbyLocations(radius)) {
            if (!nearby.getItems().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether the location contains ground items.
     *
     * @param location location to check
     * @return true if there are items on the ground
     */
    public boolean hasItemAt(Location location) {
        return !location.getItems().isEmpty();
    }

    /**
     * Returns the first item on the ground, or null if none exists.
     *
     * @param location location to check
     * @return first item, or null
     */
    public Item firstItemAt(Location location) {
        if (location.getItems().isEmpty()) {
            return null;
        }

        return location.getItems().get(0);
    }
}