package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.ItemCapabilities;
import game.statuses.PoisonStatus;

/**
 * An action that allows an actor to drink directly from a puddle.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class DrinkPuddleAction extends Action {

    /**
     * Drinks from the puddle and applies the appropriate effect depending on
     * whether the actor is carrying a SterilisationBox.
     *
     * @param actor the actor performing the action
     * @param map the map the actor is on
     * @return the result message
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (actor.hasAbility(ItemCapabilities.STERILISE)) {
            actor.heal(1);
            return actor + " drinks purified puddle water and heals 1 hit point.";
        }

        actor.addStatus(new PoisonStatus(3));
        return actor + " drinks toxic puddle water and is poisoned.";
    }

    /**
     * Returns the menu description.
     *
     * @param actor the actor performing the action
     * @return menu description
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " drinks from the puddle";
    }
}