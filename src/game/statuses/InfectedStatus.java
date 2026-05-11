package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;
import game.infection.InfectionEffect;

/**
 * A status that represents a target being infected by a Parasite.
 * The actual behaviour is delegated to a host-specific InfectionEffect.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class InfectedStatus implements Status {

    /**
     * The host-specific behaviour caused by this infection.
     */
    private final InfectionEffect infectionEffect;

    /**
     * Creates an infected status.
     *
     * @param infectionEffect the host-specific infection effect
     */
    public InfectedStatus(InfectionEffect infectionEffect) {
        this.infectionEffect = infectionEffect;
    }

    /**
     * Applies the immediate infection behaviour.
     *
     * @param location the infected host location
     * @return infection result text
     */
    public String onInfected(Location location) {
        return infectionEffect.onInfected(location);
    }

    /**
     * Ticks the infection effect once per game turn.
     *
     * @param currEntity the infected entity
     * @param location the infected entity's location
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        infectionEffect.tick(location);
    }

    /**
     * Returns whether this infected status should remain active.
     *
     * @return true if the infection is still active
     */
    @Override
    public boolean isStatusActive() {
        return infectionEffect.isActive();
    }
}
