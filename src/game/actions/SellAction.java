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
 * Selling the cube gives the worker 25 credits in narrative terms and instantly
 * spawns an Undead on an empty adjacent tile if possible.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class SellAction extends Action {

    private static final int CREDITS = 25;

    private final AlienCube cube;

    public SellAction(AlienCube cube) {
        this.cube = cube;
    }

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

    @Override
    public String menuDescription(Actor actor) {
        return actor + " sells the Alien Cube for " + CREDITS + " credits";
    }
}