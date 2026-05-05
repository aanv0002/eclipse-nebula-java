package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.Consumable;

/**
 * A generic action for consuming a consumable item.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class ConsumeAction extends Action {

    /**
     * The consumable target.
     */
    private final Consumable consumable;

    /**
     * Creates a consume action.
     *
     * @param consumable the consumable to be used
     */
    public ConsumeAction(Consumable consumable) {
        this.consumable = consumable;
    }

    /**
     * Executes the consume action.
     *
     * @param actor the actor performing the action
     * @param map the map the actor is on
     * @return the result message
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = consumable.consume(actor);

        if (consumable instanceof Item item && consumable.isConsumedUp()) {
            if (actor.getInventory().getItems().contains(item)) {
                actor.getInventory().remove(item);
            } else {
                map.locationOf(actor).removeItem(item);
            }
        }

        return result;
    }

    /**
     * Returns the menu description.
     *
     * @param actor the actor performing the action
     * @return menu description
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " " + consumable.getConsumeDescription();
    }
}