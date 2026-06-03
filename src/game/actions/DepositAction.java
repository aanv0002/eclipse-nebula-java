package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.Depositable;
import game.quota.QuotaSystem;

/**
 * A generic action for depositing a depositable item to the Supercomputer
 * terminal in exchange for company credits toward the quota.
 * Depends on the Depositable abstraction rather than concrete item types,
 * meaning new depositable items can be added without modifying this class (OCP, DIP).
 * Follows the same pattern as SellAction and BuyAction.
 *
 * @author Raima
 * @version 1.0
 */
public class DepositAction extends Action {

    /**
     * The depositable item to deposit.
     */
    private final Depositable depositable;

    /**
     * The concrete item backing the depositable capability.
     */
    private final Item item;

    /**
     * The quota system to add company credits to.
     */
    private final QuotaSystem quotaSystem;

    /**
     * Creates a deposit action for the given depositable item.
     *
     * @param item the concrete item to remove after depositing
     * @param depositable the depositable capability to execute
     * @param quotaSystem the quota system to add company credits to
     */
    public DepositAction(Item item, Depositable depositable, QuotaSystem quotaSystem) {
        this.item = item;
        this.depositable = depositable;
        this.quotaSystem = quotaSystem;
    }

    /**
     * Executes the deposit action by applying the deposit effect defined
     * by the item and removing it from the actor's inventory after depositing.
     *
     * @param actor the actor performing the action
     * @param map the map the actor is on
     * @return the result message of the deposit including any side effects
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = depositable.deposit(actor, quotaSystem, map.locationOf(actor));
        depositable.removeAfterDeposit(actor);
        return result;
    }

    /**
     * Returns the menu description for this action showing the item name
     * and company credit value.
     *
     * @param actor the actor performing the action
     * @return menu description including item name and company credit value
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " deposits " + depositable.getDepositDescription()
                + " for " + depositable.getDepositValue() + " company credits"
                + " (quota: " + quotaSystem.getCompanyCredits()
                + "/" + quotaSystem.getQuotaTarget() + ")";
    }
}