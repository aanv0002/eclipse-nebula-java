package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;

/**
 * A portable first aid kit that permanently increases the owner's maximum health
 * by 1 and restores health to full when used.
 * Can be purchased from the Supercomputer for 1000 credits.
 * Attempting to buy without enough credits will kill the worker on the spot.
 * The first aid kit weighs 25 units and has a 20-turn cooldown that only
 * progresses while being carried.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @version 2.0
 */
public class FirstAidKit extends Item implements Consumable, Purchasable {

    /**
     * Weight of the first aid kit.
     */
    public static final int WEIGHT = 25;

    /**
     * Cooldown duration in turns.
     */
    public static final int COOLDOWN_TURNS = 20;

    /**
     * Purchase price of the first aid kit.
     */
    public static final int BUY_PRICE = 1000; // ADDED: buy price constant

    /**
     * Remaining cooldown turns.
     */
    private int cooldownRemaining;

    /**
     * Tracks the current carrier of the first aid kit.
     */
    private Actor lastCarrier;

    /**
     * Creates a first aid kit.
     */
    public FirstAidKit() {
        super("First Aid Kit", '+');
        this.cooldownRemaining = 0;
        this.lastCarrier = null;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    @Override
    public boolean canConsume() {
        return cooldownRemaining == 0;
    }

    @Override
    public void tick(Location currentLocation, Actor actor) {
        if (lastCarrier != actor) {
            cooldownRemaining = Math.max(cooldownRemaining, COOLDOWN_TURNS + 1);
            lastCarrier = actor;
        }

        if (cooldownRemaining > 0) {
            cooldownRemaining--;
        }
    }

    @Override
    public void tick(Location currentLocation) {
        lastCarrier = null;
    }

    @Override
    public String consume(Actor actor) {
        if (!canConsume()) {
            return actor + " cannot use the first aid kit for another "
                    + cooldownRemaining + " turn(s).";
        }

        actor.modifyStatisticMaximum(ActorStatistics.HEALTH, StatisticOperations.INCREASE, 1);
        actor.modifyStatistic(
                ActorStatistics.HEALTH,
                StatisticOperations.UPDATE,
                actor.getMaximumStatistic(ActorStatistics.HEALTH)
        );

        cooldownRemaining = COOLDOWN_TURNS + 1;

        return actor + " uses the first aid kit, increases maximum health by 1, and restores health to full.";
    }

    @Override
    public String getConsumeDescription() {
        return "uses the first aid kit";
    }

    @Override
    public boolean isConsumedUp() {
        return false;
    }

    /**
     * Purchases the first aid kit from the Supercomputer.
     * Costs 1000 credits. If the worker cannot afford it, the
     * Supercomputer kills them on the spot by setting their HP to 0.
     * Otherwise deducts 1000 credits and adds the kit to the inventory.
     *
     * @param actor the actor buying the first aid kit
     * @param wallet the wallet to deduct credits from
     * @param location the location of the actor
     * @return the result message
     */
    @Override
    public String buy(Actor actor, Wallet wallet, Location location) {
        if (!wallet.hasEnoughCredits(BUY_PRICE)) {
            actor.hurt(actor.getMaximumStatistic(ActorStatistics.HEALTH));
            return actor + " cannot afford the First Aid Kit and is killed by the "
                    + "deeply upset Supercomputer!";
        }

        wallet.deductCredits(BUY_PRICE);
        actor.getInventory().add(this);
        return actor + " purchases a First Aid Kit for " + BUY_PRICE + " credits.";
    }

    /**
     * Returns the buy price of the first aid kit.
     *
     * @return buy price in credits
     */
    @Override
    public int getBuyPrice() {
        return BUY_PRICE;
    }

    /**
     * Returns the menu text fragment for the buy action.
     *
     * @return menu text fragment
     */
    @Override
    public String getBuyDescription() {
        return "First Aid Kit (+1 max HP, full heal)";
    }
}