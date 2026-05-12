package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.infection.CookieInfectionEffect;
import game.infection.Infectable;
import game.statuses.InfectedStatus;

/**
 * A packet of cookies containing 5 cookies that can be consumed or sold.
 * Without a SterilisationBox, eating one cookie permanently decreases max HP by 1.
 * With a SterilisationBox, eating one cookie heals 1 HP.
 * When sold, the price is dynamic based on remaining cookies (1 credit per cookie).
 * Selling instantly deducts 1 HP from the seller for every cookie sold as an
 * organic processing fee.
 * The item disappears only after all cookies are consumed or when sold.
 *
 * Cookies can also be infected by a Parasite. An infected packet loses 1 cookie
 * every turn and attempts to spawn Parasites beside it.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @author Shrabonti Majumder
 * @version 2.0
 */
public class Cookies extends Item implements Consumable, Sellable, Infectable {

    /**
     * Weight of the cookies.
     */
    public static final int WEIGHT = 2;

    /**
     * Total number of cookies.
     */
    public static final int TOTAL_COOKIES = 5;

    /**
     * Price per remaining cookie when sold to the Supercomputer.
     */
    public static final int PRICE_PER_COOKIE = 1;

    /**
     * HP deducted per cookie when sold as organic processing fee.
     */
    public static final int HP_COST_PER_COOKIE = 1;

    /**
     * Remaining cookies.
     */
    private int remainingCookies;

    /**
     * Creates a cookie packet.
     */
    public Cookies() {
        super("Cookies", 'c');
        this.remainingCookies = TOTAL_COOKIES;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Returns whether at least one cookie can be consumed.
     *
     * @return true if cookies remain
     */
    @Override
    public boolean canConsume() {
        return remainingCookies > 0;
    }

    /**
     * Consumes one cookie.
     *
     * @param actor the actor consuming the cookie
     * @return result message
     */
    @Override
    public String consume(Actor actor) {
        if (!canConsume()) {
            return actor + " has no cookies left to eat.";
        }

        remainingCookies--;

        String result;
        if (actor.hasAbility(ItemCapabilities.STERILISE)) {
            actor.heal(1);
            result = actor + " eats a sterilised cookie and heals 1 hit point.";
        } else {
            actor.modifyStatisticMaximum(ActorStatistics.HEALTH, StatisticOperations.DECREASE, 1);
            result = actor + " eats a toxic cookie and permanently loses 1 maximum health.";
        }

        if (remainingCookies == 0) {
            result += " The cookie packet is now empty.";
        }

        return result;
    }

    /**
     * Returns the consume action description.
     *
     * @return consume description
     */
    @Override
    public String getConsumeDescription() {
        return "eats a cookie";
    }

    /**
     * Returns whether all cookies are gone.
     *
     * @return true if no cookies remain
     */
    @Override
    public boolean isConsumedUp() {
        return remainingCookies == 0;
    }

    /**
     * Removes the cookie packet if empty.
     *
     * @param actor the actor consuming the object
     * @param map the current map
     */
    @Override
    public void removeIfConsumedUp(Actor actor, GameMap map) {
        if (!isConsumedUp()) {
            return;
        }

        if (actor.getInventory().getItems().contains(this)) {
            actor.getInventory().remove(this);
        } else {
            map.locationOf(actor).removeItem(this);
        }
    }

    /**
     * Sells the cookies to the Supercomputer.
     *
     * @param actor the actor selling the cookies
     * @param wallet the wallet to add credits to
     * @param location the location of the actor
     * @return the result message
     */
    @Override
    public String sell(Actor actor, Wallet wallet, Location location) {
        int cookiesToSell = remainingCookies;
        int creditsEarned = cookiesToSell * PRICE_PER_COOKIE;

        wallet.addCredits(creditsEarned);
        actor.hurt(cookiesToSell * HP_COST_PER_COOKIE);

        remainingCookies = 0;

        return actor + " sells " + cookiesToSell + " cookies for "
                + creditsEarned + " credits. The organic processing fee deducts "
                + cookiesToSell + " HP!";
    }

    /**
     * Returns the sell price of the cookies based on remaining quantity.
     *
     * @return sell price in credits
     */
    @Override
    public int getSellPrice() {
        return remainingCookies * PRICE_PER_COOKIE;
    }

    /**
     * Returns the menu text fragment for the sell action.
     *
     * @return menu text fragment
     */
    @Override
    public String getSellDescription() {
        return "Cookies (" + remainingCookies + " remaining)";
    }

    /**
     * Removes the cookies from the actor's inventory after selling.
     *
     * @param actor the actor whose inventory to remove this item from
     */
    @Override
    public void removeAfterSell(Actor actor) {
        actor.getInventory().remove(this);
    }

    /**
     * Reduces cookie content due to infection.
     *
     * @param amount amount to reduce
     */
    public void reduceCookies(int amount) {
        remainingCookies = Math.max(0, remainingCookies - amount);
    }

    /**
     * Returns whether cookies remain.
     *
     * @return true if cookies remain
     */
    public boolean hasCookies() {
        return remainingCookies > 0;
    }

    /**
     * Returns whether this cookie packet can be infected.
     *
     * @return true if cookies remain and no infection is active
     */
    @Override
    public boolean canBeInfected() {
        return hasCookies() && !this.hasStatus(InfectedStatus.class);
    }

    /**
     * Applies Parasite infection to the cookie packet.
     *
     * @param parasite the Parasite applying infection
     * @param location the cookie packet's location
     * @return infection result message
     */
    @Override
    public String infect(Actor parasite, Location location) {
        InfectedStatus infectedStatus = new InfectedStatus(new CookieInfectionEffect(this));
        this.addStatus(infectedStatus);
        return infectedStatus.onInfected(location);
    }
}
