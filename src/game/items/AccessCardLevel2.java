package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.doors.ClearanceLevel;

/**
 * A level 2 access card that unlocks Aluminium and Iron doors.
 * Costs 100 credits to purchase from the Supercomputer.
 * To calibrate the card to the user, the Supercomputer forcefully
 * extracts a blood sample, dealing exactly 5 points of damage
 * to the buyer immediately upon purchase.
 *
 * @author Raima
 * @version 1.0
 */
public class AccessCardLevel2 extends AccessCard {

    /**
     * Weight of the level 2 access card.
     */
    public static final int WEIGHT = 2;

    /**
     * Purchase price of the level 2 access card.
     */
    public static final int BUY_PRICE = 100;

    /**
     * Damage dealt to the buyer as a blood sample calibration fee.
     */
    public static final int CALIBRATION_DAMAGE = 5;

    /**
     * Creates a level 2 access card.
     */
    public AccessCardLevel2() {
        super("Level 2 Access Card", 'α', WEIGHT, ClearanceLevel.LEVEL_2);
    }

    /**
     * Purchases the level 2 access card.
     * Deducts 100 credits from the wallet, adds the card to the
     * worker's inventory and deals 5 damage to the buyer as a
     * blood sample calibration fee.
     *
     * @param actor the actor buying the card
     * @param wallet the wallet to deduct credits from
     * @param location the location of the actor
     * @return the result message
     */
    @Override
    public String buy(Actor actor, Wallet wallet, Location location) {
        if (!wallet.hasEnoughCredits(BUY_PRICE)) {
            return actor + " cannot afford the Level 2 Access Card. Costs "
                    + BUY_PRICE + " credits but only has "
                    + wallet.getCredits() + " credits.";
        }
        wallet.deductCredits(BUY_PRICE);
        actor.getInventory().add(this);
        actor.hurt(CALIBRATION_DAMAGE); // ADDED: blood sample calibration deals 5 damage
        return actor + " purchases a Level 2 Access Card for " + BUY_PRICE
                + " credits. The Supercomputer extracts a blood sample, dealing "
                + CALIBRATION_DAMAGE + " damage!";
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
        return "Level 2 Access Card (unlocks Aluminium and Iron doors)";
    }
}