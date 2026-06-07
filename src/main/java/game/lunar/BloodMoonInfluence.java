package game.lunar;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;

import java.util.ArrayList;
import java.util.List;

/**
 * A lunar influence activated when the API returns a Blood Moon phase
 * or when eclipse.is_blood_moon is true in the API response.
 * Scans the map for isolated Undead or Slime creatures and triggers
 * MutationEvolution replacing them with stronger MutatedCreatures
 * that spread CorruptionGround when walking.
 *
 * @author Raima
 * @version 1.0
 */
public class BloodMoonInfluence implements LunarInfluence {

    /**
     * The evolution behaviour applied to isolated creatures.
     */
    private final EvolutionBehaviour evolution;

    /**
     * Maximum number of adjacent creatures for a creature to be
     * considered isolated.
     */
    private static final int ISOLATION_THRESHOLD = 1;

    /**
     * Creates a BloodMoonInfluence with MutationEvolution behaviour.
     */
    public BloodMoonInfluence() {
        this.evolution = new MutationEvolution();
    }

    /**
     * Scans the map for isolated Undead or Slime creatures and applies
     * MutationEvolution to replace them with MutatedCreatures.
     * A creature is considered isolated if it has fewer than two
     * other creatures in its adjacent tiles.
     *
     * @param map the active game map to apply the influence to
     * @param data the moon phase data retrieved from the API
     */
    @Override
    public void applyInfluence(GameMap map, MoonPhaseData data) {
        new Display().println("Blood Moon rises! Isolated creatures begin to mutate!");

        List<Actor> mutationTargets = new ArrayList<>();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location location = map.at(x, y);
                if (!location.containsAnActor()) continue;

                Actor actor = location.getActor();
                if (!actor.hasAbility(ActorCapabilities.CREATURE)) continue;

                int adjacentCreatures = 0;
                for (Exit exit : location.getExits()) {
                    Actor adjacent = exit.getDestination().getActor();
                    if (adjacent != null && !adjacent.hasAbility(ActorCapabilities.WORKER)) {
                        adjacentCreatures++;
                    }
                }

                if (adjacentCreatures < ISOLATION_THRESHOLD) {
                    mutationTargets.add(actor);
                }
            }
        }

        if (mutationTargets.isEmpty()) {
            new Display().println("Blood Moon: No isolated creatures found to mutate.");
            return;
        }

        for (Actor target : mutationTargets) {
            Location location = map.locationOf(target);
            evolution.evolve(target, location, map);
        }
    }

    /**
     * Returns the name of this lunar influence.
     *
     * @return influence name
     */
    @Override
    public String getInfluenceName() {
        return "Blood Moon Influence";
    }
}