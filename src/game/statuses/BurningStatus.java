package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A status effect that burns an actor for 1 damage per turn for a fixed duration.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class BurningStatus implements Status {

    /**
     * Remaining turns of burning.
     */
    private int remainingTurns;

    /**
     * Creates a burning status.
     *
     * @param duration the number of turns the burning lasts
     */
    public BurningStatus(int duration) {
        this.remainingTurns = duration;
    }

    /**
     * Resets the burning duration.
     *
     * @param duration the new duration
     */
    public void resetDuration(int duration) {
        this.remainingTurns = duration;
    }

    /**
     * Burns the affected actor by 1 damage and shows a message.
     *
     * @param currEntity the entity this status is attached to
     * @param location the current location of the entity
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        currEntity.asCapability(Actor.class).ifPresent(actor -> {
            actor.hurt(1);
            new Display().println(actor + " is burned for 1 damage.");
        });
        remainingTurns--;
    }

    /**
     * Returns whether the burning is still active.
     *
     * @return true if burning is still active, false otherwise
     */
    @Override
    public boolean isStatusActive() {
        return remainingTurns > 0;
    }
}