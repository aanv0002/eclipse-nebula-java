package game.alarm;

/**
 * Tracks the facility alarm state.
 *
 * Once triggered, the alarm remains triggered permanently for creature behaviour.
 * However, door lockdown only lasts for a limited number of turns.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class AlarmSystem {

    /**
     * Default number of turns doors remain locked after alarm activation.
     */
    public static final int DEFAULT_LOCKDOWN_TURNS = 5;

    /**
     * Whether the alarm has ever been triggered.
     */
    private boolean triggered;

    /**
     * Remaining turns for temporary door lockdown.
     */
    private int lockdownRemainingTurns;

    /**
     * Counts how many times the alarm has been activated.
     */
    private int activationCount;

    /**
     * Creates a new alarm system.
     */
    public AlarmSystem() {
        this.triggered = false;
        this.lockdownRemainingTurns = 0;
        this.activationCount = 0;
    }

    /**
     * Activates the alarm.
     *
     * This permanently marks the alarm as triggered and refreshes
     * the temporary door lockdown duration.
     */
    public void activate() {
        this.triggered = true;
        this.lockdownRemainingTurns = DEFAULT_LOCKDOWN_TURNS;
        this.activationCount++;
    }

    /**
     * Advances the alarm system by one turn.
     * Only the door lockdown timer decreases.
     */
    public void tick() {
        if (lockdownRemainingTurns > 0) {
            lockdownRemainingTurns--;
        }
    }

    /**
     * Returns whether the alarm has been triggered at least once.
     *
     * @return true if triggered
     */
    public boolean isTriggered() {
        return triggered;
    }

    /**
     * Returns whether door lockdown is still active.
     *
     * @return true if doors are still locked by alarm
     */
    public boolean isLockdownActive() {
        return lockdownRemainingTurns > 0;
    }

    /**
     * Returns remaining door lockdown turns.
     *
     * @return remaining lockdown turns
     */
    public int getRemainingTurns() {
        return lockdownRemainingTurns;
    }

    /**
     * Returns how many times the alarm has been activated.
     *
     * @return activation count
     */
    public int getActivationCount() {
        return activationCount;
    }
}