package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.teleport.Teleportable;
import game.teleport.TeleportDestination;
import game.teleport.TeleportService;

import java.util.List;

/**
 * Action for using a teleport device.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class TeleportAction extends Action {

    private final Teleportable teleportable;
    private final TeleportDestination selectedDestination;
    private final boolean chooseRandomDestination;
    private final boolean allowMalfunction;
    private final TeleportService service;

    /**
     * Creates a teleport action.
     *
     * @param teleportable teleport device
     * @param selectedDestination selected destination, or null for random
     * @param chooseRandomDestination true if destination should be chosen randomly
     * @param allowMalfunction true if 50% malfunction applies
     */
    public TeleportAction(Teleportable teleportable,
                          TeleportDestination selectedDestination,
                          boolean chooseRandomDestination,
                          boolean allowMalfunction) {
        this.teleportable = teleportable;
        this.selectedDestination = selectedDestination;
        this.chooseRandomDestination = chooseRandomDestination;
        this.allowMalfunction = allowMalfunction;
        this.service = new TeleportService();
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        Location source = map.locationOf(actor);
        TeleportDestination destination = selectedDestination;

        if (chooseRandomDestination) {
            List<TeleportDestination> destinations = teleportable.getDestinations(actor, map);
            if (destinations.isEmpty()) {
                return actor + " cannot find a valid teleport destination.";
            }
            destination = destinations.get((int) (Math.random() * destinations.size()));
        }

        Location finalLocation = destination.getLocation();
        boolean malfunctioned = false;

        if (allowMalfunction && Math.random() < 0.5) { //0.5
            Location randomLocation = service.getRandomLocation(destination.getMap(), actor);
            if (randomLocation != null) {
                finalLocation = randomLocation;
                malfunctioned = true;
            }
        }

        if (!service.teleport(actor, finalLocation)) {
            return actor + " cannot teleport to " + destination.getName()
                    + " because the destination is blocked.";
        }

        teleportable.getEffect().apply(actor, source, finalLocation);

        if (malfunctioned) {
            return actor + " uses " + teleportable.getTeleportName()
                    + ", but it malfunctions and teleports them randomly within "
                    + destination.getMap() + ".";
        }

        return actor + " uses " + teleportable.getTeleportName()
                + " and teleports to " + destination.getName() + ".";
    }

    @Override
    public String menuDescription(Actor actor) {
        if (selectedDestination == null) {
            return actor + " uses " + teleportable.getTeleportName();
        }
        return actor + " uses " + teleportable.getTeleportName()
                + " to teleport to " + selectedDestination.getName();
    }
}