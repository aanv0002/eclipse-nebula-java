package game.lunar;

import edu.monash.fit2099.engine.positions.GameMap;

/**
 * A contract for lunar influences activated by different moon phases.
 * Each lunar influence scans the game map for valid targets and
 * triggers the appropriate evolution behaviour on selected creatures
 * or terrain based on the current moon phase data.
 * This interface allows MoonPhaseController to depend on an abstraction
 * rather than concrete influence classes (DIP, OCP).
 *
 * @author Raima
 * @version 1.0
 */
public interface LunarInfluence {

    /**
     * Applies this lunar influence to the game map.
     * Scans the map for valid targets and triggers the appropriate
     * evolution behaviour on selected creatures or terrain.
     *
     * @param map the active game map to apply the influence to
     * @param data the moon phase data retrieved from the API
     */
    void applyInfluence(GameMap map, MoonPhaseData data);

    /**
     * Returns the name of this lunar influence for console output.
     *
     * @return the name of this lunar influence
     */
    String getInfluenceName();
}