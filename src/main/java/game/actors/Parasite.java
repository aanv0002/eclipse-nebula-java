package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.InfectNearbyTargetBehaviour;
import game.inventory.BasicInventory;

import java.util.Map;
import java.util.TreeMap;

/**
 * The Parasite is an invasive creature with 30 hit points.
 * If it has an adjacent valid infection target, it infects one target and dies.
 * Otherwise, it wanders randomly.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class Parasite extends Actor {

    /**
     * Running number for parasite naming.
     */
    private static int counter = 1;

    /**
     * Prioritised behaviours of the Parasite.
     */
    private final Map<Integer, Behaviour<Actor, Action>> behaviours = new TreeMap<>();

    /**
     * Creates a Parasite.
     */
    public Parasite() {
        super("#" + counter++ + " Parasite", 'x', 30, new BasicInventory());
        this.enableAbility(ActorCapabilities.CREATURE);
        this.enableAbility(ActorCapabilities.PARASITE);
        behaviours.put(1, new InfectNearbyTargetBehaviour());
    }

    /**
     * Chooses an action for the Parasite.
     * Infection is prioritised over wandering.
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction the Action this Actor took last turn
     * @param map        the map containing the Actor
     * @param display    the I/O object
     * @return the chosen action
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        Location here = map.locationOf(this);

        for (Behaviour<Actor, Action> behaviour : behaviours.values()) {
            Action action = behaviour.operate(this, here);
            if (action != null) {
                return action;
            }
        }

        return new DoNothingAction();
    }
}
