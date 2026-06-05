package game.teleport;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles teleport movement and random-location selection.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class TeleportService {

    private final Random random = new Random();

    /**
     * Moves the actor to the destination location if possible.
     *
     * @param actor actor to move
     * @param destination destination location
     * @return true if teleport succeeds
     */
    public boolean teleport(Actor actor, Location destination) {
        if (destination.containsAnActor() || !destination.canActorEnter(actor)) {
            return false;
        }

        destination.map().moveActor(actor, destination);
        return true;
    }

    /**
     * Returns random enterable locations from a map.
     *
     * @param map map to search
     * @param actor actor being moved
     * @param count number of locations needed
     * @return list of random locations
     */
    public List<Location> getRandomLocations(GameMap map, Actor actor, int count) {
        List<Location> validLocations = new ArrayList<>();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location location = map.at(x, y);
                if (!location.containsAnActor() && location.canActorEnter(actor)) {
                    validLocations.add(location);
                }
            }
        }

        List<Location> selected = new ArrayList<>();

        while (!validLocations.isEmpty() && selected.size() < count) {
            selected.add(validLocations.remove(random.nextInt(validLocations.size())));
        }

        return selected;
    }

    /**
     * Returns one random enterable location.
     *
     * @param map map to search
     * @param actor actor being moved
     * @return random valid location
     */
    public Location getRandomLocation(GameMap map, Actor actor) {
        List<Location> locations = getRandomLocations(map, actor, 1);
        return locations.isEmpty() ? null : locations.get(0);
    }
}