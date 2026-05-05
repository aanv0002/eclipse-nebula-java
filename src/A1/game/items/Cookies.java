package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;

/**
 * A packet of cookies containing 5 cookies.
 * Without a SterilisationBox, eating one cookie permanently decreases max HP by 1.
 * With a SterilisationBox, eating one cookie heals 1 HP.
 * The item disappears only after all cookies are consumed.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class Cookies extends Item implements Consumable {

    /**
     * Weight of the cookies.
     */
    public static final int WEIGHT = 2;

    /**
     * Total number of cookies.
     */
    public static final int TOTAL_COOKIES = 5;

    /**
     * Remaining cookies.
     */
    private int remainingCookies;

    /**
     * Creates a cookie packet.
     */
    public Cookies() {
        super("Cookies", '◍');
        this.remainingCookies = TOTAL_COOKIES;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    @Override
    public boolean canConsume() {
        return remainingCookies > 0;
    }

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

    @Override
    public String getConsumeDescription() {
        return "eats a cookie";
    }

    @Override
    public boolean isConsumedUp() {
        return remainingCookies == 0;
    }
}