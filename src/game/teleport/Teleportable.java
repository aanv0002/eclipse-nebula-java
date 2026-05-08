package game.teleport;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

import java.util.List;

/**
 * Represents any object that can provide teleportation actions.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public interface Teleportable {

    /**
     * Returns available teleport destinations.
     *
     * @param actor the actor using the teleport device
     * @param map the current map
     * @return list of possible destinations
     */
    List<TeleportDestination> getDestinations(Actor actor, GameMap map);

    /**
     * Returns the effect applied after teleporting.
     *
     * @return teleport effect
     */
    TeleportEffect getEffect();

    /**
     * Returns the name of this teleport device.
     *
     * @return device name
     */
    String getTeleportName();
}