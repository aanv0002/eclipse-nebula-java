package game.teleport;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

import java.util.List;

/**
 * Represents any object that can provide teleportation actions.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
public interface Teleportable {

    List<TeleportDestination> getDestinations(Actor actor, GameMap map);

    TeleportEffect getEffect();

    String getTeleportName();

    /**
     * Adds a teleport destination to this teleportable object.
     * Teleportables that do not store destinations may ignore this.
     *
     * @param destination destination to add
     */
    default void addDestination(TeleportDestination destination) {
    }

    /**
     * Replaces all teleport destinations for this teleportable object.
     * Teleportables that do not use shared destination lists may ignore this.
     *
     * @param destinations destinations to set
     */
    default void setDestinations(List<TeleportDestination> destinations) {
    }
}