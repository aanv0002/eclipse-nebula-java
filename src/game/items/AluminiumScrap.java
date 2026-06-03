package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.quota.QuotaSystem;

import java.util.Random;

/**
 * A jagged piece of aluminium scrap dropped when an AluminiumDoor is cut
 * with a PlasmaCutter. Can only be deposited to the Supercomputer for
 * company credits — it cannot be sold back to the company.
 * Depositing carries a 20% chance of the worker being cut and taking 5 damage.
 *
 * @author Raima
 * @version 1.0
 */
public class AluminiumScrap extends Item implements Depositable {

    /**
     * Weight of the aluminium scrap.
     */
    public static final int WEIGHT = 2;

    /**
     * Company credit value when deposited.
     */
    public static final int DEPOSIT_VALUE = 50;

    /**
     * Damage dealt to worker if cut during deposit.
     */
    public static final int CUT_DAMAGE = 5;

    /**
     * Chance of worker being cut during deposit.
     */
    private static final double CUT_CHANCE = 0.20;

    /**
     * Random number generator for cut chance.
     */
    private final Random random = new Random();

    /**
     * Creates an aluminium scrap.
     */
    public AluminiumScrap() {
        super("Aluminium Scrap", '%');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Deposits the aluminium scrap to the Supercomputer.
     * Adds 50 company credits to the quota system.
     * There is a 20% chance the worker is cut and takes 5 damage.
     *
     * @param actor the actor depositing the scrap
     * @param quotaSystem the quota system to add company credits to
     * @param location the current location of the actor
     * @return the result message of the deposit including any side effects
     */
    @Override
    public String deposit(Actor actor, QuotaSystem quotaSystem, Location location) {
        quotaSystem.addCompanyCredits(DEPOSIT_VALUE);
        String result = actor + " deposits the Aluminium Scrap for "
                + DEPOSIT_VALUE + " company credits.";

        if (random.nextDouble() < CUT_CHANCE) {
            actor.hurt(CUT_DAMAGE);
            result += " The jagged metal cuts the worker, dealing "
                    + CUT_DAMAGE + " damage!";
        }
        return result;
    }

    /**
     * Returns the company credit value of the aluminium scrap.
     *
     * @return deposit value in company credits
     */
    @Override
    public int getDepositValue() {
        return DEPOSIT_VALUE;
    }

    /**
     * Returns the menu text fragment for the deposit action.
     *
     * @return menu text fragment
     */
    @Override
    public String getDepositDescription() {
        return "Aluminium Scrap (50 company credits)";
    }

    /**
     * Removes the aluminium scrap from the actor's inventory after depositing.
     *
     * @param actor the actor whose inventory to remove this item from
     */
    @Override
    public void removeAfterDeposit(Actor actor) {
        actor.getInventory().remove(this);
    }
}