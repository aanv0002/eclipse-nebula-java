package game.teleport;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.Fire;

/**
 * Burns tiles surrounding the teleport destination.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class TubeBurnEffect implements TeleportEffect {

    @Override
    public void apply(Actor actor, Location source, Location destination) {
        for (Exit exit : destination.getExits()) {
            Location adjacent = exit.getDestination();
            adjacent.setGround(new Fire(adjacent.getGround(), 2));
        }
    }
}