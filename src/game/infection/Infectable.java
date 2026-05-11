package game.infection;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A contract for actors or items that can be infected by a Parasite.
 * Each infectable host decides whether it can be infected and what happens
 * when infection is applied.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public interface Infectable {

    /**
     * Returns whether this target may currently receive an infection.
     *
     * @return true if this target is a valid infection target
     */
    boolean canBeInfected();

    /**
     * Applies infection from a Parasite.
     *
     * @param parasite the Parasite applying the infection
     * @param location the target location
     * @return a message describing the infection result
     */
    String infect(Actor parasite, Location location);
}
