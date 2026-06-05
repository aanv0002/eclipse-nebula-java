package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.ToxicWaste;

/**
 * Corrupts adjacent enterable ground tiles into Toxic Waste.
 *
 * This effect is triggered immediately when the mannequin enters IdleState.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
public class CorruptSurroundingsEffect implements StateTransitionEffect {

    @Override
    public void apply(Actor actor, Location location) {
        int corruptedTiles = 0;

        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();

            if (adjacent.canActorEnter(actor)) {
                adjacent.setGround(new ToxicWaste());
                corruptedTiles++;
            }
        }

        new Display().println(actor + " becomes dormant and corrupts "
                + corruptedTiles + " surrounding tile(s) into Toxic Waste.");
    }
}