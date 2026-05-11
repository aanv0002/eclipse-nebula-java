package game.behaviours;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.infection.Infectable;

/**
 * Selects the first valid adjacent infectable target.
 */
public class AdjacentInfectableSelector implements InfectionTargetSelector {

    @Override
    public InfectionTarget selectTarget(Location location) {
        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            Infectable actorTarget = destination.getActorAs(Infectable.class);

            if (actorTarget != null && actorTarget.canBeInfected()) {
                return new InfectionTarget(actorTarget, destination);
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
