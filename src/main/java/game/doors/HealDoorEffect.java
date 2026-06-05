package game.doors;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Heals the actor when a titanium door is unlocked.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class HealDoorEffect implements DoorEffect {

    @Override
    public void apply(Actor actor, Location doorLocation) {
        actor.heal(5);
        new Display().println(actor + " is healed by the decontamination sequence.");
    }
}