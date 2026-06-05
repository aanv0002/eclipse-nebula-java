package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.LaserBarrierGround;

/**
 * Places active intercepting laser barrier on an adjacent spot.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class DeployBarrierAction extends Action {

    private final Item barrier;
    private final Location destination;
    private final String direction;

    public DeployBarrierAction(Item barrier, Location destination, String direction) {
        this.barrier = barrier;
        this.destination = destination;
        this.direction = direction;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        destination.setGround(new LaserBarrierGround());
        actor.getInventory().remove(barrier);
        return actor + " strategically deploys a Laser Barrier to the " + direction + ".";
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " deploys a Laser Barrier component to the " + direction;
    }
}