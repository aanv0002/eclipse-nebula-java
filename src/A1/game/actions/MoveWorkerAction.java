package game.actions;

import edu.monash.fit2099.engine.actions.MoveActorAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.BurningGround;
import game.statuses.BurningStatus;

import java.util.List;

/**
 * A movement action for workers that applies immediate burning if the actor steps
 * onto a burning ground.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class MoveWorkerAction extends MoveActorAction {

    /**
     * Destination of the move.
     */
    private final Location destination;

    /**
     * Creates a movement action for a worker.
     *
     * @param destination the move destination
     * @param direction the direction of the move
     * @param hotKey the menu hotkey
     */
    public MoveWorkerAction(Location destination, String direction, String hotKey) {
        super(destination, direction, hotKey);
        this.destination = destination;
    }

    /**
     * Moves the actor and applies burning if the actor steps onto fire.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return the result of the movement
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = super.execute(actor, map);

        if (destination.getGroundAs(BurningGround.class) != null) {
            applyBurn(actor);
        }

        return result;
    }

    /**
     * Applies burning when stepping onto fire.
     *
     * @param actor the actor stepping onto fire
     */
    private void applyBurn(Actor actor) {
        List<BurningStatus> burningStatuses = actor.statusesOf(BurningStatus.class);

        if (burningStatuses.isEmpty()) {
            actor.addStatus(new BurningStatus(5));
        }
    }
}