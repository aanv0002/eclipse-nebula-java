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
 * A class representing a small rectangular piece of plastic that holds entirely
 * too much power over your ability to walk through doors.
 * Its primary function is to beep happily when the player has clearance, and beep
 * angrily when they don't.
 * Essential for progressing the plot.
 *
 * @author Adrian Kristanto
 * @version 2.0
 */
public class AccessCard extends Item {

    /**
     * Weight of the access card.
     */
    public static final int WEIGHT = 1;

    /**
     * Clearance level provided by this access card.
     */
    private final ClearanceLevel clearanceLevel;

    /**
     * Creates a level 1 access card.
     */
    public AccessCard() {
        this(ClearanceLevel.LEVEL_1);
    }

    /**
     * Creates an access card with a specific clearance level.
     *
     * @param clearanceLevel the clearance level provided by this card
     */
    public AccessCard(ClearanceLevel clearanceLevel) {
        super(getCardName(clearanceLevel), '▤');
        this.clearanceLevel = clearanceLevel;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Returns the clearance level of this access card.
     *
     * @return the clearance level
     */
    public ClearanceLevel getClearanceLevel() {
        return clearanceLevel;
    }

    /**
     * Returns actions available while the card is being carried.
     * Adds an unlock action for each adjacent locked unlockable ground if this
     * card has enough clearance.
     *
     * Alarm checking is not performed here. The actual alarm restriction is
     * enforced by UnlockDoorAction, which keeps the lockdown rule centralised in
     * one place.
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

    /**
     * Returns the display name for an access card.
     *
     * @param clearanceLevel the clearance level
     * @return formatted card name
     */
    private static String getCardName(ClearanceLevel clearanceLevel) {
        return switch (clearanceLevel) {
            case LEVEL_1 -> "Level 1 Access Card";
            case LEVEL_2 -> "Level 2 Access Card";
            case LEVEL_3 -> "Level 3 Access Card";
        };
    }
}