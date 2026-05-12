package game.infection;

import edu.monash.fit2099.engine.positions.Location;
import game.actors.Parasite;
import game.items.Cookies;
import game.spawners.CreatureSpawnService;

/**
 * Infection effect for a Cookie host.
 * The infection consumes 1 cookie each turn and attempts to spawn another
 * Parasite on an adjacent tile.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class CookieInfectionEffect implements InfectionEffect {

    /**
     * The infected Cookies packet.
     */
    private final Cookies cookies;

    /**
     * Service used to spawn Parasites and apply spawn reactions.
     */
    private final CreatureSpawnService spawnService;

    /**
     * Creates a Cookie infection effect.
     *
     * @param cookies the infected Cookies packet
     */
    public CookieInfectionEffect(Cookies cookies) {
        this.cookies = cookies;
        this.spawnService = new CreatureSpawnService();
    }

    /**
     * Applies the immediate Cookie infection result.
     *
     * @param location the infected Cookies location
     * @return infection result message
     */
    @Override
    public String onInfected(Location location) {
        return "The infection starts consuming the Cookies.";
    }

    /**
     * Consumes cookie content and attempts to spawn a Parasite nearby.
     *
     * @param location the infected Cookies location
     */
    @Override
    public void tick(Location location) {
        cookies.reduceCookies(1);
        spawnService.spawnAdjacent(location, new Parasite());
    }

    /**
     * Returns whether the infection should continue.
     *
     * @return true while cookies remain
     */
    @Override
    public boolean isActive() {
        return cookies.hasCookies();
    }
}
