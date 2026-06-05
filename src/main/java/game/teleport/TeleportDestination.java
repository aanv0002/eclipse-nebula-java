package game.teleport;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Represents one teleport target location.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class TeleportDestination {

    private final GameMap map;
    private final Location location;
    private final String name;

    /**
     * Creates a teleport destination.
     *
     * @param map target map
     * @param location target location
     * @param name display name
     */
    public TeleportDestination(GameMap map, Location location, String name) {
        this.map = map;
        this.location = location;
        this.name = name;
    }

    public GameMap getMap() {
        return map;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}