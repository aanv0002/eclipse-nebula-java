package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;

import java.util.List;
import java.util.Random;

/**
 * A portable sterilisation box that can be purchased from the Supercomputer.
 * Costs 750 credits. When purchased, the intense radiation emitted by the box
 * instantly and permanently erases one random item from the buyer's inventory.
 * It weighs 7 units and enables the STERILISE capability when carried.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @version 2.0
 */
public class SterilisationBox extends Item implements Purchasable {

    /**
     * Weight of the sterilisation box.
     */
    public static final int WEIGHT = 7;

    /**
     * Purchase price of the sterilisation box.
     */
    public static final int BUY_PRICE = 750; // ADDED: buy price constant

    /**
     * Random number generator for selecting a random item to erase.
     */
    private final Random random = new Random(); // ADDED: random for item erasure

    /**
     * Creates a sterilisation box.
     */
    public SterilisationBox() {
        super("Sterilisation Box", '▣');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
        this.enableAbility(ItemCapabilities.STERILISE);
    }

    /**
     * Purchases the sterilisation box from the Supercomputer.
     * Deducts 750 credits from the wallet and adds the box to the
     * worker's inventory. The intense radiation instantly and permanently
     * erases one random item from the buyer's inventory.
     *
     * @param actor the actor buying the sterilisation box
     * @param wallet the wallet to deduct credits from
     * @param location the location of the actor
     * @return the result message
     */
    @Override
    public String buy(Actor actor, Wallet wallet, Location location) {
        if (!wallet.hasEnoughCredits(BUY_PRICE)) {
            return actor + " cannot afford the Sterilisation Box. Costs "
                    + BUY_PRICE + " credits but only has "
                    + wallet.getCredits() + " credits.";
        }

        wallet.deductCredits(BUY_PRICE);
        actor.getInventory().add(this);

        List<Item> items = actor.getInventory().getItems()
                .stream()
                .filter(item -> item != this)
                .toList();

        String result = actor + " purchases a Sterilisation Box for " + BUY_PRICE + " credits.";

        if (!items.isEmpty()) {
            Item randomItem = items.get(random.nextInt(items.size()));
            actor.getInventory().remove(randomItem);
            result += " The radiation erases " + randomItem + " from the inventory!";
        } else {
            result += " The radiation finds nothing to erase.";
        }

        return result;
    }

    /**
     * Returns the buy price of the sterilisation box.
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
        return "Sterilisation Box (erases one random item from inventory)";
    }
}