package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.statuses.PoisonStatus;

/**
 * A spoiled apple that can be consumed.
 * Without a SterilisationBox it poisons the consumer.
 * With a SterilisationBox it heals the consumer.
 * The apple disappears once consumed.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class Apple extends Item implements Consumable {

    /**
     * Weight of the apple.
     */
    public static final int WEIGHT = 1;

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
}