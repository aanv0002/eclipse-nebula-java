package game.infection;

import edu.monash.fit2099.engine.positions.Location;

/**
 * A host-specific infection effect that runs while an InfectedStatus is active.
 * Different hosts use different implementations to keep infection behaviour
 * extensible without instanceof checks.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public interface InfectionEffect {

    /**
     * Runs immediately when the infection is applied.
     *
     * @param location the infected host location
     * @return immediate infection result text
     */
    String onInfected(Location location);

    /**
     * Runs once per game tick while the infection remains active.
     *
     * @param location the infected host location
     */
    void tick(Location location);

    /**
     * Returns whether this infection effect should continue ticking.
     *
     * @return true if the infection effect is active
     */
    boolean isActive();
}
