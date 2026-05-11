package game.infection;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Contract for actors or items that can be infected by a Parasite.
 */
public interface Infectable {

    boolean canBeInfected();

    String infect(Actor parasite, Location location);
}
