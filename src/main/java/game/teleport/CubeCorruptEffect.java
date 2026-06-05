package game.teleport;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.ToxicWaste;

/**
 * Corrupts tiles around the teleport destination into toxic waste.
 *
 * @author Tan Hong Sze
 * @version 1.2
 */
public class CubeCorruptEffect implements TeleportEffect {

    /**
     * Permanently corrupts all adjacent destination tiles into Toxic Waste.
     *
     * @param actor the actor teleporting
     * @param source source location
     * @param destination final destination location
     */
    @Override
    public void apply(Actor actor, Location source, Location destination) {
        for (Exit exit : destination.getExits()) {
            Location adjacent = exit.getDestination();
            adjacent.setGround(new ToxicWaste());
        }

        new Display().println("The alien cube corrupts the surrounding destination ground into toxic waste.");
    }
}