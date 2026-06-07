package game.security;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Governs operational modules or utility tools carried or deployed by the worker.
 *
 * @author Shrabonti Majumder
 * @version 1.1
 */
public interface GridCountermeasure {
    /**
     * Returns the type of countermeasure this module represents.
     * @return a string identifying the countermeasure type
     */
    String getCountermeasureType();

    /**
     * Returns the actions this countermeasure offers while carried by an actor.
     * @param owner the actor carrying the countermeasure
     * @param map the map the actor is on
     * @return the available countermeasure actions
     */
    ActionList allowableActions(Actor owner, GameMap map);
}