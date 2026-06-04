package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;

/**
 * Intercepts hostile creatures crossing its coordinate.
 * * @author Shrabonti Majumder
 * @version 1.0
 */
public class LaserBarrierGround extends Ground {

    public LaserBarrierGround() {
        super('|', "Laser Barrier");
    }

    @Override
    public boolean canActorEnter(Actor actor) {
        return true;
    }

    @Override
    public void tick(Location location) {
        if (location.containsAnActor()) {
            Actor actor = location.getActor();
            if (actor.hasAbility(ActorCapabilities.CREATURE) || actor.hasAbility(ActorCapabilities.HOSTILE_TO_WORKER)) {
                new Display().println("The Laser Barrier cleanly intercepts " + actor + ", dealing massive burn damage!");
                actor.hurt(50);
                location.setGround(new Puddle());
            }
        }
    }
}