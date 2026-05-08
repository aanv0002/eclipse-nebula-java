package game.doors;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Shocks the actor when unlocking an aluminium door.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class ShockDoorEffect implements DoorEffect {

    @Override
    public void apply(Actor actor, Location doorLocation) {
        actor.hurt(2);
        new Display().println(actor + " is shocked for 2 damage.");
    }
}