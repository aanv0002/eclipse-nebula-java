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
 * @author Tan Hong Sze
 * @version 1.0
 */
public class TeleportationTube extends Ground implements Teleportable {

    private final List<TeleportDestination> destinations;

    /**
     * Creates a teleportation tube.
     */
    public TeleportationTube() {
        super('Φ', "Teleportation Tube");
        this.destinations = new ArrayList<>();
    }

    /**
     * Adds a destination to this tube.
     *
     * @param destination destination to add
     */
    public void addDestination(TeleportDestination destination) {
        destinations.add(destination);
    }

    @Override
    public List<TeleportDestination> getDestinations(Actor actor, GameMap map) {
        return destinations;
    }

    @Override
    public TeleportEffect getEffect() {
        return new TubeBurnEffect();
    }

    @Override
    public String getTeleportName() {
        return "the teleportation tube";
    }

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