package game.lunar;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A contract for evolution behaviours applied to creatures or terrain
 * by a LunarInfluence. Each evolution behaviour defines a complex
 * effect such as spawning creatures, replacing actors, applying
 * status effects or modifying terrain.
 * This interface allows LunarInfluence classes to depend on an
 * abstraction rather than concrete evolution classes (DIP, OCP).
 *
 * @author Raima
 * @version 1.0
 */
public interface EvolutionBehaviour {

    /**
     * Applies this evolution behaviour to the target actor at the
     * given location on the game map.
     * Each implementation defines its own complex effect such as
     * spawning new creatures, replacing the actor with a stronger
     * variant or modifying surrounding terrain.
     *
     * @param target the actor to apply the evolution to
     * @param location the location of the target actor
     * @param map the active game map
     * @return a message describing the evolution effect
     */
    String evolve(Actor target, Location location, GameMap map);

    /**
     * Returns the name of this evolution behaviour for console output.
     *
     * @return the name of this evolution behaviour
     */
    String getEvolutionName();
}