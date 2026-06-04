package game.security;

import edu.monash.fit2099.engine.positions.Location;

/**
 * Defines any structural map component or entity that monitors nearby coordinates
 * and executes complex area-of-effect layout destruction or grid displacement.
 * * @author Shrabonti Majumder
 * @version 1.0
 */
public interface SecurityHazard {
    /**
     * Safely disarms and removes the hazard from the map.
     * @param location the location of the hazard
     */
    void disarm(Location location);

    /**
     * Checks if the hazard is live and dangerous.
     * @return true if active.
     */
    boolean isActive();
}