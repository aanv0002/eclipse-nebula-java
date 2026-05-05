package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.AttackAction;
import game.actors.ActorCapabilities;

/**
 * A behaviour that attacks a nearby worker if one is adjacent.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class AttackNearbyWorkerBehaviour implements Behaviour<Actor, Action> {

    /**
     * Returns an attack action if a worker is nearby.
     *
     * @param actor the actor performing the behaviour
     * @param location the current location of the actor
     * @return an attack action, or null if no worker is adjacent
     */
    @Override
    public Action operate(Actor actor, Location location) {
        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            Actor target = destination.getActor();

            if (target != null && target.hasAbility(ActorCapabilities.WORKER)) {
                return new AttackAction(target, exit.getName(), actor.getIntrinsicWeapon());
            }
        }
        return null;
    }
}