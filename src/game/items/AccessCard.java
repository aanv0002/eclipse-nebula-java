package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.UnlockDoorAction;
import game.doors.ClearanceLevel;
import game.doors.Unlockable;

/**
 * Abstract base class for all access cards.
 * Access cards allow workers to unlock doors in the facility.
 * Each level of access card can unlock doors up to its clearance level.
 * Subclasses define the specific buy price, weight, display character
 * and buy effect for each card level.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @version 2.0
 */
public abstract class AccessCard extends Item implements Purchasable {

    /**
     * The clearance level provided by this access card.
     */
    private final ClearanceLevel clearanceLevel;

    /**
     * Creates an access card with the given name, display character,
     * weight and clearance level.
     *
     * @param name the name of the access card
     * @param displayChar the display character of the access card
     * @param weight the weight of the access card
     * @param clearanceLevel the clearance level provided by this card
     */
    public AccessCard(String name, char displayChar, int weight, ClearanceLevel clearanceLevel) {
        super(name, displayChar);
        this.clearanceLevel = clearanceLevel;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(weight));
        this.makePortable();
    }

    /**
     * Returns actions available while the card is being carried.
     * Adds an unlock action for each adjacent locked unlockable ground
     * if this card has enough clearance.
     *
     * @param owner the actor carrying this item
     * @param map the current map
     * @return actions enabled by the access card
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        Location currentLocation = map.locationOf(owner);

        for (Exit exit : currentLocation.getExits()) {
            Location destination = exit.getDestination();
            Unlockable unlockable = destination.getGroundAs(Unlockable.class);

            if (unlockable != null
                    && !unlockable.isUnlocked()
                    && clearanceLevel.canOpen(unlockable.getRequiredLevel())) {
                actions.add(new UnlockDoorAction(unlockable, destination));
            }
        }

        return actions;
    }
}