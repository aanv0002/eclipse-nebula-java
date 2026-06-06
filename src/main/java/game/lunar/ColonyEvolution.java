package game.lunar;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.Slime;
import game.spawners.CreatureSpawnService;

/**
 * An evolution behaviour triggered by the Full Moon influence.
 * Causes clustered Slimes to split into additional Slimes on
 * nearby valid tiles, increasing local creature density on the map.
 *
 * @author Raima
 * @version 1.0
 */
public class ColonyEvolution implements EvolutionBehaviour {

    /**
     * Spawn service for placing new Slimes on the map.
     */
    private final CreatureSpawnService spawnService;

    /**
     * Creates a ColonyEvolution with a spawn service.
     */
    public ColonyEvolution() {
        this.spawnService = new CreatureSpawnService();
    }

    /**
     * Causes the target Slime to split into an additional Slime
     * on a nearby valid tile. Increases local creature density
     * as a result of the Full Moon lunar influence.
     *
     * @param target the slime actor to evolve
     * @param location the location of the target slime
     * @param map the active game map
     * @return a message describing the colony evolution effect
     */
    @Override
    public String evolve(Actor target, Location location, GameMap map) {
        Slime newSlime = new Slime();
        String result;

        if (spawnService.spawnAdjacent(location, newSlime)) {
            result = "Full Moon Colony Evolution: " + target
                    + " splits into an additional Slime nearby!";
        } else {
            result = "Full Moon Colony Evolution: " + target
                    + " tried to split but found no empty adjacent tile!";
        }

        new Display().println(result);
        return result;
    }

    /**
     * Returns the name of this evolution behaviour.
     *
     * @return evolution name
     */
    @Override
    public String getEvolutionName() {
        return "Colony Evolution";
    }
}