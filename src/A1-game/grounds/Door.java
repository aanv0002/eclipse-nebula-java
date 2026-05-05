package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;

/**
 * Its primary purpose in the universe is to halt the progress of underpaid
 * {@code ContractedWorker}s until they can produce the correct rectangular
 * piece of plastic.
 */
public class Door extends Ground implements Unlockable {

    /**
     * Whether the door has been unlocked.
     */
    private boolean unlocked;

    /**
     * Creates a locked door.
     */
    public Door() {
        super('=', "Door");
        this.unlocked = false;
    }

    /**
     * Unlocks the door.
     */
    @Override
    public void unlock() {
        this.unlocked = true;
    }

    /**
     * Returns whether the door is unlocked.
     *
     * @return true if unlocked
     */
    @Override
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * Allows entry only if the door has been unlocked.
     *
     * @param actor the actor trying to enter
     * @return true if the actor may enter
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return unlocked;
    }
}
