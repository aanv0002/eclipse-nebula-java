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
 * A magic circle that randomly teleports workers to another magic circle.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class MagicCircle extends Ground implements Teleportable {

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
     * @param circleDestinations destinations
     */
    public void setCircleDestinations(List<TeleportDestination> circleDestinations) {
        this.circleDestinations = circleDestinations;
    }

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

    @Override
    public TeleportEffect getEffect() {
        return new SpawnFlaskEffect();
    }

    @Override
    public String getTeleportName() {
        return "the magic circle";
    }

    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();

        if (direction.equals("")) {
            actions.add(new TeleportAction(this, null, true, false));
        }

        return actions;
    }
}