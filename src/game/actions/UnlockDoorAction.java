package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.alarm.AlarmSystem;
import game.doors.Unlockable;

/**
 * An action that attempts to unlock an unlockable ground.
 *
 * During alarm lockdown, unlocking is temporarily prevented.
 * This class is the single enforcement point for alarm-based door locking,
 * so AccessCard does not need to duplicate the same check.
 *
 * @author Tan Hong Sze
 * @version 2.0
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
     * Location of the door being unlocked.
     */
    private final Location doorLocation;

    /**
     * Sets the shared alarm system used by all UnlockDoorAction instances.
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
     * @param doorLocation the location of the unlockable ground
     */
    public UnlockDoorAction(Unlockable unlockable, Location doorLocation) {
        this.unlockable = unlockable;
        this.doorLocation = doorLocation;
    }

    /**
     * Executes the unlock attempt.
     *
     * If the facility alarm lockdown is active, the unlock attempt is blocked.
     * Otherwise, the unlockable ground is unlocked and its door effect is applied.
     *
     * @param actor the actor performing the action
     * @param map the map the actor is on
     * @return the result of the unlock attempt
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (sharedAlarmSystem != null && sharedAlarmSystem.isLockdownActive()) {
            return "The facility alarm prevents unlocking doors for another "
                    + sharedAlarmSystem.getRemainingTurns() + " turn(s).";
        }

        unlockable.unlock();
        unlockable.applyEffect(actor, doorLocation);

        return actor + " unlocks the door.";
    }

    /**
     * Returns the menu description for this action.
     *
     * @param actor the actor performing the action
     * @return the menu description
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " unlocks the door";
    }
}