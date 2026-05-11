package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.InfectNearbyTargetBehaviour;
import game.inventory.BasicInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * A parasite that infects adjacent valid hosts, dying immediately afterward.
 */
public class Parasite extends Actor {

    private static int counter = 1;

    private final Random random = new Random();
    private final Map<Integer, Behaviour<Actor, Action>> behaviours = new TreeMap<>();

    public Parasite() {
        super("#" + counter++ + " Parasite", 'x', 30, new BasicInventory());
        this.enableAbility(ActorCapabilities.CREATURE);
        this.enableAbility(ActorCapabilities.PARASITE);
        behaviours.put(1, new InfectNearbyTargetBehaviour());
    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        Location here = map.locationOf(this);

        for (Behaviour<Actor, Action> behaviour : behaviours.values()) {
            Action action = behaviour.operate(this, here);
            if (action != null) {
                return action;
            }
        }

        Action moveAction = getRandomMoveAction(here);
        if (moveAction != null) {
            return moveAction;
        }

        return new DoNothingAction();
    }

    private Action getRandomMoveAction(Location here) {
        List<Action> validMoves = new ArrayList<>();

        for (Exit exit : here.getExits()) {
            Action moveAction = exit.getDestination().getMoveAction(this, exit.getName(), exit.getHotKey());
            if (moveAction != null) {
                validMoves.add(moveAction);
            }
        }

        if (validMoves.isEmpty()) {
            return null;
        }

        return validMoves.get(random.nextInt(validMoves.size()));
    }
}
