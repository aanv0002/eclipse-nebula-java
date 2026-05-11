package game.infection;

import edu.monash.fit2099.engine.positions.Location;

/**
 * Host-specific effect that runs while an infected status is active.
 */
public interface InfectionEffect {

    String onInfected(Location location);

    void tick(Location location);

    boolean isActive();
}
