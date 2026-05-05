package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.UnlockDoorAction;
import game.grounds.Unlockable;

/**
 * A class representing a small rectangular piece of plastic that holds entirely
 * too much power over your ability to walk through doors.
 * Its primary function is to beep happily when the player has clearance, and beep
 * angrily when they don't.
 * Essential for progressing the plot.
 *
 * @author Adrian Kristanto
 */
public class AccessCard extends Item {

    /**
     * Weight of the access card.
     */
    public static final int WEIGHT = 1;

    /**
     * Creates an access card.
     */
    public AccessCard() {
        super("Access Card", '▤');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Returns actions available while the card is being carried.
     * Adds an unlock action for each adjacent locked unlockable ground.
     *
     * Alarm checking is not performed here. The actual restriction is enforced
     * by UnlockDoorAction, which keeps the alarm rule centralised in one place.
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
            Unlockable unlockable = exit.getDestination().getGroundAs(Unlockable.class);
            if (unlockable != null && !unlockable.isUnlocked()) {
                actions.add(new UnlockDoorAction(unlockable));
            }
        }

        return actions;
    }
}