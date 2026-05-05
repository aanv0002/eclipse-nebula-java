package game.grounds;

/**
 * A contract for grounds that can be unlocked.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public interface Unlockable {

    /**
     * Unlocks the object.
     */
    void unlock();

    /**
     * Returns whether the object is already unlocked.
     *
     * @return true if unlocked, false otherwise
     */
    boolean isUnlocked();
}