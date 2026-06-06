package game.lunar;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * An evolution behaviour triggered by the New Moon influence.
 * Changes selected creatures into dormant ambush enemies that
 * stay inactive until a worker enters their detection range,
 * then become aggressive and attack with increased damage.
 *
 * @author Raima
 * @version 1.0
 */
public class AmbushEvolution implements EvolutionBehaviour {

    /**
     * Creates an AmbushEvolution.
     */
    public AmbushEvolution() {
    }

    /**
     * Applies the ambush evolution to the target creature.
     * Adds DormantStatus to the creature making it inactive
     * until a worker enters its detection range.
     *
     * @param target the creature to evolve into an ambush enemy
     * @param location the location of the target creature
     * @param map the active game map
     * @return a message describing the ambush evolution effect
     */
    @Override
    public String evolve(Actor target, Location location, GameMap map) {
        target.addStatus(new DormantStatus());

        String result = "New Moon Ambush Evolution: " + target
                + " becomes dormant and lies in wait for unsuspecting workers!";

        new Display().println(result);
        return result;
    }

    /**
     * Returns the name of this evolution behaviour.
     *
     * @return evolution name
     */
    @Override
    public String getEvolutionName() {
        return "Ambush Evolution";
    }
}