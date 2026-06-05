package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.actions.AttackAction;
import game.behaviours.RandomMoveBehaviour;
import game.infection.Infectable;
import game.inventory.BasicInventory;
import game.items.ItemCapabilities;
import game.statuses.InfectedStatus;
import game.infection.ScrapSnatcherInfectionEffect;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * A Scrap Snatcher is a creature that wanders the map and steals any
 * depositable resources it encounters.
 *
 * When infected by a Parasite, it becomes rabid, loses its hoarding
 * behaviour, attacks adjacent workers, and suffers infection effects
 * handled by ScrapSnatcherInfectionEffect class
 *
 * @author Amra Anver
 * @version 1.0
 */
public class ScrapSnatcher extends Actor implements Infectable {

    /**
     * Running number used for unique Scrap Snatcher names.
     */
    private static int counter = 1;

    /**
     * Tracks whether this Scrap Snatcher is infected.
     */
    private boolean infected;

    /**
     * Prioritised behaviours of the Parasite.
     */
    private final Map<Integer, Behaviour<Actor, Action>> behaviours = new TreeMap<>();

    public ScrapSnatcher() {
        super("#" + counter++ + " Scrap Snatcher", 's',25, new BasicInventory());
        this.enableAbility(ActorCapabilities.CREATURE);
        this.enableAbility(ActorCapabilities.SCRAP_SNATCHER);
        behaviours.put(999, new RandomMoveBehaviour());
        this.setIntrinsicWeapon(new IntrinsicWeapon(1, "punches", 10, "bare fist"){});
    }

    /**
     * Returns whether this Scrap Snatcher is infected.
     *
     * @return true if infected
     */
    public boolean isInfected() {
        return infected;
    }

    /**
     * Sets the infection state.
     *
     * @param infected true if infected
     */
    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    /**
     * Determines whether this Scrap Snatcher can be infected.
     *
     * @return true if not already infected
     */
    @Override
    public boolean canBeInfected() {
        return !infected;
    }

    /**
     * Applies infection to this Scrap Snatcher.
     *
     * @param parasite the infecting parasite
     * @param location the current location
     * @return infection result message
     */
    @Override
    public String infect(Actor parasite, Location location) {

        infected = true;

        InfectedStatus status = new InfectedStatus(new ScrapSnatcherInfectionEffect(this));

        this.addStatus(status);

        return status.onInfected(location);
    }

    /**
     * Executes this Scrap Snatcher's turn.
     * Normal state:
     * - steals depositable resources
     * - moves randomly
     * Infected state:
     * - attacks adjacent workers if possible
     * - otherwise moves randomly
     *
     * @param actions collection of possible actions
     * @param lastAction previous action
     * @param map current map
     * @param display display object
     * @return chosen action
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {

        Location here = map.locationOf(this);

        if (!infected) {
            snatchResources(here);
        }

        if (infected) {

            Action attack = getAttackWorkerAction(here);

            if (attack != null) {
                return attack;
            }
        }

        for (Behaviour<Actor, Action> behaviour : behaviours.values()) {

            Action action = behaviour.operate(this, here);

            if (action != null) {
                return action;
            }
        }

        return new DoNothingAction();
    }

    /**
     * Removes all depositable resources from the current tile.
     *
     * @param location current location
     */
    private void snatchResources(Location location) {

        for (Item item : new ArrayList<>(location.getItems())) {

            if (item.hasAbility(ItemCapabilities.DEPOSITABLE)) {
                location.removeItem(item);
            }
        }
    }

    /**
     * Finds an adjacent worker and returns an attack action.
     *
     * @param location current location
     * @return attack action if a worker is adjacent, otherwise null
     */
    private Action getAttackWorkerAction(Location location) {

        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();

            if (destination.containsAnActor()) {
                Actor target = destination.getActor();

                if (target.hasAbility(ActorCapabilities.WORKER)) {
                    return new AttackAction(target, exit.getName(), this.getIntrinsicWeapon());
                }
            }
        }

        return null;
    }
}

