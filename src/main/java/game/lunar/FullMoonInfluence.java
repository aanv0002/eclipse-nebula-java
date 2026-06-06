package game.lunar;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;
import game.actors.Slime;

import java.util.ArrayList;
import java.util.List;

/**
 * A lunar influence activated when the API returns a Full Moon phase.
 * Scans the map for clustered Slimes and triggers ColonyEvolution
 * on selected clusters, increasing local creature density on the map.
 *
 * @author Raima
 * @version 1.0
 */
public class FullMoonInfluence implements LunarInfluence {

    /**
     * The evolution behaviour applied to clustered Slimes.
     */
    private final EvolutionBehaviour evolution;

    /**
     * Minimum number of nearby Slimes to trigger colony evolution.
     */
    private static final int CLUSTER_THRESHOLD = 2;

    /**
     * Creates a FullMoonInfluence with ColonyEvolution behaviour.
     */
    public FullMoonInfluence() {
        this.evolution = new ColonyEvolution();
    }

    /**
     * Scans the map for clustered Slimes and applies ColonyEvolution
     * to each cluster found. A cluster is defined as a Slime with
     * at least two other Slimes within its adjacent tiles.
     *
     * @param map the active game map to apply the influence to
     * @param data the moon phase data retrieved from the API
     */
    @Override
    public void applyInfluence(GameMap map, MoonPhaseData data) {
        new Display().println("Full Moon rising! Slime colonies become unstable!");

        List<Actor> clusteredSlimes = new ArrayList<>();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location location = map.at(x, y);
                if (!location.containsAnActor()) continue;

                Actor actor = location.getActor();
                if (!(actor instanceof Slime)) continue;

                int nearbySlimes = 0;
                for (edu.monash.fit2099.engine.positions.Exit exit : location.getExits()) {
                    Actor adjacent = exit.getDestination().getActor();
                    if (adjacent instanceof Slime) {
                        nearbySlimes++;
                    }
                }

                if (nearbySlimes >= CLUSTER_THRESHOLD) {
                    clusteredSlimes.add(actor);
                }
            }
        }

        if (clusteredSlimes.isEmpty()) {
            new Display().println("Full Moon: No Slime clusters detected on this map.");
            return;
        }

        for (Actor slime : clusteredSlimes) {
            Location location = map.locationOf(slime);
            evolution.evolve(slime, location, map);
        }
    }

    /**
     * Returns the name of this lunar influence.
     *
     * @return influence name
     */
    @Override
    public String getInfluenceName() {
        return "Full Moon Influence";
    }
}