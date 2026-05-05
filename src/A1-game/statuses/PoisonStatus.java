package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A status effect that damages an actor by 1 point each turn for a fixed duration.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class PoisonStatus implements Status {

    /**
     * Remaining turns of poison.
     */
    private int remainingTurns;

    /**
     * Creates a poison status.
     *
     * @param duration the number of turns poison should last
     */
    public PoisonStatus(int duration) {
        this.remainingTurns = duration;
    }

    /**
     * Damages the affected actor by 1 point and reduces remaining duration by 1.
     *
     * @param currEntity the entity this status is attached to
     * @param location the current location of the entity
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        currEntity.asCapability(Actor.class).ifPresent(actor -> actor.hurt(1));
        remainingTurns--;
    }

    /**
     * Returns whether the poison is still active.
     *
     * @return true if poison is still active, false otherwise
     */
    @Override
    public boolean isStatusActive() {
        return remainingTurns > 0;
    }
}