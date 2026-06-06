package game.lunar;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;
import game.statuses.PoisonStatus;

/**
 * A status effect applied when a dormant creature wakes up during
 * the New Moon or when lunar energy makes creatures hostile.
 * Causes the affected creature to deal additional poison damage
 * to adjacent workers each turn for 5 turns, representing
 * heightened aggression from the lunar influence.
 *
 * @author Raima
 * @version 1.0
 */
public class AggressiveStatus implements Status {

    /**
     * Remaining turns of aggressive status.
     */
    private int remainingTurns;

    /**
     * Duration of aggressive status in turns.
     */
    private static final int DURATION = 5;

    /**
     * Additional poison damage dealt to adjacent workers each turn.
     */
    private static final int POISON_DAMAGE = 2;

    /**
     * Poison duration applied to adjacent workers.
     */
    private static final int POISON_TURNS = 3;

    /**
     * Creates an AggressiveStatus with default duration of 5 turns.
     */
    public AggressiveStatus() {
        this.remainingTurns = DURATION;
    }

    /**
     * Each turn applies additional PoisonStatus to all adjacent workers
     * representing the creature's heightened aggression from lunar energy.
     *
     * @param currEntity the entity this status is attached to
     * @param location the current location of the entity
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        currEntity.asCapability(Actor.class).ifPresent(actor -> {
            for (edu.monash.fit2099.engine.positions.Exit exit : location.getExits()) {
                Actor adjacent = exit.getDestination().getActor();
                if (adjacent != null && adjacent.hasAbility(ActorCapabilities.WORKER)) {
                    adjacent.addStatus(new PoisonStatus(POISON_TURNS, POISON_DAMAGE));
                    new Display().println(actor + " lunges aggressively at "
                            + adjacent + " poisoning them for "
                            + POISON_TURNS + " turns!");
                }
            }
        });
        remainingTurns--;
    }

    /**
     * Returns whether this status is still active.
     *
     * @return true if aggressive status has remaining turns
     */
    @Override
    public boolean isStatusActive() {
        return remainingTurns > 0;
    }
}