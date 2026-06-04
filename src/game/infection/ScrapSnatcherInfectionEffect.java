package game.infection;

import edu.monash.fit2099.engine.positions.Location;
import game.actors.ScrapSnatcher;

/**
 * Infection effect for Scrap Snatchers.
 * While infected:
 * - loses hoarding behaviour
 * - attacks nearby workers
 * - loses 1 hit point every turn until death
 *
 * @author Amra Anver
 * @version 1.0
 */
public class ScrapSnatcherInfectionEffect implements InfectionEffect {

    /**
     * The infected Scrap Snatcher.
     */
    private final ScrapSnatcher host;

    public ScrapSnatcherInfectionEffect(ScrapSnatcher host) {
        this.host = host;
    }

    /**
     * Executes the immediate effect when infection occurs.
     *
     * @param location the location of the infected Scrap Snatcher
     * @return a message describing the infection event
     */
    @Override
    public String onInfected(Location location) {
        return host + " becomes infected and turns rabid!";
    }


    /**
     * Applies infection effects for the current turn.
     * The infected Scrap Snatcher loses 1 hit point every turn
     * while it remains alive.
     *
     * @param location the location of the infected Scrap Snatcher
     */
    @Override
    public void tick(Location location) {

        if (!host.isConscious()) {
            return;
        }

        host.hurt(1);
    }

    /**
     * Returns whether the infection remains active.
     * The infection ends when the host dies.
     *
     * @return true if the host is still alive
     */
    @Override
    public boolean isActive() {
        return host.isConscious();
    }
}