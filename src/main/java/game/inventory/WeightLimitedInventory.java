package game.inventory;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Inventory;
import edu.monash.fit2099.engine.items.Item;
import game.items.ItemStatistics;

/**
 * At its core, this is just an oversized {@code ArrayList}.
 * It prevents the player from simply carrying the entire {@code EclipseNebula}
 * with them by enforcing an arbitrary capacity limit.
 *
 * @author Adrian Kristanto
 * @author Shrabonti Majumder
 */
public class WeightLimitedInventory extends Inventory {
    private int weightLimit;
    private int weight;

    public WeightLimitedInventory(int weightLimit) {
        this.weightLimit = weightLimit;
        this.weight = 0;
    }

    /**
     * Sets a new weight limit for the inventory.
     * @param newLimit the new weight limit.
     */
    public void setWeightLimit(int newLimit) {
        this.weightLimit = newLimit;
    }

    /**
     * Retrieves the current weight limit.
     * @return current weight limit.
     */
    public int getWeightLimit() {
        return this.weightLimit;
    }

    @Override
    public boolean add(Item item) {
        Display display = new Display();
        if (item.hasStatistic(ItemStatistics.WEIGHT)) {
            int itemWeight = item.getStatistic(ItemStatistics.WEIGHT);
            if (weight + itemWeight <= this.weightLimit) {
                items.add(item);
                this.weight += itemWeight;
                display.println(String.format("%s added successfully. Current inventory weight (%d/%d)", item, weight, weightLimit));
                return true;
            } else {
                display.println(String.format("Fails to add %s with weight %d. Weight limit will be exceeded (%d/%d)", item, itemWeight, weight + itemWeight, weightLimit));
                return false;
            }
        } else {
            String msg = String.format("%s does not have a weight statistic", item);
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public boolean remove(Item item) {
        if (item.hasStatistic(ItemStatistics.WEIGHT)) {
            int itemWeight = item.getStatistic(ItemStatistics.WEIGHT);
            boolean removed = items.remove(item);
            if (removed) {
                this.weight -= itemWeight;
            }
            return removed;
        } else {
            String msg = String.format("%s does not have a weight statistic", item);
            throw new IllegalArgumentException(msg);
        }
    }
}