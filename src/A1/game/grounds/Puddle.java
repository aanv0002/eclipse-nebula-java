package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.DrinkPuddleAction;

/**
 * A small, stationary body of mysterious liquid on the ground.
 * In a standard video game, this would just be water. On a deprecated moon
 * in the Eclipse Nebula, it could be anything from spilled engine coolant to
 * highly corrosive alien saliva. Step in it at your own risk.
 */
public class Puddle extends Ground {
    public Puddle() {
        super('~', "Puddle");
    }

    /**
     * Allows the actor standing on the puddle to drink from it.
     *
     * @param actor the Actor acting
     * @param location the current Location
     * @param direction the direction of the Ground from the Actor
     * @return an action list containing the drink action when standing on this puddle
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();

        if (direction.equals("")) {
            actions.add(new DrinkPuddleAction());
        }

        return actions;
    }
}