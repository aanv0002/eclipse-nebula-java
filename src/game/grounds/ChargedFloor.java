package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.statuses.StunnedStatus;

/**
 * A static-filled floor tile spawned by a Static Pylon.
 * * @author Shrabonti Majumder
 * @version 1.0
 */
public class ChargedFloor extends Ground implements PurgeableTerrain {

    public ChargedFloor() {
        super('Z', "Charged Floor");
    }

    @Override
    public void tick(Location location) {
        if (location.containsAnActor()) {
            Actor actor = location.getActor();
            if (!actor.hasStatus(StunnedStatus.class)) {
                actor.addStatus(new StunnedStatus(1));
                new Display().println(actor + " steps on the Charged Floor and is electrocuted/stunned!");
            }
        }
    }

    @Override
    public void purge(Location location) {
        location.setGround(new Floor());
    }
}