package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ActorCapabilities;
import game.actors.ContractedWorker;
import game.items.Purchasable;
import game.items.Wallet;

/**
 * A generic action for purchasing an item from the Supercomputer terminal.
 * The wallet is retrieved from the actor at execution time, allowing any
 * purchasable item to create a BuyAction without needing direct access to
 * the wallet.
 *
 * @author Raima
 * @version 2.0
 * @see Purchasable
 */
public class BuyAction extends Action {

    /**
     * The purchasable item to buy.
     */
    private final Purchasable purchasable;

    /**
     * Creates a buy action for the given purchasable item.
     * The wallet is retrieved from the actor at execution time.
     *
     * @param purchasable the item to buy
     */
    public BuyAction(Purchasable purchasable) {
        this.purchasable = purchasable;
    }

    /**
     * Executes the buy action by retrieving the wallet from the actor
     * and applying the buy effect defined by the item.
     *
     * @param actor the actor performing the action
     * @param map the map the actor is on
     * @return the result message
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (!actor.hasAbility(ActorCapabilities.WORKER)) {
            return actor + " cannot buy items.";
        }

        ContractedWorker worker = (ContractedWorker) actor;
        Wallet wallet = worker.getWallet();

        return purchasable.buy(actor, wallet, map.locationOf(actor));
    }

    /**
     * Returns the menu description showing the item name, buy price
     * and the worker's current wallet balance.
     *
     * @param actor the actor performing the action
     * @return menu description
     */
    @Override
    public String menuDescription(Actor actor) {
        if (actor.hasAbility(ActorCapabilities.WORKER)) {
            ContractedWorker worker = (ContractedWorker) actor;
            return actor + " buys " + purchasable.getBuyDescription()
                    + " for " + purchasable.getBuyPrice() + " credits"
                    + " (wallet: " + worker.getWallet().getCredits() + " credits)";
        }
        return actor + " buys " + purchasable.getBuyDescription();
    }
}