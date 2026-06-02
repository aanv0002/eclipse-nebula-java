package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;
import game.statuses.BlindStatus;

/**
 * A volatile crystal that explodes when stepped on.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class SolarCrystal extends Ground {

    private static final int EXPLOSION_DAMAGE = 4;
    private final Ground previousGround;

    /**
     * Creates a solar crystal over existing ground.
     *
     * @param previousGround previous ground
     */
    public SolarCrystal(Ground previousGround) {
        super('*', "Solar Crystal");
        this.previousGround = previousGround;
    }

    @Override
    public boolean canActorEnter(Actor actor) {
        return previousGround.canActorEnter(actor);
    }

    @Override
    public void tick(Location location) {
        if (!location.containsAnActor()) {
            return;
        }

        Actor trigger = location.getActor();

        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();

            if (adjacent.containsAnActor()) {
                Actor target = adjacent.getActor();
                target.hurt(EXPLOSION_DAMAGE);

                if (target.hasAbility(ActorCapabilities.WORKER)) {
                    target.addStatus(new BlindStatus());
                }
            }
        }

        trigger.hurt(EXPLOSION_DAMAGE);

        if (trigger.hasAbility(ActorCapabilities.WORKER)) {
            trigger.addStatus(new BlindStatus());
        }

        location.setGround(previousGround);
    }
}