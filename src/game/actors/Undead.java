package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.alarm.AlarmSystem;
import game.behaviours.AttackNearbyWorkerBehaviour;
import game.behaviours.FollowNearestWorkerBehaviour;
import game.inventory.BasicInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * A hostile creature that attacks nearby workers and permanently chases them
 * only if the alarm is triggered after it has already spawned.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class Undead extends Actor {

    /**
     * Running number for undead naming.
     */
    private static int counter = 1;

    /**
     * Shared alarm system for all undead.
     */
    private static AlarmSystem sharedAlarmSystem;

    /**
     * Random generator for wandering.
     */
    private final Random random = new Random();

    /**
     * Behaviours of the undead.
     */
    private final Map<Integer, Behaviour<Actor, Action>> behaviours = new TreeMap<>();

    /**
     * Whether this undead has permanently switched to chase mode.
     */
    private boolean alarmed;

    /**
     * The alarm activation count observed when this undead was created.
     */
    private final int knownActivationCount;

    /**
     * Sets the shared alarm system used by all undead.
     *
     * @param alarmSystem the shared alarm system
     */
    public static void setAlarmSystem(AlarmSystem alarmSystem) {
        sharedAlarmSystem = alarmSystem;
    }

    /**
     * Creates an undead.
     */
    public Undead() {
        super("#" + counter++ + " Undead", 'Ѫ', 15, new BasicInventory());
        this.enableAbility(ActorCapabilities.HOSTILE_TO_WORKER);
        this.setIntrinsicWeapon(new IntrinsicWeapon(1, "punches", 10, "bare fist") {});
        behaviours.put(1, new AttackNearbyWorkerBehaviour());
        behaviours.put(2, new FollowNearestWorkerBehaviour());

        this.alarmed = false;
        this.knownActivationCount = (sharedAlarmSystem == null) ? 0 : sharedAlarmSystem.getActivationCount();
    }

    /**
     * Chooses an action for the undead.
     *
     * Priority:
     * 1. Attack nearby worker
     * 2. Permanently chase nearest worker if alarm was triggered after this undead spawned
     * 3. Wander randomly
     *
     * @param actions collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn
     * @param map the map containing the Actor
     * @param display the I/O object
     * @return the chosen action
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        Location here = map.locationOf(this);

        Action attackAction = behaviours.get(1).operate(this, here);
        if (attackAction != null) {
            return attackAction;
        }

        if (!alarmed && sharedAlarmSystem != null
                && sharedAlarmSystem.getActivationCount() > knownActivationCount) {
            alarmed = true;
        }

        if (alarmed) {
            Action chaseAction = behaviours.get(2).operate(this, here);
            if (chaseAction != null) {
                return chaseAction;
            }
        }

        return getRandomMoveAction(here);
    }

    /**
     * Returns a random valid movement action.
     *
     * @param here current location
     * @return move action or do nothing
     */
    private Action getRandomMoveAction(Location here) {
        List<Action> validMoves = new ArrayList<>();

        for (Exit exit : here.getExits()) {
            Action moveAction = exit.getDestination().getMoveAction(this, exit.getName(), exit.getHotKey());
            if (moveAction != null) {
                validMoves.add(moveAction);
            }
        }

        if (validMoves.isEmpty()) {
            return new DoNothingAction();
        }

        return validMoves.get(random.nextInt(validMoves.size()));
    }
}