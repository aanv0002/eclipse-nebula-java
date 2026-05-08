package game.doors;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A contract for grounds that can be unlocked.
 *
 * Unlockable grounds define their locked state, required clearance level, and
 * any effect that should occur when they are successfully unlocked.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public interface Unlockable {

    /**
     * Unlocks the ground.
     */
    void unlock();

    /**
     * Returns whether the ground has already been unlocked.
     *
     * @return true if unlocked
     */
    boolean isUnlocked();

    /**
     * Returns the clearance level required to unlock this ground.
     *
     * @return required clearance level
     */
    ClearanceLevel getRequiredLevel();

    /**
     * Applies the effect that occurs after this ground is unlocked.
     *
     * @param actor the actor unlocking the ground
     * @param doorLocation the location of the unlockable ground
     */
    void applyEffect(Actor actor, Location doorLocation);
}