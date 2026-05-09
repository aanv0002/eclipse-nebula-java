package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A contract for items that can be sold to the Supercomputer terminal.
 * Implementing classes define their own sell price and sell effect,
 * which triggers immediately at the point of the transaction.
 *
 * @author Raima
 * @version 1.0
 */
public interface Sellable {

    /**
     * Sells the item and applies its effect to the seller.
     * The effect triggers immediately at the point of the transaction.
     *
     * @param actor the actor selling the item
     * @param wallet the wallet to add credits to
     * @param location the location of the actor selling the item
     * @return the result message
     */
    String sell(Actor actor, Wallet wallet, Location location);

    /**
     * Returns the sell price of this item.
     *
     * @return the sell price in credits
     */
    int getSellPrice();

    /**
     * Returns the menu text fragment describing this item for the sell action.
     *
     * @return menu text fragment
     */
    String getSellDescription();
}