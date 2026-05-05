package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.Weapon;

/**
 * An action that allows one actor to attack another using a weapon.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class AttackAction extends Action {

    /**
     * The actor being attacked.
     */
    private final Actor target;

    /**
     * The direction of the target relative to the attacker.
     */
    private final String direction;

    /**
     * The weapon used for the attack.
     */
    private final Weapon weapon;

    /**
     * Creates an attack action.
     *
     * @param target the target actor
     * @param direction the direction of the target
     * @param weapon the weapon used
     */
    public AttackAction(Actor target, String direction, Weapon weapon) {
        this.target = target;
        this.direction = direction;
        this.weapon = weapon;
    }

    /**
     * Executes the attack.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return the result message
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = weapon.attack(actor, target, map);

        if (!target.isConscious()) {
            result += System.lineSeparator() + target.unconscious(actor, map);
        }

        return result;
    }

    /**
     * Returns the menu description.
     *
     * @param actor The actor performing the action.
     * @return menu description
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " attacks " + target + " at " + direction;
    }
}