package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;

/**
 * A massive and heavy CRT monitor.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class CRTMonitor extends Item {

    /**
     * Weight of the CRT monitor.
     */
    public static final int WEIGHT = 30;

    /**
     * Creates a CRT monitor.
     */
    public CRTMonitor() {
        super("CRT Monitor", '◙');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }
}