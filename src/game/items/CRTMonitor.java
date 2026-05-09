package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.grounds.BurningGround;
import game.grounds.Fire;

import java.util.Random;

/**
 * A massive and heavy CRT monitor that can be sold to the Supercomputer.
 * Sells for 25 credits. The sheer relief of offloading this 30-weight object
 * immediately heals the seller for 5 HP.
 * However there is a 20% chance the archaic hardware shorts out the terminal,
 * dealing 2 damage to the seller and spawning Fire on all surrounding tiles.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @version 2.0
 */
public class CRTMonitor extends Item implements Sellable {

    /**
     * Weight of the CRT monitor.
     */
    public static final int WEIGHT = 30;

    /**
     * Sell price of the CRT monitor.
     */
    public static final int SELL_PRICE = 25; // ADDED: sell price constant

    /**
     * Health points healed when the CRT monitor is sold.
     */
    public static final int HEAL_AMOUNT = 5; // ADDED: heal amount constant

    /**
     * Damage dealt to the seller if the terminal shorts out on sale.
     */
    public static final int SHORT_DAMAGE = 2; // ADDED: short circuit damage constant

    /**
     * Chance of the terminal shorting out when the CRT monitor is sold.
     */
    private static final double SHORT_CHANCE = 0.2; // ADDED: short circuit chance constant

    /**
     * Random number generator for short circuit chance calculation.
     */
    private final Random random = new Random(); // ADDED: random for short circuit chance

    /**
     * Creates a CRT monitor.
     */
    public CRTMonitor() {
        super("CRT Monitor", '◙');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Sells the CRT monitor to the Supercomputer.
     * Adds 25 credits to the wallet and heals the seller for 5 HP.
     * There is a 20% chance the terminal shorts out, dealing 2 damage
     * to the seller and spawning Fire on all surrounding tiles.
     *
     * @param actor the actor selling the CRT monitor
     * @param wallet the wallet to add credits to
     * @param location the location of the actor
     * @return the result message
     */
    @Override
    public String sell(Actor actor, Wallet wallet, Location location) {
        wallet.addCredits(SELL_PRICE);
        actor.heal(HEAL_AMOUNT);

        String result = actor + " sells the CRT Monitor for " + SELL_PRICE
                + " credits and feels immediate relief, healing " + HEAL_AMOUNT + " HP!";

        if (random.nextDouble() < SHORT_CHANCE) {
            actor.hurt(SHORT_DAMAGE);
            result += " The terminal shorts out, dealing " + SHORT_DAMAGE + " damage!";

            for (Exit exit : location.getExits()) {
                Location surroundingLocation = exit.getDestination();
                if (surroundingLocation.getGroundAs(BurningGround.class) == null) {
                    surroundingLocation.setGround(new Fire(surroundingLocation.getGround(), 5));
                }
            }
            result += " Fire spawns on all surrounding tiles!";
        }

        return result;
    }

    /**
     * Returns the sell price of the CRT monitor.
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
        return "CRT Monitor";
    }
}