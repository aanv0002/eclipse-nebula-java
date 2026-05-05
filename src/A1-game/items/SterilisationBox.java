package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;

/**
 * A portable sterilisation box.
 * For REQ1, it is a unique ship item that can be picked up and carried.
 * It weighs 7 units.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class SterilisationBox extends Item {

    /**
     * Weight of the sterilisation box.
     */
    public static final int WEIGHT = 7;

    /**
     * Creates a sterilisation box.
     */
    public SterilisationBox() {
        super("Sterilisation Box", '▣');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
        this.enableAbility(ItemCapabilities.STERILISE);
    }
}