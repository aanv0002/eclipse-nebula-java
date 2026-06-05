package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.actions.MoveActorAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * /**
 * Behaviour that moves an actor to a random neighbouring location.
 * The behaviour considers all adjacent locations that:
 * - can be entered by the actor
 * - do not already contain another actor
 * One valid destination is selected at random and returned as a MoveActorAction
 * If no valid destinations exist, no action is returned.
 *
 * @author Amra Anver
 * @version 1.0
 */
public class RandomMoveBehaviour implements Behaviour<Actor, Action> {
    /**
     * Chooses a random valid movement action.
     *
     * @param actor the actor performing the behaviour
     * @param location the actor's current location
     * @return a random movement action, or null if movement is impossible
     */
    @Override
    public Action operate(Actor actor, Location location) {

        List<Action> actions = new ArrayList<>();

        for (Exit exit : location.getExits()) {

            Location destination = exit.getDestination();

            if (destination.canActorEnter(actor)
                    && !destination.containsAnActor()) {

                actions.add(
                        new MoveActorAction(destination, exit.getName())
                );
            }
        }

        if (actions.isEmpty()) {
            return null;
        }

        Collections.shuffle(actions);

        return actions.get(0);
    }
}