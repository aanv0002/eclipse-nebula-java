package game.lunar;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;

/**
 * A corrupted ground tile created by MutationEvolution and spread
 * by MutatedCreature when walking. Actors standing on CorruptionGround
 * receive a weakening effect that causes them to take increased damage
 * from attacks. Workers standing on this ground take 1 damage per turn
 * from the corrupting energy.
 *
 * @author Raima
 * @version 1.0
 */
public class CorruptionGround extends Ground {

    /**
     * Damage dealt to actors standing on corruption ground per turn.
     */
    private static final int CORRUPTION_DAMAGE = 1;

    /**
     * Creates a CorruptionGround tile.
     */
    public CorruptionGround() {
        super('¤', "Corruption Ground");
    }

    /**
     * Deals corruption damage to any actor standing on this tile each turn.
     * Workers take damage from the corrupting energy seeping through
     * the ground left behind by MutatedCreatures.
     *
     * @param location the location of this ground
     */
    @Override
    public void tick(Location location) {
        if (location.containsAnActor()) {
            Actor actor = location.getActor();
            if (actor.hasAbility(ActorCapabilities.WORKER)) {
                actor.hurt(CORRUPTION_DAMAGE);
                new Display().println(actor + " is weakened by the corruption"
                        + " ground taking " + CORRUPTION_DAMAGE + " damage!");
            }
        }
    }

    /**
     * Allows all actors to enter corruption ground.
     *
     * @param actor the actor attempting to enter
     * @return true always
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return true;
    }
}