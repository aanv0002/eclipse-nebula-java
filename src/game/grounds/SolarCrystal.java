package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;
import game.statuses.BlindStatus;

import java.util.List;

/**
 * A volatile crystal that can spread and explode when stepped on.
 *
 * SolarCrystal is spawned by CrystalBloomWeather. It does not spread
 * immediately when created. Instead, on later ground ticks, it has a chance
 * to spread into a nearby valid tile. If an actor stands on it, the crystal
 * explodes, damages nearby actors, blinds workers, and restores the original
 * ground underneath.
 *
 * @author Tan Hong Sze
 * @version 2.1
 */
public class SolarCrystal extends Ground {

    /**
     * Damage dealt by the crystal explosion.
     */
    private static final int EXPLOSION_DAMAGE = 4;

    /**
     * Chance for the crystal to spread each tick.
     */
    private static final double SPREAD_CHANCE = 0.75;

    /**
     * Ground that existed before this crystal appeared.
     */
    private final Ground previousGround;

    /**
     * Whether this crystal has already had its first tick.
     * This prevents spreading immediately on the same turn it is created.
     */
    private boolean hasActivated;

    /**
     * Creates a solar crystal over existing ground.
     *
     * @param previousGround ground that should be restored after explosion
     */
    public SolarCrystal(Ground previousGround) {
        super('*', "Solar Crystal");
        this.previousGround = previousGround;
        this.hasActivated = false;
    }

    @Override
    public boolean canActorEnter(Actor actor) {
        return previousGround.canActorEnter(actor);
    }

    @Override
    public void tick(Location location) {
        if (location.containsAnActor()) {
            explode(location);
            return;
        }

        if (!hasActivated) {
            hasActivated = true;
            return;
        }

        spread(location);
    }

    /**
     * Spreads the crystal to one nearby valid tile based on spread chance.
     *
     * @param location current crystal location
     */
    private void spread(Location location) {
        if (Math.random() >= SPREAD_CHANCE) {
            return;
        }

        List<Location> validTargets = new java.util.ArrayList<>();

        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();

            if (canSpreadTo(adjacent)) {
                validTargets.add(adjacent);
            }
        }

        if (validTargets.isEmpty()) {
            return;
        }

        Location target = validTargets.get(
                new java.util.Random().nextInt(validTargets.size())
        );

        target.setGround(new SolarCrystal(target.getGround()));

        new Display().println("Solar Crystal spreads from ("
                + location.x() + "," + location.y()
                + ") to ("
                + target.x() + "," + target.y()
                + ").");
    }

    /**
     * Checks whether the crystal can spread to the target location.
     *
     * @param location target location
     * @return true if the target location can receive a crystal
     */
    private boolean canSpreadTo(Location location) {
        return !location.containsAnActor()
                && location.getItems().isEmpty();
    }

    /**
     * Explodes the crystal, damaging actors in the current and adjacent tiles.
     *
     * @param location crystal location
     */
    private void explode(Location location) {
        Actor trigger = location.getActor();

        damageAndBlind(trigger);

        for (Exit exit : location.getExits()) {
            Location adjacent = exit.getDestination();

            if (adjacent.containsAnActor()) {
                damageAndBlind(adjacent.getActor());
            }
        }

        location.setGround(previousGround);
    }

    /**
     * Damages the actor and blinds workers.
     *
     * @param actor actor affected by the explosion
     */
    private void damageAndBlind(Actor actor) {
        actor.hurt(EXPLOSION_DAMAGE);

        if (actor.hasAbility(ActorCapabilities.WORKER)) {
            actor.addStatus(new BlindStatus());
            new Display().println(actor + " takes "
                    + EXPLOSION_DAMAGE
                    + " damage from exploded crystal and becomes blinded.");
        } else {
            new Display().println(actor + " takes "
                    + EXPLOSION_DAMAGE
                    + " damage.");
        }
    }
}