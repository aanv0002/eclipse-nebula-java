package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;

/**
 * A lightweight piece of ancient technology.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class FloppyDisk extends Item {

    /**
     * Weight of the floppy disk.
     */
    public static final int WEIGHT = 1;

    /**
     * Creates a floppy disk.
     */
    public FloppyDisk() {
        super("Floppy Disk", '⊟');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }
}