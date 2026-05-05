package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.DropAction;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;

/**
 * Due to severe budget cuts, the flask is only permitted to hold five (5)
 * mouthfuls of liquid per deployment. Employees are reminded not to consume
 * all five charges in a panic during a single encounter.
 */
public class Flask extends Item implements Consumable {

    /**
     * Weight of the flask.
     */
    public static final int WEIGHT = 3;

    /**
     * Maximum number of uses.
     */
    public static final int MAX_USES = 5;

    /**
     * Remaining uses.
     */
    private int remainingUses;

    /**
     * Creates a flask.
     */
    public Flask() {
        super("Flask", 'u');
        this.remainingUses = MAX_USES;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
    }

    /**
     * Returns whether the flask can still be consumed.
     *
     * @return true if at least one use remains
     */
    @Override
    public boolean canConsume() {
        return remainingUses > 0;
    }

    /**
     * Consumes one use of the flask and heals the actor by 1 point.
     *
     * @param actor the actor drinking from the flask
     * @return result message
     */
    @Override
    public String consume(Actor actor) {
        if (!canConsume()) {
            return actor + " tries to drink from an empty flask.";
        }

        remainingUses--;
        actor.heal(1);
        return actor + " drinks from the flask and heals 1 hit point. "
                + "(" + remainingUses + "/" + MAX_USES + " uses left)";
    }

    /**
     * Returns the menu text fragment for consuming the flask.
     *
     * @return menu text fragment
     */
    @Override
    public String getConsumeDescription() {
        return "drinks from the flask";
    }

    /**
     * Prevents the flask from being dropped.
     *
     * @param actor the actor carrying the flask
     * @return null
     */
    @Override
    public DropAction getDropAction(Actor actor) {
        return null;
    }

    /**
     * Returns whether the flask is fully consumed and should be removed.
     * The flask remains in the inventory even when empty.
     *
     * @return false always
     */
    @Override
    public boolean isConsumedUp() {
        return false;
    }
}