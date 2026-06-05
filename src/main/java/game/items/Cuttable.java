package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * A contract for objects that can be cut using a PlasmaCutter.
 * Each cuttable object defines its own cut effect, such as dropping items,
 * transforming the tile or spawning creatures.
 * This interface allows CutAction to depend on an abstraction rather than
 * concrete classes, avoiding instanceof checks (DIP, OCP).
 *
 * @author Raima
 * @version 1.0
 */
public interface Cuttable {

    /**
     * Cuts this object and applies its effect.
     * The effect triggers immediately at the point of the cut action.
     * For example cutting an AluminiumDoor drops AluminiumScrap and
     * transforms the tile into a Floor.
     *
     * @param actor the actor performing the cut
     * @param map the current game map
     * @return a message describing the result of the cut and any side effects
     */
    String cut(Actor actor, GameMap map);

    /**
     * Returns the menu text fragment describing this object for the cut action.
     *
     * @return a short descriptive string for the cut menu option
     */
    String getCutDescription();
}