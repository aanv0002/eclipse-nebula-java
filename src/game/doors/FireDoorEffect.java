package game.doors;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.BurningGround;
import game.grounds.Fire;

/**
 * Sets adjacent enterable tiles on fire when an iron door is unlocked.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
public class FireDoorEffect implements DoorEffect {

    /**
     * Applies the iron door effect by setting adjacent enterable tiles on fire.
     *
     * @param actor actor unlocking the door
     * @param doorLocation location of the door
     */
    @Override
    public void apply(Actor actor, Location doorLocation) {
        for (Exit exit : doorLocation.getExits()) {
            Location adjacent = exit.getDestination();

            if (adjacent.canActorEnter(actor)
                    && adjacent.getGroundAs(BurningGround.class) == null) {
                adjacent.setGround(new Fire(adjacent.getGround(), 2));
            }
        }
    }
}