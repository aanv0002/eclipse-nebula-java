package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.doors.*;


/**
 * A level 3 titanium door that heals the worker when unlocked.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class TitaniumDoor extends Ground implements Unlockable {

    private boolean unlocked;
    private final DoorEffect effect;

    public TitaniumDoor() {
        super('M', "Titanium Door");
        this.unlocked = false;
        this.effect = new HealDoorEffect();
    }

    @Override
    public void unlock() {
        unlocked = true;
    }

    @Override
    public boolean isUnlocked() {
        return unlocked;
    }

    @Override
    public ClearanceLevel getRequiredLevel() {
        return ClearanceLevel.LEVEL_3;
    }

    @Override
    public void applyEffect(Actor actor, Location doorLocation) {
        effect.apply(actor, doorLocation);
    }

    @Override
    public boolean canActorEnter(Actor actor) {
        return unlocked;
    }
}