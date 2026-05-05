package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ConsumeAction;
import game.items.Consumable;

/**
 * A behaviour that allows a slime to consume a consumable item directly from the ground.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class ConsumeGroundItemBehaviour implements Behaviour<Actor, Action> {

    /**
     * Returns a consume action for the first consumable item on the ground.
     *
     * @param actor the actor performing the behaviour
     * @param location the current location of the actor
     * @return a consume action, or null if no consumable item is on the ground
     */
    @Override
    public Action operate(Actor actor, Location location) {
        for (Consumable consumable : location.getItemsAs(Consumable.class)) {
            if (consumable.canConsume()) {
                return new ConsumeAction(consumable);
            }
        }
        return null;
    }
}