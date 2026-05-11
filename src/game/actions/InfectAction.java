package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.infection.Infectable;

/**
 * Parasite action that infects a target, then removes the parasite.
 */
public class InfectAction extends Action {

    private final Infectable target;
    private final Location targetLocation;

    public InfectAction(Infectable target, Location targetLocation) {
        this.target = target;
        this.targetLocation = targetLocation;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        String result = target.infect(actor, targetLocation);
        map.removeActor(actor);
        return result + System.lineSeparator() + actor + " dies after spreading the infection.";
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " infects a nearby target";
    }
}
