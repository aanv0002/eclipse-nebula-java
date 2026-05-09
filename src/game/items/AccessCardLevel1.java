package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.doors.ClearanceLevel;

/**
 * A basic level 1 access card that unlocks Aluminium doors.
 * Costs 50 credits to purchase from the Supercomputer.
 * This is the standard entry-level security clearance card
 * with no additional side effects when purchased.
 *
 * @author Raima
 * @version 1.0
 */
public class AccessCardLevel1 extends AccessCard {

    /**
     * Weight of the level 1 access card.
     */
    public static final int WEIGHT = 1;

    /**
     * Purchase price of the level 1 access card.
     */
    public static final int BUY_PRICE = 50;

    /**
     * Creates a level 1 access card.
     */
    public AccessCardLevel1() {
        super("Level 1 Access Card", '▤', WEIGHT, ClearanceLevel.LEVEL_1);
    }

    /**
     * Purchases the level 1 access card.
     * Deducts 50 credits from the wallet and adds the card to the
     * worker's inventory. No additional side effects.
     *
     * @param actor the actor buying the card
     * @param wallet the wallet to deduct credits from
     * @param location the location of the actor
     * @return the result message
     */
    @Override
    public String buy(Actor actor, Wallet wallet, Location location) {
        if (!wallet.hasEnoughCredits(BUY_PRICE)) {
            return actor + " cannot afford the Level 1 Access Card. Costs "
                    + BUY_PRICE + " credits but only has "
                    + wallet.getCredits() + " credits.";
        }
        wallet.deductCredits(BUY_PRICE);
        actor.getInventory().add(this);
        return actor + " purchases a Level 1 Access Card for " + BUY_PRICE + " credits.";
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
        return "Level 1 Access Card (unlocks Aluminium doors)";
    }
}