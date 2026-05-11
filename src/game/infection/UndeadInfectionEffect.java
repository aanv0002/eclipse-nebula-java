package game.infection;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Infection effect for an Undead host.
 * Alien biology rejects the reanimated dead, causing the Undead to immediately
 * explode and die.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class UndeadInfectionEffect implements InfectionEffect {

    /**
     * The infected Undead.
     */
    private final Actor undead;

    /**
     * Whether this infection effect is still active.
     */
    private boolean active;

    /**
     * Creates an Undead infection effect.
     *
     * @param undead the infected Undead
     */
    public UndeadInfectionEffect(Actor undead) {
        this.undead = undead;
        this.active = true;
    }

    /**
     * Immediately kills and removes the infected Undead.
     *
     * @param location the infected Undead's location
     * @return infection result message
     */
    @Override
    public String onInfected(Location location) {
        undead.hurt(undead.getStatistic(ActorStatistics.HEALTH));
        location.map().removeActor(undead);
        active = false;
        return undead + " rejects the infection and blows up.";
    }

    /**
     * Does nothing because the Undead infection resolves immediately.
     *
     * @param location the infected Undead's location
     */
    @Override
    public void tick(Location location) {
    }

    /**
     * Returns whether this infection effect is still active.
     *
     * @return true if active
     */
    @Override
    public boolean isActive() {
        return active;
    }
}
