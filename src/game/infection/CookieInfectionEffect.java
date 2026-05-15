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

    private final Cookies cookies;
    private final CreatureSpawnService spawnService;

    public CookieInfectionEffect(Cookies cookies) {
        this.cookies = cookies;
        this.spawnService = new CreatureSpawnService();
    }

    @Override
    public String onInfected(Location location) {
        return "The infection starts consuming the Cookies.";
    }

    @Override
    public void tick(Location location) {
        cookies.reduceCookies(1);
        spawnService.spawnAdjacent(location, new Parasite());
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
