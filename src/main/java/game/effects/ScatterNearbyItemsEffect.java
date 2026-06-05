package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Moves ground items within 3 tiles of the actor into empty adjacent tiles.
 *
 * This effect is triggered immediately when the mannequin enters ActiveState.
 * It scans all locations within 3 tiles, collects loose ground items from those
 * locations, then scatters them into empty tiles adjacent to the mannequin.
 *
 * @author Tan Hong Sze
 * @version 1.3
 */
public class ScatterNearbyItemsEffect implements StateTransitionEffect {

    /**
     * Radius used to search for nearby items.
     */
    private static final int SCATTER_RADIUS = 3;

    @Override
    public void apply(Actor actor, Location location) {
        Display display = new Display();
        List<Location> emptyDestinations = new ArrayList<>();
        List<ItemMove> itemMoves = new ArrayList<>();

        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();

            if (!adjacent.containsAnActor() && adjacent.getItems().isEmpty()) {
                emptyDestinations.add(adjacent);
            }
        }

        if (emptyDestinations.isEmpty()) {
            display.println(actor + " tries to scatter nearby items, but no empty adjacent tile is available.");
            return;
        }

        for (Location source : location.getNearbyLocations(SCATTER_RADIUS)) {
            for (Item item : new ArrayList<>(source.getItems())) {
                itemMoves.add(new ItemMove(item, source));
            }
        }

        int destinationIndex = 0;
        int movedItems = 0;

        for (ItemMove itemMove : itemMoves) {
            Location destination = emptyDestinations.get(destinationIndex % emptyDestinations.size());

            itemMove.source.removeItem(itemMove.item);
            destination.addItem(itemMove.item);

            display.println(actor + " scatters " + itemMove.item
                    + " from (" + itemMove.source.x() + ", " + itemMove.source.y() + ")"
                    + " to (" + destination.x() + ", " + destination.y() + ").");

            destinationIndex++;
            movedItems++;
        }

        display.println(actor + " distorts nearby matter and scatters "
                + movedItems + " item(s) within " + SCATTER_RADIUS + " tiles.");
    }

    /**
     * Stores an item and its original source location before scattering begins.
     */
    private static class ItemMove {
        private final Item item;
        private final Location source;

        private ItemMove(Item item, Location source) {
            this.item = item;
            this.source = source;
        }
    }
}