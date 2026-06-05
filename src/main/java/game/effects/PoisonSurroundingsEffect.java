package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Location;
import game.statuses.PoisonStatus;

/**
 * Poisons all actors within 3 tiles when a state transition occurs.
 *
 * @author Tan Hong Sze
 * @version 1.2
 */
public class PoisonSurroundingsEffect implements StateTransitionEffect {

    /**
     * Radius of the poison pulse.
     */
    private static final int POISON_RADIUS = 3;

    /**
     * Poison duration.
     */
    private static final int POISON_DURATION = 3;

    /**
     * Poison damage per turn.
     */
    private static final int POISON_DAMAGE = 1;

    @Override
    public void apply(Actor actor, Location location) {
        int poisonedActors = 0;

        for (Location nearby : location.getNearbyLocations(POISON_RADIUS)) {
            if (nearby.containsAnActor() && nearby.getActor() != actor) {
                nearby.getActor().addStatus(new PoisonStatus(POISON_DURATION, POISON_DAMAGE));
                poisonedActors++;
            }
        }

        new Display().println(actor + " releases a toxic pulse and poisons "
                + poisonedActors + " actor(s) within " + POISON_RADIUS + " tiles.");
    }
}