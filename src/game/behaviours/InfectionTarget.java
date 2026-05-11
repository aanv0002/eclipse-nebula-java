package game.behaviours;

import edu.monash.fit2099.engine.positions.Location;
import game.infection.Infectable;

/**
 * Selected infection target and its location.
 */
public class InfectionTarget {

    private final Infectable infectable;
    private final Location location;

    public InfectionTarget(Infectable infectable, Location location) {
        this.infectable = infectable;
        this.location = location;
    }

    public Infectable infectable() {
        return infectable;
    }

    public Location location() {
        return location;
    }
}
