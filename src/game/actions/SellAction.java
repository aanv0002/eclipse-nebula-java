package game.actions;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.Undead;
import game.items.AlienCube;

/**
 * An action for selling an Alien Cube.
 *
 * Selling the cube removes it from the actor's inventory, gives 25 credits in
 * narrative terms, and attempts to spawn one Undead on an empty adjacent tile.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class SellAction extends Action {

    /**
     * Credits gained from selling the Alien Cube.
     */
    private static final int CREDITS = 25;

    /**
     * The Alien Cube being sold.
     */
    private final AlienCube cube;

    /**
     * Creates a sell action for the given Alien Cube.
     *
     * @param cube the Alien Cube to sell
     */
    public SellAction(AlienCube cube) {
        this.cube = cube;
    }

    /**
     * Executes the sale of the Alien Cube.
     *
     * The cube is removed from the actor's inventory. The action then searches
     * adjacent locations and attempts to spawn one Undead on the first empty,
     * enterable tile found.
     *
     * @param actor the actor selling the cube
     * @param map the map the actor is currently on
     * @return result message describing the sale and spawn outcome
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        actor.getInventory().remove(cube);

        Location actorLocation = map.locationOf(actor);
        Undead undead = new Undead();

        for (Exit exit : actorLocation.getExits()) {
            Location adjacent = exit.getDestination();

            if (!adjacent.containsAnActor() && adjacent.canActorEnter(undead)) {
                try {
                    map.addActor(undead, adjacent);
                    return actor + " sells the Alien Cube for " + CREDITS
                            + " credits. An Undead appears nearby!";
                } catch (GameEngineException exception) {
                    return actor + " sells the Alien Cube for " + CREDITS
                            + " credits, but the Undead fails to appear.";
                }
            }
        }

        return actor + " sells the Alien Cube for " + CREDITS
                + " credits, but there is no empty adjacent tile for an Undead to spawn.";
    }

    /**
     * Returns the menu description for selling the Alien Cube.
     *
     * @param actor the actor performing the action
     * @return menu description
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " sells the Alien Cube for " + CREDITS + " credits";
    }
}