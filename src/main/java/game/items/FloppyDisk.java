package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;

import java.util.Random;

/**
 * A lightweight piece of ancient technology that can be sold to the Supercomputer.
 * Sells for 1 credit. When sold, there is a 50% chance the Supercomputer glitches
 * and accidentally deducts 50 credits from the worker's wallet after giving
 * the seller 1 credit.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @version 2.0
 */
public class FloppyDisk extends Item implements Sellable {

    /**
     * Weight of the floppy disk.
     */
    public static final int WEIGHT = 1;

    /**
     * Sell price of the floppy disk.
     */
    public static final int SELL_PRICE = 1; // ADDED: sell price constant

    /**
     * Credits deducted from wallet if Supercomputer glitches on sale.
     */
    public static final int GLITCH_DEDUCTION = 50; // ADDED: glitch deduction constant

    /**
     * Chance of the Supercomputer glitching when the floppy disk is sold.
     */
    private static final double GLITCH_CHANCE = 0.5; // ADDED: glitch chance constant

    /**
     * Random number generator for glitch chance calculation.
     */
    private final Random random = new Random(); // ADDED: random for glitch chance


    /**
     * Creates a floppy disk.
     */
    public FloppyDisk() {
        super("Floppy Disk", '⊟');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Sells the floppy disk to the Supercomputer.
     * Adds 1 credit to the wallet. There is a 50% chance the Supercomputer
     * glitches and deducts 50 credits from the wallet after the sale.
     *
     * @param actor the actor selling the floppy disk
     * @param wallet the wallet to add credits to
     * @param location the location of the actor
     * @return the result message
     */
    @Override
    public String sell(Actor actor, Wallet wallet, Location location) {
        wallet.addCredits(SELL_PRICE);
        String result = actor + " sells the Floppy Disk for " + SELL_PRICE + " credit.";

        if (random.nextDouble() < GLITCH_CHANCE) {
            wallet.deductCredits(GLITCH_DEDUCTION);
            result += " The Supercomputer glitches and deducts "
                    + GLITCH_DEDUCTION + " credits!";
        }

        return result;
    }

    /**
     * Returns the sell price of the floppy disk.
     *
     * @return sell price in credits
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
        return "Floppy Disk";
    }

    /**
     * Removes the floppy disk from the actor's inventory after selling.
     *
     * @param actor the actor whose inventory to remove this item from
     */
    @Override
    public void removeAfterSell(Actor actor) {
        actor.getInventory().remove(this);
    }
}