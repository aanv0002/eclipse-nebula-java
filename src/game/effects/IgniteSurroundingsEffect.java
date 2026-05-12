package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.BurningGround;
import game.grounds.Fire;

/**
 * Ignites adjacent enterable tiles when a state transition occurs.
 *
 * This effect is triggered immediately when the mannequin enters BerserkState.
 *
 * @author Tan Hong Sze
 * @version 1.1
 */
public class IgniteSurroundingsEffect implements StateTransitionEffect {

    private static final int FIRE_DURATION = 3;

    @Override
    public void apply(Actor actor, Location location) {
        int ignitedTiles = 0;

        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();

            if (adjacent.canActorEnter(actor)
                    && adjacent.getGroundAs(BurningGround.class) == null) {
                adjacent.setGround(new Fire(adjacent.getGround(), FIRE_DURATION));
                ignitedTiles++;
            }
        }

        new Display().println(actor + "'s berserk energy ignites "
                + ignitedTiles + " surrounding tile(s).");
    }
}