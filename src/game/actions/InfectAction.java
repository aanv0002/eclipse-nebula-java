package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.infection.Infectable;

/**
 * An action that allows a Parasite to infect a selected target.
 * After successfully applying infection, the Parasite dies immediately.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class InfectAction extends Action {

    /**
     * The target to infect.
     */
    private final Infectable target;

    /**
     * The location of the infection target.
     */
    private final Location targetLocation;

    /**
     * Creates an infect action.
     *
     * @param target the infection target
     * @param targetLocation the location of the infection target
     */
    public InfectAction(Infectable target, Location targetLocation) {
        this.target = target;
        this.targetLocation = targetLocation;
    }

    /**
     * Executes the infection and removes the Parasite from the map.
     *
     * @param actor the Parasite performing the infection
     * @param map the map the Parasite is on
     * @return the result message
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = target.infect(actor, targetLocation);
        map.removeActor(actor);
        return result + System.lineSeparator() + actor + " dies after spreading the infection.";
    }

    /**
     * Returns the menu description.
     *
     * @param actor the actor performing the action
     * @return menu description
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " infects a nearby target";
    }
}
