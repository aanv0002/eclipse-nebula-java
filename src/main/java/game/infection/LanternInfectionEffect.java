package game.infection;

import edu.monash.fit2099.engine.positions.Location;
import game.items.Lantern;

/**
 * Infection effect for a Lantern host.
 * The infection drains 1 unit of oil from the Lantern every turn.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class LanternInfectionEffect implements InfectionEffect {

    /**
     * The infected Lantern.
     */
    private final Lantern lantern;

    /**
     * Creates a Lantern infection effect.
     *
     * @param lantern the infected Lantern
     */
    public LanternInfectionEffect(Lantern lantern) {
        this.lantern = lantern;
    }

    /**
     * Applies the immediate Lantern infection result.
     *
     * @param location the infected Lantern's location
     * @return infection result message
     */
    @Override
    public String onInfected(Location location) {
        return "The infection begins draining the Lantern's oil.";
    }

    /**
     * Drains 1 oil from the Lantern.
     *
     * @param location the infected Lantern's location
     */
    @Override
    public void tick(Location location) {
        lantern.drainOil(1);
    }

    /**
     * Returns whether the infection should continue.
     *
     * @return true while the Lantern still has oil
     */
    @Override
    public boolean isActive() {
        return lantern.hasOil();
    }
}
