package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;

import java.util.*;

/**
 * A behaviour that moves the actor toward the nearest worker.
 *
 * This implementation uses breadth-first search (BFS) to find a reachable path
 * to a location adjacent to a worker, allowing the actor to move around walls
 * and other obstacles instead of getting stuck when a direct path is blocked.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class FollowNearestWorkerBehaviour implements Behaviour<Actor, Action> {

    /**
     * Chooses a movement action that leads the actor along a shortest reachable path
     * to a tile adjacent to the nearest worker.
     *
     * @param actor the actor performing the behaviour
     * @param location the actor's current location
     * @return a move action toward a reachable worker-adjacent tile, or null if no path exists
     */
    @Override
    public Action operate(Actor actor, Location location) {
        Queue<Location> queue = new ArrayDeque<>();
        Map<Location, Action> firstMoveMap = new HashMap<>();
        Set<Location> visited = new HashSet<>();

        queue.add(location);
        visited.add(location);

        while (!queue.isEmpty()) {
            Location current = queue.poll();

            if (isAdjacentToWorker(current)) {
                return firstMoveMap.get(current);
            }

            for (Exit exit : current.getExits()) {
                Location destination = exit.getDestination();

                if (visited.contains(destination)) {
                    continue;
                }

                Action moveAction = destination.getMoveAction(actor, exit.getName(), exit.getHotKey());
                if (moveAction == null) {
                    continue;
                }

                visited.add(destination);
                queue.add(destination);

                if (current.equals(location)) {
                    firstMoveMap.put(destination, moveAction);
                } else {
                    firstMoveMap.put(destination, firstMoveMap.get(current));
                }
            }
        }

        return null;
    }

    /**
     * Returns whether the given location is adjacent to a worker.
     *
     * @param location the location to check
     * @return true if a worker is in one of the neighbouring locations
     */
    private boolean isAdjacentToWorker(Location location) {
        for (Exit exit : location.getExits()) {
            Location neighbour = exit.getDestination();
            if (neighbour.containsAnActor()) {
                Actor target = neighbour.getActor();
                if (target.hasAbility(ActorCapabilities.WORKER)) {
                    return true;
                }
            }
        }
        return false;
    }
}