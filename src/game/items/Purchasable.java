package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A contract for items that can be purchased from the Supercomputer terminal.
 *  * Implementing classes define their own buy price and buy effect,
 *  * which triggers immediately at the point of the transaction.
 *
 * @author Raima
 * @version 1.0
 */
public interface Purchasable {

    /**
     * Purchases this item and applies its effect to the buyer.
     * The effect triggers immediately at the point of the transaction.
     *
     * @param actor the actor buying the item
     * @param wallet the wallet to deduct credits from
     * @param location the location of the actor buying the item
     * @return the result message
     */
    String buy(Actor actor, Wallet wallet, Location location);

    /**
     * Returns the buy price of this item.
     *
     * @return the buy price in credits
     */
    int getBuyPrice();

    /**
     * Returns the menu text fragment describing this item for the buy action.
     *
     * @return menu text fragment
     */
    String getBuyDescription();
}