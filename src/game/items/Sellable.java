package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A contract for items that can be sold to the Supercomputer terminal.
 * Implementing classes define their own sell price and sell effect,
 * which triggers immediately at the point of the transaction.
 *
 * @author Raima
 * @version 2.0
 */
public interface Sellable {

    /**
     * Returns whether this item can currently be sold.
     * By-default all items can be sold unless overridden.
     *
     * @return true if the item can be sold
     */
    default boolean canSell() {
        return true;
    }

    /**
     * Sells this item and applies its effect to the seller.
     * The effect triggers immediately at the point of the transaction.
     *
     * @param actor the actor selling this item
     * @param wallet the wallet to add credits to after the sale
     * @param location the current location of the actor selling this item
     * @return a message describing the result of the sale and any side effects
     */
    String sell(Actor actor, Wallet wallet, Location location);

    /**
     * Removes this item from the actor's inventory after a successful sale.
     * Called by SellAction only after a successful sale.
     *
     * @param actor the actor whose inventory to remove this item from
     */
    void removeAfterSell(Actor actor);

    /**
     * Returns the sell price of this item in credits.
     *
     * @return the sell price in credits
     */
    int getSellPrice();

    /**
     * Returns the menu text fragment describing this item for the sell action.
     *
     * @return a short descriptive string for the sell menu option
     */
    String getSellDescription();
}