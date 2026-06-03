package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actors.Slime;
import game.quota.QuotaSystem;
import game.spawners.CreatureSpawnService;

import java.util.Random;

/**
 * An industrial fan dropped when a Vent is cut with a PlasmaCutter.
 * Can be sold to the Supercomputer for 150 worker credits or deposited
 * for 10 company credits.
 * Selling triggers a hazard — a Slime spawns on an empty adjacent tile.
 * Depositing rewards the worker with 10 HP of healing.
 *
 * @author Raima
 * @version 1.0
 */
public class IndustrialFan extends Item implements Sellable, Depositable {

    /**
     * Weight of the industrial fan.
     */
    public static final int WEIGHT = 5;

    /**
     * Sell price of the industrial fan in worker credits.
     */
    public static final int SELL_PRICE = 150;

    /**
     * Company credit value when deposited.
     */
    public static final int DEPOSIT_VALUE = 10;

    /**
     * HP healed when deposited.
     */
    public static final int HEAL_AMOUNT = 10;

    /**
     * Spawn service for spawning Slime on sell.
     */
    private final CreatureSpawnService spawnService;

    /**
     * Creates an industrial fan.
     */
    public IndustrialFan() {
        super("Industrial Fan", '@');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
        this.spawnService = new CreatureSpawnService();
    }

    /**
     * Sells the industrial fan to the Supercomputer for 150 worker credits.
     * Stripping the facility's cooling system triggers a hazard —
     * a Slime spawns on an empty tile adjacent to the worker.
     *
     * @param actor the actor selling the fan
     * @param wallet the wallet to add credits to
     * @param location the location of the actor
     * @return the result message of the sale and any side effects
     */
    @Override
    public String sell(Actor actor, Wallet wallet, Location location) {
        wallet.addCredits(SELL_PRICE);
        String result = actor + " sells the Industrial Fan for "
                + SELL_PRICE + " worker credits.";

        // ADDED: spawn Slime adjacent to worker (worker is adjacent to Supercomputer)
        Slime slime = new Slime();
        if (spawnService.spawnAdjacent(location, slime)) {
            result += " A Slime spawns nearby due to the cooling system failure!";
        } else {
            result += " A Slime tried to spawn but found no empty adjacent tile!";
        }
        return result;
    }

    /**
     * Returns the sell price of the industrial fan.
     *
     * @return sell price in worker credits
     */
    @Override
    public int getSellPrice() {
        return SELL_PRICE;
    }

    /**
     * Returns the menu text fragment for the sell action.
     *
     * @return menu text fragment
     */
    @Override
    public String getSellDescription() {
        return "Industrial Fan (150 worker credits)";
    }

    /**
     * Removes the industrial fan from the actor's inventory after selling.
     *
     * @param actor the actor whose inventory to remove this item from
     */
    @Override
    public void removeAfterSell(Actor actor) {
        actor.getInventory().remove(this);
    }

    /**
     * Deposits the industrial fan to the Supercomputer for 10 company credits.
     * The Company rewards compliance with a burst of fresh oxygen —
     * the worker is healed for 10 HP.
     *
     * @param actor the actor depositing the fan
     * @param quotaSystem the quota system to add company credits to
     * @param location the current location of the actor
     * @return the result message of the deposit and any side effects
     */
    @Override
    public String deposit(Actor actor, QuotaSystem quotaSystem, Location location) {
        quotaSystem.addCompanyCredits(DEPOSIT_VALUE);
        actor.heal(HEAL_AMOUNT);
        String result = actor + " deposits the Industrial Fan for "
                + DEPOSIT_VALUE + " company credits. "
                + "A burst of fresh oxygen heals the worker for "
                + HEAL_AMOUNT + " HP!";

        return result;
    }

    /**
     * Returns the company credit value of the industrial fan.
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
        return "Industrial Fan (10 company credits, heals 10 HP)";
    }

    /**
     * Removes the industrial fan from the actor's inventory after depositing.
     *
     * @param actor the actor whose inventory to remove this item from
     */
    @Override
    public void removeAfterDeposit(Actor actor) {
        actor.getInventory().remove(this);
    }
}