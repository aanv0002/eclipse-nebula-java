package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.quota.QuotaSystem;

/**
 * A contract for items that can be deposited to the Supercomputer terminal
 * in exchange for company credits that contribute toward the quota.
 * Each depositable item defines its own deposit effect and company credit value,
 * following the same pattern as the Sellable interface.
 *
 * @author Raima
 * @version 1.0
 */
public interface Depositable {

    /**
     * Deposits this item and applies its effect to the depositor.
     * The effect triggers immediately at the point of the transaction.
     * Company credits are added to the QuotaSystem after deposit.
     *
     * @param actor the actor depositing this item
     * @param quotaSystem the quota system to add company credits to
     * @param location the current location of the actor
     * @return a message describing the result of the deposit and any side effects
     */
    String deposit(Actor actor, QuotaSystem quotaSystem, Location location);

    /**
     * Returns the company credit value of this item when deposited.
     *
     * @return the company credit value
     */
    int getDepositValue();

    /**
     * Returns the menu text fragment describing this item for the deposit action.
     *
     * @return a short descriptive string for the deposit menu option
     */
    String getDepositDescription();

    /**
     * Removes this item from the actor's inventory after a successful deposit.
     *
     * @param actor the actor whose inventory to remove this item from
     */
    void removeAfterDeposit(Actor actor);
}