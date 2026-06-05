package game.doors;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Effect applied when a door is unlocked.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public interface DoorEffect {

    /**
     * Applies the door effect.
     *
     * @param actor actor unlocking the door
     * @param doorLocation location of the door
     */
    void apply(Actor actor, Location doorLocation);
}