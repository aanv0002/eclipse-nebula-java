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
import game.infection.Infectable;
import game.infection.UndeadInfectionEffect;
import game.inventory.BasicInventory;
import game.statuses.InfectedStatus;

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
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class Undead extends Actor implements Infectable {

    private static int counter = 1;
    private static AlarmSystem sharedAlarmSystem;

    private final Random random = new Random();
    private final Map<Integer, Behaviour<Actor, Action>> behaviours = new TreeMap<>();
    private boolean alarmed;
    private final int knownActivationCount;

    public static void setAlarmSystem(AlarmSystem alarmSystem) {
        sharedAlarmSystem = alarmSystem;
    }

    public Undead() {
        super("#" + counter++ + " Undead", 'u', 15, new BasicInventory());
        this.enableAbility(ActorCapabilities.HOSTILE_TO_WORKER);
        this.enableAbility(ActorCapabilities.CREATURE);
        this.enableAbility(ActorCapabilities.UNDEAD);
        this.setIntrinsicWeapon(new IntrinsicWeapon(1, "punches", 10, "bare fist") {});
        behaviours.put(1, new AttackNearbyWorkerBehaviour());
        behaviours.put(2, new FollowNearestWorkerBehaviour());

        this.alarmed = false;
        this.knownActivationCount = (sharedAlarmSystem == null) ? 0 : sharedAlarmSystem.getActivationCount();
    }

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

    @Override
    public boolean canBeInfected() {
        return this.isConscious();
    }

    @Override
    public String infect(Actor parasite, Location location) {
        InfectedStatus infectedStatus = new InfectedStatus(new UndeadInfectionEffect(this));
        this.addStatus(infectedStatus);
        return infectedStatus.onInfected(location);
    }
}
