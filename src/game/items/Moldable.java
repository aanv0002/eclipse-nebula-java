package game.items;

import edu.monash.fit2099.engine.items.Item;

/**
 * Represents an organic item that can be mutated into a molded item.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public interface Moldable {

    /**
     * Returns this object as an Item so it can be removed from a location.
     *
     * @return this item
     */
    Item asItem();

    /**
     * Creates the molded replacement item.
     *
     * @return molded item
     */
    Item createMoldedVersion();
}