package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.AttackAction;
import game.actors.ActorCapabilities;

import java.util.ArrayList;

/**
 * A berserk behaviour that attacks an adjacent worker and then forces the target
 * to drop all inventory items onto the ground.
 *
 * This behaviour combines combat with an inventory consequence, making BerserkState
 * more distinct than a normal attack state.
 *
 * This is one of the two new behaviours for REQ5.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class BerserkAttackAndDropBehaviour implements Behaviour<Actor, Action> {

    /**
     * Returns an action that attacks and drops the inventory of an adjacent worker.
     *
     * @param actor the actor performing the behaviour
     * @param location the current location of the actor
     * @return attack-and-drop action, or null if no adjacent worker exists
     */
    @Override
    public Action operate(Actor actor, Location location) {
        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();

            if (destination.containsAnActor()
                    && destination.getActor().hasAbility(ActorCapabilities.WORKER)) {
                return new AttackAndDropInventoryAction(
                        destination.getActor(),
                        destination,
                        exit.getName()
                );
            }
        }

        return null;
    }

    /**
     * Action that attacks a target and then drops all target inventory.
     */
    private static class AttackAndDropInventoryAction extends Action {

        /**
         * Target worker.
         */
        private final Actor target;

        /**
         * Location of the target worker.
         */
        private final Location targetLocation;

        /**
         * Direction of the target relative to the attacker.
         */
        private final String direction;

        /**
         * Creates an attack-and-drop action.
         *
         * @param target target worker
         * @param targetLocation location of the target
         * @param direction direction of the target
         */
        private AttackAndDropInventoryAction(Actor target, Location targetLocation, String direction) {
            this.target = target;
            this.targetLocation = targetLocation;
            this.direction = direction;
        }

        /**
         * Executes the berserk attack and forces the target to drop all inventory.
         *
         * @param actor the actor performing the action
         * @param map the map the actor is on
         * @return result message
         */
        @Override
        public String execute(Actor actor, GameMap map) {
            String result = new AttackAction(
                    target,
                    direction,
                    actor.getIntrinsicWeapon()
            ).execute(actor, map);

            for (Item item : new ArrayList<>(target.getInventory().getItems())) {
                target.getInventory().remove(item);
                targetLocation.addItem(item);
            }

            return result + "\n" + target + " drops all carried items from the shock.";
        }

        /**
         * Returns the menu description.
         *
         * @param actor the actor performing the action
         * @return menu description
         */
        @Override
        public String menuDescription(Actor actor) {
            return actor + " attacks " + target + " and forces them to drop all items";
        }
    }
}