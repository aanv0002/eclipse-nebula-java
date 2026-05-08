package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.doors.*;

/**
 * A level 1 aluminium door.
 *
 * This door can be unlocked by an access card with level 1 or higher clearance.
 * Unlocking it applies a shock effect to the actor.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class AluminiumDoor extends Ground implements Unlockable {

    /**
     * Whether this door has been unlocked.
     */
    private boolean unlocked;

    /**
     * Effect applied when this door is unlocked.
     */
    private final DoorEffect effect;

    /**
     * Creates a locked aluminium door.
     */
    public AluminiumDoor() {
        super('=', "Aluminium Door");
        this.unlocked = false;
        this.effect = new ShockDoorEffect();
    }

    /**
     * Unlocks this door.
     */
    @Override
    public void unlock() {
        unlocked = true;
    }

    /**
     * Returns whether this door is unlocked.
     *
     * @return true if unlocked
     */
    @Override
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * Returns the clearance level required to unlock this door.
     *
     * @return level 1 clearance
     */
    @Override
    public ClearanceLevel getRequiredLevel() {
        return ClearanceLevel.LEVEL_1;
    }

    /**
     * Applies this door's unlock effect.
     *
     * @param actor the actor unlocking the door
     * @param doorLocation the location of this door
     */
    @Override
    public void applyEffect(Actor actor, Location doorLocation) {
        effect.apply(actor, doorLocation);
    }

    /**
     * Allows actors to enter only after the door is unlocked.
     *
     * @param actor the actor attempting to enter
     * @return true if the door is unlocked
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return unlocked;
    }
}