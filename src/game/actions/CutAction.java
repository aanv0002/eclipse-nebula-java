package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.Cuttable;

/**
 * A generic action for cutting a cuttable object using a PlasmaCutter.
 * Depends on the Cuttable abstraction rather than concrete types,
 * meaning new cuttable objects can be added without modifying this class (OCP, DIP).
 * Follows the same pattern as SellAction and BuyAction.
 *
 * @author Raima
 * @version 1.0
 */
public class CutAction extends Action {

    /**
     * The cuttable object to cut.
     */
    private final Cuttable cuttable;

    /**
     * The direction of the cuttable object from the actor.
     * Empty string if the cuttable is in the actor's inventory.
     */
    private final String direction;

    /**
     * Creates a cut action for the given cuttable object.
     *
     * @param cuttable the object to cut
     * @param direction the direction of the object from the actor,
     *                  or empty string if in inventory
     */
    public CutAction(Cuttable cuttable, String direction) {
        this.cuttable = cuttable;
        this.direction = direction;
    }

    /**
     * Executes the cut action by delegating the cut effect
     * entirely to the cuttable object.
     *
     * @param actor the actor performing the cut
     * @param map the map the actor is on
     * @return the result message of the cut including any side effects
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        return cuttable.cut(actor, map);
    }

    /**
     * Returns the menu description for this action.
     *
     * @param actor the actor performing the action
     * @return menu description including direction if applicable
     */
    @Override
    public String menuDescription(Actor actor) {
        if (direction.isEmpty()) {
            return actor + " cuts " + cuttable.getCutDescription() + " in inventory";
        }
        return actor + " cuts " + cuttable.getCutDescription() + " to the " + direction;
    }
}