package game.behaviours;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.infection.Infectable;

/**
 * Selects the first valid infectable target in the adjacent tiles.
 * Actors are checked before carried items, and carried items are checked
 * before ground items on the same adjacent tile.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class AdjacentInfectableSelector implements InfectionTargetSelector {

    /**
     * Selects a valid adjacent infectable target.
     *
     * @param location the Parasite's current location
     * @return the selected infection target, or null if none exists
     */
    @Override
    public InfectionTarget selectTarget(Location location) {
        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            Infectable actorTarget = destination.getActorAs(Infectable.class);

            if (actorTarget != null && actorTarget.canBeInfected()) {
                return new InfectionTarget(actorTarget, destination);
            }

            Actor actor = destination.getActor();
            if (actor != null) {
                for (Infectable carriedItemTarget
                        : actor.getInventory().getItemsAs(Infectable.class)) {
                    if (carriedItemTarget.canBeInfected()) {
                        return new InfectionTarget(carriedItemTarget, destination);
                    }
                }
            }

            for (Infectable itemTarget : destination.getItemsAs(Infectable.class)) {
                if (itemTarget.canBeInfected()) {
                    return new InfectionTarget(itemTarget, destination);
                }
            }
        }
        return null;
    }
}
