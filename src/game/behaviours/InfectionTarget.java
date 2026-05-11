package game.behaviours;

import edu.monash.fit2099.engine.positions.Location;
import game.infection.Infectable;

/**
 * A selected infection target and the location where that target exists.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class InfectionTarget {

    /**
     * The selected infectable target.
     */
    private final Infectable infectable;

    /**
     * The location of the selected target.
     */
    private final Location location;

    /**
     * Creates an infection target.
     *
     * @param infectable the selected infectable target
     * @param location the target location
     */
    public InfectionTarget(Infectable infectable, Location location) {
        this.infectable = infectable;
        this.location = location;
    }

    /**
     * Returns the infectable target.
     *
     * @return the infectable target
     */
    public Infectable infectable() {
        return infectable;
    }

    /**
     * Returns the target location.
     *
     * @return the target location
     */
    public Location location() {
        return location;
    }
}
