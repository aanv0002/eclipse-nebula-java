package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.WalletCarrier;
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
 * @version 2.0
 */
public class SellAction extends Action {

    /**
     * The sellable item to sell.
     */
    private final Sellable sellable;

    /**
     * Creates a sell action for the given sellable item.
     * The wallet is retrieved from the actor at execution time.
     *
     * @param sellable the item to sell
     */
    public SellAction(Sellable sellable) {
        this.sellable = sellable;
    }

    /**
     * Executes the sell action by retrieving the wallet from the actor,
     * applying the sell effect defined by the item and removing it from
     * the actor's inventory after selling.
     *
     * @param actor the actor performing the action
     * @param map the map the actor is on
     * @return the result message
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        WalletCarrier walletCarrier = actor.asCapability(WalletCarrier.class).orElse(null);
        if (walletCarrier == null) {
            return actor + " cannot sell items.";
        }

        Wallet wallet = walletCarrier.getWallet();

        if (!sellable.canSell()) {
            return actor + " cannot sell " + sellable.getSellDescription() + " right now.";
        }

        String result = sellable.sell(actor, wallet, map.locationOf(actor));
        sellable.removeAfterSell(actor);

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