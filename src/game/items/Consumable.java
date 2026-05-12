package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * A contract for objects that can be consumed.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
public interface Consumable {

    boolean canConsume();

    String consume(Actor actor);

    String getConsumeDescription();

    boolean isConsumedUp();

    /**
     * Removes the consumable if it has been fully consumed.
     *
     * @param actor the actor consuming the object
     * @param map the current map
     */
    void removeIfConsumedUp(Actor actor, GameMap map);
}