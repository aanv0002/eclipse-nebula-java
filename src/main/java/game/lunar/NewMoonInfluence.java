package game.lunar;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;
import game.actors.Undead;
import game.actors.Slime;

import java.util.ArrayList;
import java.util.List;

/**
 * A lunar influence activated when the API returns a New Moon phase.
 * Scans the map for Undead or Slime creatures in enclosed areas
 * near walls and triggers AmbushEvolution on selected creatures
 * making them dormant until a worker enters their detection range.
 *
 * @author Raima
 * @version 1.0
 */
public class NewMoonInfluence implements LunarInfluence {

    /**
     * The evolution behaviour applied to selected creatures.
     */
    private final EvolutionBehaviour evolution;

    /**
     * Minimum number of adjacent walls to consider a location enclosed.
     */
    private static final int WALL_THRESHOLD = 2;

    /**
     * Creates a NewMoonInfluence with AmbushEvolution behaviour.
     */
    public NewMoonInfluence() {
        this.evolution = new AmbushEvolution();
    }

    /**
     * Scans the map for Undead or Slime creatures in enclosed areas
     * near walls and applies AmbushEvolution to selected creatures.
     * A location is considered enclosed if it has at least two
     * adjacent wall tiles.
     *
     * @param map the active game map to apply the influence to
     * @param data the moon phase data retrieved from the API
     */
    @Override
    public void applyInfluence(GameMap map, MoonPhaseData data) {
        new Display().println("New Moon darkness falls! Creatures lurk in the shadows!");

        List<Actor> ambushTargets = new ArrayList<>();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location location = map.at(x, y);
                if (!location.containsAnActor()) continue;

                Actor actor = location.getActor();
                if (!(actor instanceof Undead) && !(actor instanceof Slime)) continue;

                int adjacentWalls = 0;
                for (Exit exit : location.getExits()) {
                    if (!exit.getDestination().canActorEnter(actor)) {
                        adjacentWalls++;
                    }
                }

                if (adjacentWalls >= WALL_THRESHOLD) {
                    ambushTargets.add(actor);
                }
            }
        }

        if (ambushTargets.isEmpty()) {
            new Display().println("New Moon: No creatures found in enclosed areas.");
            return;
        }

        for (Actor target : ambushTargets) {
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
        return "New Moon Influence";
    }
}