package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A status effect that burns an actor for a configurable amount of damage
 * per turn for a fixed duration.
 * The damage per turn is configurable, allowing different burning sources
 * to deal different amounts of damage. For example, standing on fire deals
 * 1 damage per turn, while selling a lantern may burn the seller for
 * 2 damage per turn for 3 turns.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @version 2.0
 */
public class BurningStatus implements Status {

    /**
     * Default damage per turn if not specified.
     */
    public static final int DEFAULT_DAMAGE = 1; // ADDED: default damage constant

    /**
     * Remaining turns of burning.
     */
    private int remainingTurns;

    /**
     * Damage dealt per turn.
     */
    private final int damagePerTurn; // ADDED: configurable damage per turn

    /**
     * Creates a burning status with default damage of 1 per turn.
     *
     * @param duration the number of turns the burning lasts
     */
    public BurningStatus(int duration) {
        this(duration, DEFAULT_DAMAGE); // ADDED: delegate to two parameter constructor
    }

    /**
     * Creates a burning status with custom damage per turn.
     *
     * @param duration the number of turns the burning lasts
     * @param damagePerTurn the amount of damage dealt per turn
     */
    public BurningStatus(int duration, int damagePerTurn) { // ADDED: new constructor with damage parameter
        this.remainingTurns = duration;
        this.damagePerTurn = damagePerTurn;
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
     * Burns the affected actor by the configured damage per turn and shows a message.
     *
     * @param currEntity the entity this status is attached to
     * @param location the current location of the entity
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        currEntity.asCapability(Actor.class).ifPresent(actor -> {
            actor.hurt(damagePerTurn); // EDITED: use configurable damage
            new Display().println(actor + " is burned for " + damagePerTurn + " damage.");
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