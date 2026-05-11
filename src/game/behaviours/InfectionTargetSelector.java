package game.behaviours;

import edu.monash.fit2099.engine.positions.Location;

/**
 * Selects an infection target near a parasite.
 */
public interface InfectionTargetSelector {

    InfectionTarget selectTarget(Location location);
}
