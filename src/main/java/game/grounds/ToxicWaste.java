package game.grounds;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Toxic waste that damages actors standing on it.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class ToxicWaste extends Ground {

    /**
     * Creates toxic waste.
     */
    public ToxicWaste() {
        super('≈', "Toxic Waste");
    }

    @Override
    public void tick(Location location) {
        if (location.containsAnActor()) {
            location.getActor().hurt(1);
            new Display().println(location.getActor() + " is damaged by toxic waste.");
        }
    }
}