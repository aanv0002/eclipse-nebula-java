package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.doors.ClearanceLevel;

import java.util.Random;

/**
 * A level 3 access card that unlocks all doors currently in the game.
 * Costs 200 credits to purchase from the Supercomputer.
 * Due to predatory pricing algorithms, there is a 50% chance the
 * Supercomputer will hit the buyer with a hidden fee, deducting an
 * additional 50 credits on top of the 200-credit purchase price.
 *
 * @author Raima
 * @version 1.0
 */
public class AccessCardLevel3 extends AccessCard {

    /**
     * Weight of the level 3 access card.
     */
    public static final int WEIGHT = 3;

    /**
     * Purchase price of the level 3 access card.
     */
    public static final int BUY_PRICE = 200;

    /**
     * Hidden fee that may be charged upon purchase.
     */
    public static final int HIDDEN_FEE = 50;

    /**
     * Chance of being charged the hidden fee.
     */
    private static final double HIDDEN_FEE_CHANCE = 0.5;

    /**
     * Random number generator for hidden fee chance calculation.
     */
    private final Random random = new Random();

    /**
     * Creates a level 3 access card.
     */
    public AccessCardLevel3() {
        super("Level 3 Access Card", '◐', WEIGHT, ClearanceLevel.LEVEL_3);
    }

    /**
     * Purchases the level 3 access card.
     * Deducts 200 credits from the wallet and adds the card to the
     * worker's inventory. There is a 50% chance of an additional
     * hidden fee of 50 credits being deducted.
     *
     * @param actor the actor buying the card
     * @param wallet the wallet to deduct credits from
     * @param location the location of the actor
     * @return the result message
     */
    @Override
    public String buy(Actor actor, Wallet wallet, Location location) {
        if (!wallet.hasEnoughCredits(BUY_PRICE)) {
            return actor + " cannot afford the Level 3 Access Card. Costs "
                    + BUY_PRICE + " credits but only has "
                    + wallet.getCredits() + " credits.";
        }

        wallet.deductCredits(BUY_PRICE);
        actor.getInventory().add(this);

        String result = actor + " purchases a Level 3 Access Card for "
                + BUY_PRICE + " credits.";

        // ADDED: 50% chance of hidden fee
        if (random.nextDouble() < HIDDEN_FEE_CHANCE) {
            wallet.deductCredits(HIDDEN_FEE);
            result += " The Supercomputer charges a hidden fee of "
                    + HIDDEN_FEE + " credits!";
        }

        return result;
    }

    /**
     * Returns the buy price of this access card.
     *
     * @return the buy price in credits
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
        return "Level 3 Access Card (unlocks all doors)";
    }
}