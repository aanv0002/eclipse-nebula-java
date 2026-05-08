package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.doors.*;

/**
 * A level 3 titanium door.
 *
 * This door can only be unlocked by an access card with level 3 clearance.
 * Unlocking it applies a healing decontamination effect to the actor.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class TitaniumDoor extends Ground implements Unlockable {

    /**
     * Whether this door has been unlocked.
     */
    private boolean unlocked;

    /**
     * Effect applied when this door is unlocked.
     */
    private final DoorEffect effect;

    /**
     * Creates a locked titanium door.
     */
    public TitaniumDoor() {
        super('M', "Titanium Door");
        this.unlocked = false;
        this.effect = new HealDoorEffect();
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
     * @return level 3 clearance
     */
    @Override
    public ClearanceLevel getRequiredLevel() {
        return ClearanceLevel.LEVEL_3;
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