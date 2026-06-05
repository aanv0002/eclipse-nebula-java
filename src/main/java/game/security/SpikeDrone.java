package game.security;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;
import game.grounds.AcidPool;
import game.inventory.BasicInventory;
import game.statuses.CorrodedStatus;
import java.util.ArrayList;

/**
 * Stationary security mine triggering grid displacement and applying CorrodedStatus.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class SpikeDrone extends Actor implements SecurityHazard {

    private boolean active;

    public SpikeDrone() {
        super("Spike Drone", 'D', 10, new BasicInventory());
        this.active = true;
    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        if (!active) return new DoNothingAction();

        Location here = map.locationOf(this);
        boolean workerAdjacent = false;
        Actor targetWorker = null;

        for (Exit exit : here.getExits()) {
            Location adj = exit.getDestination();
            if (adj.containsAnActor() && adj.getActor().hasAbility(ActorCapabilities.WORKER)) {
                workerAdjacent = true;
                targetWorker = adj.getActor();
                break;
            }
        }

        if (workerAdjacent && targetWorker != null) {
            display.println("Spike Drone detects an unauthorized worker and explosively detonates!");
            targetWorker.addStatus(new CorrodedStatus(10));

            for (Item item : new ArrayList<>(here.getItems())) {
                here.removeItem(item);
            }

            here.setGround(new AcidPool());
            map.removeActor(this);
        }

        return new DoNothingAction();
    }

    @Override
    public void disarm(Location location) {
        this.active = false;
        location.map().removeActor(this);
        new Display().println("Spike Drone safely defused and bypassed.");
    }

    @Override
    public boolean isActive() {
        return active;
    }
}