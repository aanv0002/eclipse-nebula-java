package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.inventory.BasicInventory;
import game.items.Moldable;

/**
 * A short-lived spore actor that mutates nearby organic moldable items.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
public class Spores extends Actor {

    /**
     * Creates spores.
     */
    public Spores() {
        super("Spores", '웃', 1, new BasicInventory());
    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        Location current = map.locationOf(this);
        int mutatedItems = mutateNearbyItems(current);

        display.println(this + " mutates " + mutatedItems + " nearby organic item(s).");

        this.unconscious(map);
        return new DoNothingAction();
    }

    /**
     * Mutates moldable items on the current and adjacent tiles.
     *
     * @param current current location
     * @return number of mutated items
     */
    private int mutateNearbyItems(Location current) {
        int count = mutateItemsAt(current);

        for (Exit exit : current.getExits()) {
            count += mutateItemsAt(exit.getDestination());
        }

        return count;
    }

    /**
     * Mutates moldable items at a location.
     *
     * @param location location to scan
     * @return number of mutated items
     */
    private int mutateItemsAt(Location location) {
        int count = 0;

        for (Moldable moldable : location.getItemsAs(Moldable.class)) {
            location.removeItem(moldable.asItem());
            location.addItem(moldable.createMoldedVersion());
            count++;
        }

        return count;
    }
}