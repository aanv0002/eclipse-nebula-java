package game.lunar;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * A default lunar influence applied during non-major moon phases.
 * Does not apply any evolution behaviour to the game world.
 *
 * @author Raima
 * @version 1.0
 */
public class DefaultLunarInfluence implements LunarInfluence {

    /**
     * Does nothing during non-major moon phases.
     *
     * @param map the active game map
     * @param data the moon phase data
     */
    @Override
    public void applyInfluence(GameMap map, MoonPhaseData data) {
        new Display().println("Lunar system: " + data.getPhaseName()
                + " — no major lunar influence this cycle.");
    }

    /**
     * Returns the name of this lunar influence.
     *
     * @return influence name
     */
    @Override
    public String getInfluenceName() {
        return "Default Lunar Influence";
    }
}