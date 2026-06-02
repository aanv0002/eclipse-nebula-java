package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.statuses.RootStatus;

/**
 * A molded apple created when spores mutate an apple.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
public class MoldedApple extends Item implements Consumable {

    private boolean consumed;

    public MoldedApple() {
        super("Molded Apple", 'õ');
        this.consumed = false;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(1));
        this.makePortable();
    }

    @Override
    public boolean canConsume() {
        return !consumed;
    }

    @Override
    public String consume(Actor actor) {
        consumed = true;
        actor.addStatus(new RootStatus());
        return actor + " eats a molded apple and becomes rooted.";
    }

    @Override
    public String getConsumeDescription() {
        return "eats the molded apple";
    }

    @Override
    public boolean isConsumedUp() {
        return consumed;
    }

    @Override
    public void removeIfConsumedUp(Actor actor, GameMap map) {
        if (isConsumedUp()) {
            actor.getInventory().remove(this);
        }
    }
}