package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TeleportAction;
import game.teleport.Teleportable;
import game.teleport.TeleportDestination;
import game.teleport.TeleportEffect;
import game.teleport.TubeBurnEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * A teleportation tube that supports predetermined teleport destinations.
 *
 * Teleportation tubes may teleport within the same map or between maps. Each
 * destination is manually added during world setup.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class TeleportationTube extends Ground implements Teleportable {

    /**
     * Predetermined destinations available from this tube.
     */
    private final List<TeleportDestination> destinations;

    /**
     * Creates a teleportation tube.
     */
    public TeleportationTube() {
        super('Φ', "Teleportation Tube");
        this.destinations = new ArrayList<>();
    }

    /**
     * Adds a predetermined destination to this tube.
     *
     * @param destination destination to add
     */
    @Override
    public void addDestination(TeleportDestination destination) {
        destinations.add(destination);
    }

    /**
     * Returns all predetermined tube destinations.
     *
     * @param actor the actor using the tube
     * @param map the current map
     * @return list of tube destinations
     */
    @Override
    public List<TeleportDestination> getDestinations(Actor actor, GameMap map) {
        return destinations;
    }

    /**
     * Returns the destination-burning effect applied after teleporting.
     *
     * @return tube burn effect
     */
    @Override
    public TeleportEffect getEffect() {
        return new TubeBurnEffect();
    }

    /**
     * Returns the teleport device name.
     *
     * @return teleport device name
     */
    @Override
    public String getTeleportName() {
        return "the teleportation tube";
    }

    /**
     * Allows the actor to use the tube when standing directly on it.
     *
     * @param actor the actor acting
     * @param location the current location
     * @param direction the direction of this ground from the actor
     * @return action list containing one action per destination
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();

        if (direction.equals("")) {
            for (TeleportDestination destination : destinations) {
                actions.add(new TeleportAction(this, destination, false, true));
            }
        }

        return actions;
    }
}