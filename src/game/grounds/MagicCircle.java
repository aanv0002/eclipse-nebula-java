package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TeleportAction;
import game.teleport.SpawnFlaskEffect;
import game.teleport.Teleportable;
import game.teleport.TeleportDestination;
import game.teleport.TeleportEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * A magic circle that randomly teleports workers to another magic circle on the
 * same map.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class MagicCircle extends Ground implements Teleportable {

    /**
     * All known magic circle destinations on the map.
     */
    private List<TeleportDestination> circleDestinations;

    /**
     * Creates a magic circle.
     */
    public MagicCircle() {
        super('◎', "Magic Circle");
        this.circleDestinations = new ArrayList<>();
    }

    /**
     * Sets all magic circle destinations on the map.
     *
     * @param destinations all magic circle destinations
     */
    @Override
    public void setDestinations(List<TeleportDestination> destinations) {
        this.circleDestinations = destinations;
    }

    /**
     * Returns all other magic circles except the one the actor is currently
     * standing on.
     *
     * @param actor the actor using the magic circle
     * @param map the current map
     * @return available destination circles
     */
    @Override
    public List<TeleportDestination> getDestinations(Actor actor, GameMap map) {
        Location current = map.locationOf(actor);
        List<TeleportDestination> available = new ArrayList<>();

        for (TeleportDestination destination : circleDestinations) {
            Location target = destination.getLocation();

            if (target.x() != current.x() || target.y() != current.y()) {
                available.add(destination);
            }
        }

        return available;
    }

    /**
     * Returns the flask-spawning effect applied after arrival.
     *
     * @return spawn flask effect
     */
    @Override
    public TeleportEffect getEffect() {
        return new SpawnFlaskEffect();
    }

    /**
     * Returns the teleport device name.
     *
     * @return teleport device name
     */
    @Override
    public String getTeleportName() {
        return "the magic circle";
    }

    /**
     * Allows the actor to use the magic circle when standing directly on it.
     *
     * @param actor the actor acting
     * @param location the current location
     * @param direction the direction of this ground from the actor
     * @return action list containing the teleport action when standing on circle
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();

        if (direction.equals("")) {
            actions.add(new TeleportAction(this, null, true, false));
        }

        return actions;
    }
}