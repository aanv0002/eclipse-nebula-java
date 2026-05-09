package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.statuses.PoisonStatus;

/**
 * A spoiled apple that can be consumed or sold to the Supercomputer.
 * Without a SterilisationBox it poisons the consumer for 5 turns dealing 1 damage per turn.
 * With a SterilisationBox it heals the consumer for 3 hit points.
 * When sold without a SterilisationBox, it poisons the seller for 2 turns
 * dealing 2 damage per turn.
 * The apple disappears once consumed or sold.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @version 2.0
 */
public class Apple extends Item implements Consumable, Sellable {

    /**
     * Weight of the apple.
     */
    public static final int WEIGHT = 1;

    /**
     * Sell price of the apple.
     */
    public static final int SELL_PRICE = 1; // ADDED: sell price constant

    /**
     * Number of turns the seller is poisoned when sold without SterilisationBox.
     */
    public static final int SELL_POISON_TURNS = 2; // ADDED: poison duration on sell

    /**
     * Damage dealt per turn when seller is poisoned after selling.
     */
    public static final int SELL_POISON_DAMAGE = 2; // ADDED: poison damage on sell

    /**
     * Whether this apple has been consumed.
     */
    private boolean consumed;

    /**
     * Creates an apple.
     */
    public Apple() {
        super("Apple", 'ó');
        this.consumed = false;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    @Override
    public boolean canConsume() {
        return !consumed;
    }

    @Override
    public String consume(Actor actor) {
        consumed = true;

        if (actor.hasAbility(ItemCapabilities.STERILISE)) {
            actor.heal(3);
            return actor + " eats the sterilised apple and heals 3 hit points.";
        }

        actor.addStatus(new PoisonStatus(5));
        return actor + " eats the spoiled apple and is poisoned.";
    }

    @Override
    public String getConsumeDescription() {
        return "eats the apple";
    }

    @Override
    public boolean isConsumedUp() {
        return consumed;
    }

    /**
     * Sells the apple to the Supercomputer.
     * Adds 1 credit to the wallet. If the seller does not have a
     * SterilisationBox, they are poisoned for 2 turns dealing 2 damage per turn.
     *
     * @param actor the actor selling the apple
     * @param wallet the wallet to add credits to
     * @param location the location of the actor
     * @return the result message
     */
    @Override
    public String sell(Actor actor, Wallet wallet, Location location) {
        wallet.addCredits(SELL_PRICE);
        String result = actor + " sells the apple for " + SELL_PRICE + " credit.";

        if (!actor.hasAbility(ItemCapabilities.STERILISE)) {
            actor.addStatus(new PoisonStatus(SELL_POISON_TURNS, SELL_POISON_DAMAGE));
            result += " The toxic apple poisons the seller for "
                    + SELL_POISON_TURNS + " turns dealing "
                    + SELL_POISON_DAMAGE + " damage per turn!";
        }

        return result;
    }

    /**
     * Returns the sell price of the apple.
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
        return "Apple";
    }
}