package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A status effect that damages an actor by a set amount each turn for a fixed duration.
 * The damage per turn is configurable, allowing different poison sources to deal
 * different amounts of damage. For example, eating a spoiled apple deals 1 damage
 * per turn, while selling an apple without a SterilisationBox deals 2 damage per turn.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @version 2.0
 */
public class PoisonStatus implements Status {

    /**
     * Default damage per turn if not specified.
     */
    public static final int DEFAULT_DAMAGE = 1; // ADDED: default damage constant

    /**
     * Remaining turns of poison.
     */
    private int remainingTurns;

    /**
     * Damage dealt per turn.
     */
    private final int damagePerTurn; // ADDED: configurable damage per turn

    /**
     * Creates a poison status with default damage of 1 per turn.
     *
     * @param duration the number of turns poison should last
     */
    public PoisonStatus(int duration) {
        this(duration, DEFAULT_DAMAGE); // ADDED: delegate to two parameter constructor
    }

    /**
     * Creates a poison status with custom damage per turn.
     *
     * @param duration the number of turns poison should last
     * @param damagePerTurn the amount of damage dealt per turn
     */
    public PoisonStatus(int duration, int damagePerTurn) { // ADDED: new constructor with damage parameter
        this.remainingTurns = duration;
        this.damagePerTurn = damagePerTurn;
    }

    /**
     * Damages the affected actor by the configured damage per turn
     * and reduces remaining duration by 1.
     *
     * @param currEntity the entity this status is attached to
     * @param location the current location of the entity
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        currEntity.asCapability(Actor.class).ifPresent(actor -> actor.hurt(damagePerTurn)); // EDITED: use configurable damage
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