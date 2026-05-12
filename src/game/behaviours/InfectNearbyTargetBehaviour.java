package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.InfectAction;

/**
 * A Parasite behaviour that attempts to infect one nearby valid target.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class InfectNearbyTargetBehaviour implements Behaviour<Actor, Action> {

    /**
     * Selector used to choose the infection target.
     */
    private final InfectionTargetSelector targetSelector;

    /**
     * Creates the behaviour using the default adjacent target selector.
     */
    public InfectNearbyTargetBehaviour() {
        this(new AdjacentInfectableSelector());
    }

    /**
     * Creates the behaviour with a custom target selector.
     *
     * @param targetSelector the target selector
     */
    public InfectNearbyTargetBehaviour(InfectionTargetSelector targetSelector) {
        this.targetSelector = targetSelector;
    }

    /**
     * Returns an infect action if a valid target is nearby.
     *
     * @param actor the Parasite performing the behaviour
     * @param location the Parasite's current location
     * @return an InfectAction, or null if no target is available
     */
    @Override
    public Action operate(Actor actor, Location location) {
        InfectionTarget target = targetSelector.selectTarget(location);
        if (target == null) {
            return null;
        }
        return new InfectAction(target.infectable(), target.location());
    }
}
