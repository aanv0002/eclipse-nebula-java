package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.security.SecurityHazard;
import game.security.SecuritySalvage;

/**
 * Action to disarm adjacent security hazards.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class DefuseSecurityAction extends Action {

    private final SecurityHazard hazard;
    private final Location hazardLocation;

    public DefuseSecurityAction(SecurityHazard hazard, Location hazardLocation) {
        this.hazard = hazard;
        this.hazardLocation = hazardLocation;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        hazard.disarm(hazardLocation);
        hazardLocation.addItem(new SecuritySalvage());
        return actor + " successfully defused the security hazard. A Security Salvage module was safely recovered.";
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " utilizes Defuse Kit on adjacent security hazard";
    }
}