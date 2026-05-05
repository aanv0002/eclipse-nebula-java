package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.alarm.AlarmSystem;
import game.grounds.Unlockable;

/**
 * An action that attempts to unlock an unlockable ground.
 *
 * During alarm lockdown, unlocking is temporarily prevented.
 *
 * This class is the single enforcement point for alarm-based door locking,
 * so the AccessCard does not need to duplicate the same check.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class UnlockDoorAction extends Action {

    /**
     * Shared alarm system used by all unlock door actions.
     */
    private static AlarmSystem sharedAlarmSystem;

    /**
     * The unlockable target.
     */
    private final Unlockable unlockable;

    /**
     * Sets the shared alarm system used by all UnlockDoorAction instances.
     *
     * This should be called once during world setup.
     *
     * @param alarmSystem the shared alarm system
     */
    public static void setAlarmSystem(AlarmSystem alarmSystem) {
        sharedAlarmSystem = alarmSystem;
    }

    /**
     * Creates an unlock action.
     *
     * @param unlockable the target to unlock
     */
    public UnlockDoorAction(Unlockable unlockable) {
        this.unlockable = unlockable;
    }

    /**
     * Executes the unlock attempt.
     *
     * If the facility alarm lockdown is active, the unlock attempt is blocked.
     * Otherwise, the unlockable object is unlocked normally.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return the result of the unlock attempt
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (sharedAlarmSystem != null && sharedAlarmSystem.isLockdownActive()) {
            return "The facility alarm prevents unlocking doors for another "
                    + sharedAlarmSystem.getRemainingTurns() + " turn(s).";
        }

        unlockable.unlock();
        return actor + " unlocks the door.";
    }

    /**
     * Returns the menu description for this action.
     *
     * @param actor The actor performing the action.
     * @return the menu description
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " unlocks the door";
    }
}