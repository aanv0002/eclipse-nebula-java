package game.doors;

/**
 * Clearance levels used by access cards and doors.
 *
 * A higher clearance level can open doors with lower clearance requirements.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public enum ClearanceLevel {
    LEVEL_1,
    LEVEL_2,
    LEVEL_3;

    /**
     * Returns whether this clearance level can open a door requiring the given level.
     *
     * @param requiredLevel the required clearance level
     * @return true if this level is equal to or higher than the required level
     */
    public boolean canOpen(ClearanceLevel requiredLevel) {
        return this.ordinal() >= requiredLevel.ordinal();
    }
}