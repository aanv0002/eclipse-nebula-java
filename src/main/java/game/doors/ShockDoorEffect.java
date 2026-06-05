package game.doors;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Door effect for aluminium doors.
 *
 * This effect shocks the actor for exactly 2 damage when the door is unlocked.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class ShockDoorEffect implements DoorEffect {

    /**
     * Applies the shock effect to the actor.
     *
     * @param actor the actor unlocking the door
     * @param doorLocation the location of the door
     */
    @Override
    public void apply(Actor actor, Location doorLocation) {
        actor.hurt(2);
        new Display().println(actor + " is shocked for 2 damage.");
    }
}