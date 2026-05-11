package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.InfectAction;

/**
 * Parasite behaviour that infects one nearby valid target.
 */
public class InfectNearbyTargetBehaviour implements Behaviour<Actor, Action> {

    private final InfectionTargetSelector targetSelector;

    public InfectNearbyTargetBehaviour() {
        this(new AdjacentInfectableSelector());
    }

    public InfectNearbyTargetBehaviour(InfectionTargetSelector targetSelector) {
        this.targetSelector = targetSelector;
    }

    @Override
    public Action operate(Actor actor, Location location) {
        InfectionTarget target = targetSelector.selectTarget(location);
        if (target == null) {
            return null;
        }
        return new InfectAction(target.infectable(), target.location());
    }
}
