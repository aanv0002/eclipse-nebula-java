package game.lunar;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;

/**
 * A status effect applied by AmbushEvolution during the New Moon.
 * Marks a creature as dormant preventing normal wandering behaviour.
 * When a worker enters the creature's adjacent tiles the status
 * is removed and AggressiveStatus is applied making the creature
 * actively chase and attack nearby workers.
 *
 * @author Raima
 * @version 1.0
 */
public class DormantStatus implements Status {

    /**
     * Whether this status is still active.
     */
    private boolean active;

    /**
     * Creates a DormantStatus.
     */
    public DormantStatus() {
        this.active = true;
    }

    /**
     * Checks adjacent tiles each turn for nearby workers.
     * If a worker is detected the creature wakes up and becomes
     * aggressive by replacing this status with AggressiveStatus.
     *
     * @param currEntity the entity this status is attached to
     * @param location the current location of the entity
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        currEntity.asCapability(Actor.class).ifPresent(actor -> {
            for (Exit exit : location.getExits()) {
                Actor adjacent = exit.getDestination().getActor();
                if (adjacent != null && adjacent.hasAbility(ActorCapabilities.WORKER)) {
                    active = false;
                    actor.addStatus(new AggressiveStatus());
                    new Display().println(actor + " wakes from dormancy"
                            + " and becomes aggressive!");
                    return;
                }
            }
        });
    }

    /**
     * Returns whether this status is still active.
     *
     * @return true if dormant status is still active
     */
    @Override
    public boolean isStatusActive() {
        return active;
    }
}