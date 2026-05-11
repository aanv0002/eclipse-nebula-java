package game.behaviours;

import edu.monash.fit2099.engine.positions.Location;

/**
 * A selector that chooses one infection target near a Parasite.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public interface InfectionTargetSelector {

    /**
     * Selects a target from the Parasite's current location.
     *
     * @param location the Parasite's current location
     * @return the selected target, or null if no target exists
     */
    InfectionTarget selectTarget(Location location);
}
