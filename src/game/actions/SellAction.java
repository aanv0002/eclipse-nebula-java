package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ActorCapabilities;
import game.actors.ContractedWorker;
import game.items.Sellable;
import game.items.Wallet;

/**
 * A generic action for selling a sellable item to the Supercomputer terminal.
 * The wallet is retrieved from the actor at execution time, allowing any
 * sellable item to create a SellAction without needing direct access to
 * the wallet.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @author Shrabonti Majumder
 * @version 2.0
 */
public class SellAction extends Action {

    /**
     * The sellable item to sell.
     */
    private final Sellable sellable;

    /**
     * Concrete item backing the sellable capability.
     */
    private final Item item;

    /**
     * Creates a sell action for the given sellable item.
     *
     * @param item the concrete item to remove after selling
     * @param sellable the sellable capability to execute
     */
    public SellAction(Item item, Sellable sellable) {
        this.item = item;
        this.sellable = sellable;
    }

    /**
     * Executes the sell action.
     *
     * @param actor the actor performing the action
     * @param map the map the actor is on
     * @return the result message
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (!actor.hasAbility(ActorCapabilities.WORKER)) {
            return actor + " cannot sell items.";
        }

        ContractedWorker worker = (ContractedWorker) actor;
        Wallet wallet = worker.getWallet();

        String result = sellable.sell(actor, wallet, map.locationOf(actor));
        actor.getInventory().remove(item);

        return result;
    }

    /**
     * Returns the menu description showing the item name and sell price.
     *
     * @param actor the actor performing the action
     * @return menu description
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " sells " + sellable.getSellDescription()
                + " for " + sellable.getSellPrice() + " credits";
    }
}
