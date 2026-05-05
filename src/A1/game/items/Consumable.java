package game.items;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * A contract for items that can be consumed by an actor.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public interface Consumable {

    /**
     * Returns whether this consumable can currently be used.
     *
     * @return true if it can be consumed, false otherwise
     */
    boolean canConsume();

    /**
     * Consumes the item and applies its effect to the actor.
     *
     * @param actor the actor consuming the item
     * @return the result message
     */
    String consume(Actor actor);

    /**
     * Returns the menu text fragment for the consume action.
     *
     * @return menu text fragment
     */
    String getConsumeDescription();

    /**
     * Returns whether the consumable has been fully used up and should be removed.
     *
     * @return true if the item should be removed after consumption
     */
    boolean isConsumedUp();
}