package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;

/**
 * A portable first aid kit that permanently increases the owner's maximum health
 * by 1 and restores health to full when used.
 *
 * The first aid kit weighs 25 units and has a 20-turn cooldown.
 * The cooldown only progresses while being carried.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class FirstAidKit extends Item implements Consumable {

    /**
     * Weight of the first aid kit.
     */
    public static final int WEIGHT = 25;

    /**
     * Cooldown duration in turns.
     */
    public static final int COOLDOWN_TURNS = 20;

    /**
     * Remaining cooldown turns.
     */
    private int cooldownRemaining;

    /**
     * Tracks the current carrier of the first aid kit.
     * Used to detect when the item has just been picked up.
     */
    private Actor lastCarrier;

    /**
     * Creates a first aid kit.
     */
    public FirstAidKit() {
        super("First Aid Kit", '+');
        this.cooldownRemaining = 0;
        this.lastCarrier = null;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Returns whether the first aid kit is ready to be used.
     *
     * @return true if usable
     */
    @Override
    public boolean canConsume() {
        return cooldownRemaining == 0;
    }

    /**
     * Inform a carried Item of the passage of time.
     * The cooldown only progresses while this item is being carried.
     * When the item is newly picked up, it starts a 20-turn cooldown.
     *
     * @param currentLocation the location of the owner
     * @param actor the actor carrying the item
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        if (lastCarrier != actor) {
            cooldownRemaining = Math.max(cooldownRemaining, COOLDOWN_TURNS + 1);
            lastCarrier = actor;
        }

        if (cooldownRemaining > 0) {
            cooldownRemaining--;
        }
    }

    /**
     * Inform an Item on the ground of the passage of time.
     * Resets carrier tracking so that picking the item up again will start cooldown.
     *
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        lastCarrier = null;
    }

    /**
     * Uses the first aid kit.
     *
     * @param actor the actor using the kit
     * @return result message
     */
    @Override
    public String consume(Actor actor) {
        if (!canConsume()) {
            return actor + " cannot use the first aid kit for another "
                    + cooldownRemaining + " turn(s).";
        }

        actor.modifyStatisticMaximum(ActorStatistics.HEALTH, StatisticOperations.INCREASE, 1);
        actor.modifyStatistic(
                ActorStatistics.HEALTH,
                StatisticOperations.UPDATE,
                actor.getMaximumStatistic(ActorStatistics.HEALTH)
        );

        cooldownRemaining = COOLDOWN_TURNS + 1;

        return actor + " uses the first aid kit, increases maximum health by 1, and restores health to full.";
    }

    /**
     * Returns the menu text fragment for consuming the first aid kit.
     *
     * @return menu text fragment
     */
    @Override
    public String getConsumeDescription() {
        return "uses the first aid kit";
    }

    /**
     * Returns whether the first aid kit is fully consumed and should be removed.
     * The first aid kit is never removed after use.
     *
     * @return false always
     */
    @Override
    public boolean isConsumedUp() {
        return false;
    }
}